package de.mineking.commands;

import de.mineking.music.MusicUtil;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class AddMusicFavouriteCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		MusicUtil.addFavourite(m, args, channel);
	}
}
