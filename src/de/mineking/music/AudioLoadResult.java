package de.mineking.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioLoadResult implements AudioLoadResultHandler {
	private final MusicController controller;
	
	public AudioLoadResult(MusicController controller, String uri) {
		this.controller = controller;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		controller.getPlayer().playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {		
		for(AudioTrack track : playlist.getTracks()) {			
			controller.getQueue().addTrackToQueue(track.getInfo().uri);
		}
		
		controller.getQueue().getQueuelist().remove(controller.getQueue().getPos() - 1);
		
		controller.getQueue().setPos(controller.getQueue().getPos() - 1);
		controller.getQueue().next();
	}

	@Override
	public void noMatches() {
		
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		
	}
}
