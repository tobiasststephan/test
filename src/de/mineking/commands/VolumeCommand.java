package de.mineking.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.mineking.MineKingBot;
import de.mineking.Msg;
import de.mineking.music.MusicController;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements ServerCommand {
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		Role dj = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong()).dj;
		
		if(dj == null || m.getRoles().contains(dj)) {
			MusicController controller = MineKingBot.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
			AudioPlayer player = controller.getPlayer();
			
			player.setVolume(Integer.parseInt(args[1]));
			
			Msg.info(channel, "Lautstärke auf " + args[1] + " gesetzt", message, 5);
		}
		
		else {
			Msg.unlicensed(channel, "DJ", message);
		}
	}
}
