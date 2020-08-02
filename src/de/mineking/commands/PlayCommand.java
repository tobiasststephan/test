package de.mineking.commands;

import java.sql.ResultSet;
import java.sql.SQLException;


import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicController;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			if(args.length >= 2) {
				GuildVoiceState state;
				if((state = m.getVoiceState()) != null) {
					VoiceChannel vc;
					if((vc = state.getChannel()) != null) {
						MusicController controller = MineKingBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
						AudioManager manager = vc.getGuild().getAudioManager();
						
						String url = args[1];						
						
						url = url.replace("https://", "http://").split("&")[0];
						
						if(!url.startsWith("http://www.youtube.com/")) {
							boolean found = false;
							
							ResultSet set = LiteSQL.onQuery("SELECT url FROM mfs WHERE memberid = " + m.getIdLong() + " AND shortcut = '" + url + "'");
							
							try {
								if(set != null && set.next()) {
									url = set.getString("url");
									found = true;
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
							if(!found) {
								Msg.error(channel, "Input ist keine Youtube-Url und kein shortcut", message);
								return;
							}
						}
						
						if(controller.getQueue().getQueuelist().contains(url)) {
							Msg.error(channel, "Song ist bereits in der Playlist", message);
						}
						
						else {
							controller.getQueue().addTrackToQueue(url);
							
							if(manager.getConnectionStatus() != ConnectionStatus.CONNECTED) {
								manager.openAudioConnection(vc);
								manager.setSelfDeafened(true);
								
								controller.getQueue().next();
							}
							
							if(controller.channel != null) {
								controller.getQueue().printQueue(controller.channel);
							}
						}
					}
					
					else {
						Msg.error(channel, "Du musst in einem Voicechannel sein, um einen Track zu starten", message);
					}
				}
				
				else {
					Msg.error(channel, "Du musst in einem Voicechannel sein, um einen Track zu starten", message);
				}
			}
			
			else {
				Msg.ussage(channel, "<youtube-url/shortcut>", message);
			}
		}
		
		else {
			Msg.unlicensed(channel, "DJ", message);
		}
	}
}
