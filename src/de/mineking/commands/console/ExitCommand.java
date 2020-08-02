package de.mineking.commands.console;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.music.MusicUtil;
import de.mineking.types.ConsoleCommand;
import net.dv8tion.jda.api.entities.Guild;

public class ExitCommand implements ConsoleCommand {
	@Override
	public void performCommand(String command) {
		for(Guild guild : MineKingBot.INSTANCE.shardMan.getGuilds()) {
			MusicUtil.stop(guild, null);
		}
		
		
		LiteSQL.disconnect();
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. YYYY,  HH:mm:ss");
		
		System.out.println("");	
		System.out.println("  *******************************************************");
		System.out.println("  * MineKingBot       von Tobias Winkler      MineKing  *");
		System.out.println("  * Version " + MineKingBot.getVersion() + "                                        *");
		System.out.println("  *                                                     *");
		System.out.println("  * Gestartet: " + MineKingBot.getTimeStarted() + "                  *");
		System.out.println("  * Beendet  : " + sdf.format(cal.getTime()) + "                  *");
		System.out.println("  *                                                     *");
		System.out.println("  * Bot ist jezt offline                                *");
		System.out.println("  *                                                     *");
		System.out.println("  *******************************************************");
		System.out.println("");
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.exit(0);
	}
}
