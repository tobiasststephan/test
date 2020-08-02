package de.mineking.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import de.mineking.Colors;
import de.mineking.LiteSQL;
import de.mineking.music.MusicUtil;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetMusicFavouritesCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(m.getEffectiveName(), m.getUser().getAvatarUrl(),m.getUser().getEffectiveAvatarUrl());
		builder.setColor(Colors.info);
		
		boolean found = false;
		int time = 30;
		
		ResultSet set = LiteSQL.onQuery("SELECT * FROM mfs WHERE memberid = " + m.getIdLong());
		
		try {
			while(set.next()) {
				String shortcut = set.getString("shortcut");
				String url = set.getString("url");
				
				builder.appendDescription(shortcut + ":  **" + MusicUtil.getTitle(url) + "** (" + url + ") von **" + MusicUtil.getAuthor(url) + "**\n");
				found = true;
			}
		} catch (SQLException e) { }
		
		if(!found) {
			builder.setDescription("*Keine Favouriten*");
			time = 5;
		}
		
		channel.sendMessage(builder.build()).complete().delete().queueAfter(time, TimeUnit.SECONDS);
	}
}
