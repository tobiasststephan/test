package de.mineking.music;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class MusicController {
	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	public TextChannel channel;
	public Role dj; 
	
	public Message trackinfo;
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = MineKingBot.INSTANCE.audioPlayerManager.createPlayer();
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(10);
		
		this.queue = new Queue(this);
		trackinfo = null;
		
		ResultSet set = LiteSQL.onQuery("SELECT * FROM mcs WHERE guildid = " + guild.getIdLong());
		
		try {
			if(!set.isClosed()) {
				try {
					channel = guild.getTextChannelById(set.getLong("channelid"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setQueue(Queue queue) {
		this.queue = queue;
	}
	
	public Queue getQueue() {
		return queue;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
}
