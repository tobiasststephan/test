package de.mineking.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.mineking.Colors;
import de.mineking.MineKingBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Queue {
	private List<String> queuelist;
	private int pos;
	private boolean loop;
	
	private MusicController controller;
	
	public Message queuelistmessage;
	
	public Queue(MusicController controller) {
		queuelist = new ArrayList<String>();
		
		pos = 0;
		loop = false;
		
		this.controller = controller;
	}
	
	public void addTrackToQueue(String url) {
		queuelist.add(url);
	}
	
	public boolean next() {
		if(pos < this.queuelist.size()) {
			String url = queuelist.get(pos);
			
			if(url != null) {
				AudioPlayerManager apm = MineKingBot.INSTANCE.audioPlayerManager;
				apm.loadItem(url, new AudioLoadResult(controller, url));
				
				pos++;
				
				return true;
			}
		}
		
		else if(loop) {
			pos = 0;
			
			String url = queuelist.get(pos);
			
			if(url != null) {
				AudioPlayerManager apm = MineKingBot.INSTANCE.audioPlayerManager;
				apm.loadItem(url, new AudioLoadResult(controller, url));
				
				pos++;
				
				return true;
			}
		}
		
		else {
			AudioManager manager = controller.getGuild().getAudioManager();
			manager.closeAudioConnection();
			
			delQueuelistmessage();
			
			controller.setQueue(new Queue(controller));
		}
		
		return false;
	}
	
	public boolean getLoop() {
		return loop;
	}
	
	public void setLoop(boolean newstate) {
		loop = newstate;
	}
	
	public void setQueuelist(List<String> queuelist) {
		this.queuelist = queuelist;
	}
	
	public List<String> getQueuelist() {
		return queuelist;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public void delQueuelistmessage() {
		if(queuelistmessage != null) {
			queuelistmessage.delete().queue();
			queuelistmessage = null;
		}
	}
	
	public void printQueue(TextChannel channel) {
		delQueuelistmessage();
		
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setColor(Colors.std);		
		builder.setTitle("Playlist: ");
		
		for(int i = 0; i < queuelist.size(); i++) {
			if(i + 1 == pos) {
				builder.appendDescription("**-> " + MusicUtil.getTitle(queuelist.get(i)) + "**\n");
			}
			
			else {
				builder.appendDescription((i + 1) + ". " + MusicUtil.getTitle(queuelist.get(i)) + "\n");
			}
			
			if(i >= 29) {
				builder.appendDescription("...\n");
				i = queuelist.size();
				break;
			}
		}
		
		if(queuelist.size() >= 1) {
			queuelistmessage = channel.sendMessage(builder.build()).complete();
		}
		
		else {
			delQueuelistmessage();
		}
	} 
}
