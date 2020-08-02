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

public class SetMusicChannelCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		if(m.hasPermission(Permission.ADMINISTRATOR)) {
			if(!message.getMentionedChannels().isEmpty()) {
				ResultSet set = LiteSQL.onQuery("SELECT * FROM mcs WHERE guildid = " + m.getGuild().getId());
				
				if(set != null) {
					LiteSQL.onUpdate("DELETE FROM mcs WHERE guildid = " + m.getGuild().getIdLong());
				}
				
				LiteSQL.onUpdate("INSERT INTO mcs(guildid, channelid) values(" + m.getGuild().getIdLong() + ", " + message.getMentionedChannels().get(0).getIdLong() + ")");
				MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).channel = message.getMentionedChannels().get(0);
				
				//sendImage(message.getMentionedChannels().get(0));
				
				Msg.info(channel, "Neuer Musikchannel festgelegt", message, 3);
				
				System.out.println("**INFO: Neuner Music-Channel auf " + channel.getGuild().getName() + " (" + channel.getGuild().getIdLong() + ") festgelegt (" + message.getMentionedChannels().get(0).getAsMention() + ")");
			}
			
			else {
				Msg.ussage(channel, "<#channel>", message);
			}
		}
		
		else {
			Msg.unlicensed(channel, "Admin", message);
		}
	}
	
	/*private void sendImage(TextChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
	
		builder.setImage(channel.getGuild().getMemberById(732180032004292638l).getUser().getAvatarUrl());
		
		channel.sendMessage(builder.build()).queue();
	}*/
}
