package de.mineking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.mineking.listener.*;
import de.mineking.music.PlayerManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class MineKingBot {
	//permission int 3402816
	private static String version = "10.1";
	
	public static MineKingBot INSTANCE;
	
	private CommandManager cmdMan;
	public ShardManager shardMan;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	
	private static String timestarted;
	
	public static void main(String[] args) {
		try {
			new MineKingBot();
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public MineKingBot() throws LoginException, IllegalArgumentException {
		
		INSTANCE = this;
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. YYYY,  HH:mm:ss");
		
		timestarted = sdf.format(cal.getTime());
		
		LiteSQL.connect();
		SQLManager.onCreate();
		
		DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
		builder.setToken("NzMyMTgwMDMyMDA0MjkyNjM4.Xww1yg.SIVri91fIy04yh0mCOtn_Ia6rG0");
		
		
		builder.setStatus(OnlineStatus.ONLINE);
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		
		this.cmdMan = new CommandManager();
		
		builder.addEventListeners(new CommandListener());
		builder.addEventListeners(new ReactionListener());
		
		builder.setActivity(Activity.listening("&help"));
		
		this.shardMan = builder.build();

		System.out.println("");	
		System.out.println("  *******************************************************");
		System.out.println("  * MineKingBot (Musik)             von Tobias Winkler  *");
		System.out.println("  * Version " + version + "                                        *");
		System.out.println("  *                                                     *");
		System.out.println("  * Funktionen:                                         *");
		System.out.println("  * -Musik                                              *");
		System.out.println("  *                                                     *");
		System.out.println("  * Bot ist jezt online                                 *");
		System.out.println("  * Zum Stoppen 'exit' eingeben                         *");
		System.out.println("  *                                                     *");
		System.out.println("  *******************************************************");
		System.out.println("");		
		
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		consolecommands();
	}
	
	public void consolecommands() {	
		new Thread(() -> {
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while((line = reader.readLine()) != null) {
					if(!cmdMan.execute(line)) {
						System.out.println("Unbekanntes Commando");
					}
				}
			}catch (IOException e) { }
			
		}).start();
	}
	
	public CommandManager getCmdMan() {
		return cmdMan;
	}
	
	public static String getVersion() {
		return version;
	}
	
	public static String getTimeStarted() {
		return timestarted;
	}
}
