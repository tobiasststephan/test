package de.mineking.commands;

import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ServerCommand{
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setColor(m.getColor());
		builder.setTitle("**Bot Hilfe**");		
		
		builder.appendDescription("Bot-Prefix: '&'\n");
		builder.appendDescription("\n");
		builder.appendDescription("**Alle**\n");
		builder.appendDescription("addFavourite <shortcut> (<url>)\n");
		builder.appendDescription("getFavourites\n");
		builder.appendDescription("removeFavourite <shortcut>\n");
		builder.appendDescription("getPlaylists\n");
		builder.appendDescription("getPos\n");
		builder.appendDescription("**DJ**\n");
		builder.appendDescription("savePlaylist <shortcut>\n");
		builder.appendDescription("removePlaylist <shortcut>\n");
		builder.appendDescription("delete <index>\n");
		builder.appendDescription("goto <index>\n");
		builder.appendDescription("load <shortcut>\n");
		builder.appendDescription("loop\n");
		builder.appendDescription("pause\n");
		builder.appendDescription("play <youtube-url/shortcut>\n");
		builder.appendDescription("skip\n");
		builder.appendDescription("stop\n");
		builder.appendDescription("volume <volume>\n");
		builder.appendDescription("**Admin**\n");
		builder.appendDescription("setDJ <@rolle>\n");
		builder.appendDescription("removeDJ\n");
		builder.appendDescription("setMusicChannel <#channel>\n");
		builder.appendDescription("removeMusicChannel\n");
		builder.appendDescription("\n");
		
		m.getUser().openPrivateChannel().queue((ch) -> {
			ch.sendMessage(builder.build()).queue();
		});
	}
}
