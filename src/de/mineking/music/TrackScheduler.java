package de.mineking.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.mineking.Colors;
import de.mineking.MineKingBot;
import net.dv8tion.jda.api.EmbedBuilder;

public class TrackScheduler extends AudioEventAdapter {
	@Override
	public void onPlayerPause(AudioPlayer player) {
		
	}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {
		
	}
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		long guildid = MineKingBot.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guildid);
		
		if(controller.channel != null) {			
			AudioTrackInfo info = track.getInfo();
			
			long sec = info.length / 1000;
			long min = sec / 60;
			long h = min / 60;
			
			sec %= 60;
			min %= 60;
			
			String url = info.uri;
			url = url.replace("https://", "http://");
			
			EmbedBuilder builder = new EmbedBuilder();
			
			builder.setColor(Colors.std);
			builder.setTitle("Jetzt Läuft: **" + info.title.toUpperCase() + "** von **" + info.author.toUpperCase() + "**");
			builder.setDescription("Länge: " + (info.isStream ? ":red_circle: Stream" : (h > 0 ? h + "h " : "") + min + "min " + sec + "sec"));
			builder.setImage(MusicUtil.getThumbnailUrl(url));
			
			controller.trackinfo = controller.channel.sendMessage(builder.build()).complete();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			controller.trackinfo.addReaction("⏹").queue(); //⏹  ⏯ ⏭ 🔁 ⭐❌
			controller.trackinfo.addReaction("⏯").queue();
			controller.trackinfo.addReaction("⏭").queue();
			controller.trackinfo.addReaction("🔁").queue();
			controller.trackinfo.addReaction("❌").queue();
			controller.trackinfo.addReaction("⭐").queue();
			
			controller.getQueue().printQueue(controller.channel);
		}
	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		long guildid = MineKingBot.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		
		queue.next();
		
		//TODO may remove
		controller.getQueue().printQueue(controller.channel);
		
		if(controller.trackinfo != null) {
			controller.trackinfo.delete().queue();
			controller.trackinfo = null;
		}
	}
}
