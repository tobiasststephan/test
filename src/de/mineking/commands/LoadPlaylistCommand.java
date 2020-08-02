package de.mineking.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicController;
import de.mineking.music.Queue;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LoadPlaylistCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			if(args.length == 2) {
				String shortcut = args[1];
				
				boolean found = false;
				
				ResultSet set = LiteSQL.onQuery("SELECT urls FROM pls WHERE guildid = " + channel.getGuild().getIdLong() + " AND shortcut = '" + shortcut + "'");
				
				try {
					if(set != null && set.next()) {
						GuildVoiceState state;
						if((state = m.getVoiceState()) != null) {
							VoiceChannel vc;
							if((vc = state.getChannel()) != null) {
								String urls = set.getString("urls");
								
								MusicController controller = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
								AudioManager manager = vc.getGuild().getAudioManager();
								
								if(controller.getQueue().getQueuelist().size() == 0) {
									manager.openAudioConnection(vc);
									manager.setSelfDeafened(true);
									
									controller.setQueue(new Queue(controller));
									
									if(controller.trackinfo != null) {
										controller.trackinfo.delete().queue();
										controller.trackinfo = null;
									}
									
									controller.getQueue().delQueuelistmessage();
									
									List<String> queuelist = new ArrayList<String>();
									
									for(String url : urls.split(" ")) {
										queuelist.add(url);
									}
									
									controller.getQueue().setQueuelist(queuelist);
									controller.getQueue().printQueue(controller.channel);
									
									
									controller.getQueue().next();
									
									Msg.info(channel, "Playlist geladen", message, 5);
								}
								
								else {
									Msg.error(channel, "Es läuft bereits eine andere Playlist", message);
								}
							}
						}
						
						found = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(!found) {
					Msg.error(channel, "Konnte keine Playlist finden", message);
					return;
				}
			}
			
			else {
				Msg.ussage(channel, "<shortcut>", message);
			}
		}
		
		else {
			Msg.unlicensed(channel, "DJ", message);
		}
	}
}
