package de.mineking.commands;

import java.sql.ResultSet;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetDJCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		if(m.hasPermission(Permission.ADMINISTRATOR)) {
			if(!message.getMentionedRoles().isEmpty()) {
				ResultSet set = LiteSQL.onQuery("SELECT * FROM djs WHERE guildid = " + m.getGuild().getId());
				
				if(set != null) {
					LiteSQL.onUpdate("DELETE FROM djs WHERE guildid = " + m.getGuild().getIdLong());
				}
				
				LiteSQL.onUpdate("INSERT INTO djs(guildid, roleid) values(" + m.getGuild().getIdLong() + ", " + message.getMentionedRoles().get(0).getIdLong() + ")");
				MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj = message.getMentionedRoles().get(0);
				
				Msg.info(channel, "Neue DJ Rolle festgelegt", message, 3);
				
				System.out.println("**INFO: Neune DJ-Rolle auf " + channel.getGuild().getName() + " (" + channel.getGuild().getIdLong() + ") festgelegt (" + message.getMentionedRoles().get(0).getAsMention() + ")");
			}
			
			else {
				Msg.ussage(channel, "<@rolle>", message);
			}
		}
		
		else {
			Msg.unlicensed(channel, "Admin", message);;
		}
	}
}
