package de.mineking.commands;

import java.util.concurrent.TimeUnit;

import de.mineking.Colors;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicController;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetPosCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
		
		if(controller.getPlayer().getPlayingTrack() != null) {
			long sec = controller.getPlayer().getPlayingTrack().getPosition() / 1000;
			long min = sec / 60;
			long h = min / 60;
			
			sec %= 60;
			min %= 60;
			
			EmbedBuilder builder = new EmbedBuilder();
			
			builder.setColor(Colors.info);
			builder.setDescription((controller.getPlayer().getPlayingTrack().getInfo().isStream ? ":red_circle: Stream" : (h > 0 ? h + "h " : "") + min + "min " + sec + "sec"));
			
			controller.channel.sendMessage(builder.build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
		}
		
		else {
			Msg.error(channel, "Kein aufender Track", message);
		}
	}
}
