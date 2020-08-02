package de.mineking.listener;

import de.mineking.Colors;
import de.mineking.MineKingBot;
import de.mineking.Msg;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			
			if(message.startsWith("&")) {				
				String[] args = message.substring(1).split(" ");
				
				if(args.length > 0) {
					if(!MineKingBot.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage())) {
						event.getMessage().delete().queue();
						Msg.send(channel, args[0] + ": Commando nicht gefunden", Colors.missingcmd, 5);
					}
				}
			}	
		}
	}
}