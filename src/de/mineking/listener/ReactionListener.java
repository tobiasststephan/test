package de.mineking.listener;

import de.mineking.Colors;
import de.mineking.MineKingBot;
import de.mineking.music.MusicController;
import de.mineking.music.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {
	@Override //⏹  ⏯ ⏭ 🔁 ⭐
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		MusicController controller = MineKingBot.INSTANCE.playerManager.getController(event.getGuild().getIdLong());
		
		Member m = event.getMember();
		Guild guild = event.getGuild();
		
		if(controller.trackinfo != null && event.getMessageIdLong() == controller.trackinfo.getIdLong() && !event.getUser().isBot()) {			
			Role dj = MineKingBot.INSTANCE.playerManager.getController(event.getGuild().getIdLong()).dj;
			
			if(dj == null || m.getRoles().contains(dj)) {
				switch(event.getReactionEmote().getEmoji()) {
					case "⏹": MusicUtil.stop(guild, null); break;
					case "⏯": MusicUtil.pause(guild, null); event.getReaction().removeReaction(event.getUser()).queue(); break;
					case "⏭": MusicUtil.skip(guild, null); break;
					case "🔁": MusicUtil.loop(guild, event.getTextChannel()); event.getReaction().removeReaction(event.getUser()).queue(); break;
					case "❌": MusicUtil.delete(guild, event.getTextChannel(), controller.getQueue().getPos());
						if(event.getTextChannel().getHistory().getMessageById(event.getMessageIdLong()) != null) {
							event.getReaction().removeReaction(event.getUser()).queue();
						}
						 
						break;
					case "⭐": 
						m.getUser().openPrivateChannel().queue((ch) -> {
							event.getReaction().removeReaction(event.getUser()).queue();
							
							String url = controller.getQueue().getQueuelist().get(controller.getQueue().getPos() - 1);
							
							EmbedBuilder builder = new EmbedBuilder();
							
							builder.setColor(Colors.std);
							builder.setTitle("Jetzt läuft:");
							builder.setDescription(url);
							builder.appendDescription(MusicUtil.getTitle(url));
							builder.setImage(MusicUtil.getThumbnailUrl(url));
							
							ch.sendMessage(builder.build()).queue();
						});
						break;
					
				}
			}
			
			else {
				event.getReaction().removeReaction(event.getUser()).queue();
			}
		}
	}
}
