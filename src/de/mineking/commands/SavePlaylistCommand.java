package de.mineking.commands;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicController;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class SavePlaylistCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			if(args.length == 2) {
				String urls = "";
				
				MusicController controller = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
				
				for(String url : controller.getQueue().getQueuelist()) {
					urls += url + " ";
				}
				
				LiteSQL.onUpdate("DELETE FROM pls WHERE shortcut = '" + args[1] + "' AND guildid = " + channel.getGuild().getIdLong());
				LiteSQL.onUpdate("INSERT INTO pls(guildid, urls, shortcut) VALUES(" + channel.getGuild().getIdLong() + ", '" + urls + "', '" + args[1] + "')");
				Msg.info(channel, "Playlist unter **" + args[1] + "** gespeichert", message, 5);
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
