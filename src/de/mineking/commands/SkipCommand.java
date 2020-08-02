package de.mineking.commands;

import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicUtil;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			MusicUtil.skip(channel.getGuild(), channel);
		}
		
		else {
			Msg.unlicensed(channel, "DJ", message);
		}
	}
}
