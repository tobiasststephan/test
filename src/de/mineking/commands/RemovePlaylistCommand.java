package de.mineking.commands;

import de.mineking.LiteSQL;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemovePlaylistCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			if(args.length == 2) {
				LiteSQL.onUpdate("DELETE FROM pls WHERE shortcut = '" + args[1] + "' AND guildid = " + channel.getGuild().getIdLong());
				Msg.info(channel, "Playlist unter **" + args[1] + "** wurde gelöscht", message, 5);
			}
			
			else {
				Msg.ussage(channel, "<shortcut>", message);
			}
		}
	}
}
