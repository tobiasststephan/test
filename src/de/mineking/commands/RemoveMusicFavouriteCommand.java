package de.mineking.commands;

import de.mineking.LiteSQL;
import de.mineking.Msg;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveMusicFavouriteCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		if(args.length == 2) {
			LiteSQL.onUpdate("DELETE FROM mfs WHERE shortcut = '" + args[1] + "' AND memberid = " + m.getIdLong());
			Msg.info(channel, m.getAsMention() + ", Dein Musik Favourit unter " + args[1] + " wurde gelöscht", message, 5);
		}
		
		else {
			Msg.ussage(channel, "<shortcut>", message);
		}
	}
}
