package de.mineking.commands;

import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicUtil;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class GotoCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			if(args.length == 2) {
				MusicUtil.gotoIndex(channel.getGuild(), channel, Integer.parseInt(args[1]));
			}
			
			else {
				Msg.ussage(channel, "<index>", message);
			}
		}
		
		else {
			Msg.unlicensed(channel, "DJ", message);
		}
	}
}
