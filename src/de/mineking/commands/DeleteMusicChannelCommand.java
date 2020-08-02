package de.mineking.commands;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteMusicChannelCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		if(m.hasPermission(Permission.ADMINISTRATOR)) {
			LiteSQL.onUpdate("DELETE FROM mcs WHERE guildid = " + m.getGuild().getIdLong());
			MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).channel = null;
			
			Msg.info(channel, "MusicChannel wurde entfernt", message, 5);
			
			System.out.println("**INFO: Music-Channel auf " + channel.getGuild().getName() + " (" + channel.getGuild().getIdLong() + ") entfernt");
		}
		
		else {
			Msg.unlicensed(channel, "Admin", message);;
		}
	}
}
