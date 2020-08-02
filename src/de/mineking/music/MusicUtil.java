package de.mineking.music;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class MusicUtil {
	private static JSONObject getObj(String url) {
		try {
			URL page = new URL("https://www.youtube.com/oembed?url=" + url + "&format=json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(page.openStream()));
			
			String line = reader.readLine();
			
			JSONParser parser = new JSONParser();
			
			try {
				return (JSONObject) parser.parse(line);
			}
			
			 catch (ParseException e) { 
				e.printStackTrace(); 
			}
		}
		
		catch(IOException e) { 
			e.printStackTrace(); 
		}
		
		return null;
	}
	
	public static String getTitle(String url) {
		return getObj(url).get("title").toString();
	}
	
	public static String getAuthor(String url) {
		return getObj(url).get("author_name").toString();
	}
	
	public static String getAuthorUrl(String url) {
		return getObj(url).get("author_url").toString();
	}
	
	public static String getThumbnailUrl(String url) {
		return getObj(url).get("thumbnail_url").toString();
	}

	
	public static void addFavourite(Member m, String[] args, TextChannel channel) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
		
		if(args.length == 3) {
			String url = args[2];
			String shortcut = args[1];
			
			if(url.replace("https://", "http://").startsWith("http://www.youtube.com/watch?v=")) {
				LiteSQL.onUpdate("DELETE FROM mfs WHERE shortcut = '" + shortcut + "' AND memberid = " + m.getIdLong());
				LiteSQL.onUpdate("INSERT INTO mfs(memberid, url, shortcut) VALUES(" + m.getIdLong() + ", '" + url + "', '" + shortcut + "')");
				Msg.info(channel, m.getAsMention() + ", **" + MusicUtil.getTitle(url) + "** wurde unter **" + shortcut + "** gespeichert", 5);
			}
			
			else {
				Msg.error(channel, "angegebene URL ist nicht gültig", "&addFavourite");
			}
		}
		
		else if(args.length == 2) {
			if(controller.getPlayer().getPlayingTrack() != null) {
				String url = controller.getQueue().getQueuelist().get(controller.getQueue().getPos() - 1);
				String shortcut = args[1];
				
				LiteSQL.onUpdate("DELETE FROM mfs WHERE shortcut = '" + shortcut + "' AND memberid = " + m.getIdLong());
				LiteSQL.onUpdate("INSERT INTO mfs(memberid, url, shortcut) VALUES("+ m.getIdLong() + ", '" + url + "', '" + shortcut + "')");
				Msg.info(channel, m.getAsMention() + ", **" + MusicUtil.getTitle(url) + "** wurde unter **" + shortcut + "** gespeichert", 5);
			}
			
			else {
				Msg.error(channel, "Weder URL angegeben noch laufender Track", "&addFavourite");
			}
		}
		
		else {
			Msg.ussage(channel, "<shortcut> (<Youtube-URL>)", "&addFavourite");
		}
	}
	
	public static void pause(Guild guild, TextChannel channel) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guild.getIdLong());
		
		boolean newstate = !controller.getPlayer().isPaused();
		controller.getPlayer().setPaused(newstate);
		
		Msg.info(channel, "Pause wurde auf '" + newstate + "' gesetzt", 5);
	}
	
	public static void skip(Guild guild, TextChannel channel) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guild.getIdLong());
		
		controller.getPlayer().stopTrack();
		
		Msg.info(channel, "Song übersprungen", 5);
	}
	
	public static void gotoIndex(Guild guild, TextChannel channel, int newindex) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guild.getIdLong());
		
		if(newindex >= 1 && newindex <= controller.getQueue().getQueuelist().size()) {
			controller.getQueue().setPos(newindex - 1);
			controller.getPlayer().stopTrack();
			
			Msg.info(channel, "Erfolgreich zu " + newindex + " gesprungen", 5);
		}
		
		else {
			Msg.error(channel, "der angegebene Index ist ungültig", "&goto");
		}
	}
	
	public static void loop(Guild guild, TextChannel channel) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guild.getIdLong());
		
		boolean newstate = !controller.getQueue().getLoop();
		controller.getQueue().setLoop(newstate);
		
		Msg.info(channel, "Loop wurde auf '" + newstate + "' gesetzt", 5);
	}
	
	public static void stop(Guild guild, TextChannel channel) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guild.getIdLong());
		AudioManager manager = guild.getAudioManager();
		
		
		controller.getQueue().delQueuelistmessage();
		controller.setQueue(new Queue(controller));
		
		controller.getPlayer().stopTrack();
		manager.closeAudioConnection();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(controller.trackinfo != null) {
			controller.trackinfo.delete().queue();
			controller.trackinfo = null;
		}
		
		Msg.info(channel, "Playlist gestopt und geleert", 5);
	}
	
	public static void delete(Guild guild, TextChannel channel, int index) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(guild.getIdLong());
		
		if(index >= 1 && index <= controller.getQueue().getQueuelist().size()) {
			controller.getQueue().getQueuelist().remove(index - 1);
			
			controller.getQueue().printQueue(controller.channel);
			
			if(index == controller.getQueue().getPos()) {
				controller.getQueue().setPos(controller.getQueue().getPos() - 1);
				
				controller.getPlayer().stopTrack();
			}
			
			Msg.info(channel, "Position **" + index + "** erfolgreich aus der Playlist entfernt", 5);
		}
		
		else {
			Msg.error(channel, "der angegebene Index ist ungültig", "&delete");
		}
	}
}
