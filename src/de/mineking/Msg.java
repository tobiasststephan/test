package de.mineking;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Msg {
	public final static int stdduration = 5;
	
	public static void send(TextChannel channel, String text, Color color, int duration) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(color);
		 builder.setDescription(text);
		 
		 if(channel != null) {
			 if(duration == -1) {
				 channel.sendMessage(builder.build()).queue();
			 }
			 
			 else {
				 channel.sendMessage(builder.build()).complete().delete().queueAfter(duration, TimeUnit.SECONDS);
			 }
		 }
	}
	
	public static void unlicensed(TextChannel channel, String needed, Message message) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.unlicensed);
		 builder.setDescription(message.getContentDisplay().split(" ")[0].substring(1) + ": Du musst " + needed + " sein, um diesen befehl auszuführen");
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(stdduration, TimeUnit.SECONDS);
		 }
	}
	
	public static void ussage(TextChannel channel, String ussage, Message message) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.ussage);
		 builder.setDescription(message.getContentDisplay().split(" ")[0].substring(1) + ": Falsche Benutzung, benutze " + message.getContentDisplay().split(" ")[0] + " " + ussage);
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(stdduration, TimeUnit.SECONDS);
		 }
	}
	
	public static void ussage(TextChannel channel, String ussage, String command) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.ussage);
		 builder.setDescription(command + ": Falsche Benutzung, benutze " + command + " " + ussage);
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(stdduration, TimeUnit.SECONDS);
		 }
	}
	
	public static void info(TextChannel channel, String text, int duration) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.info);
		 builder.setDescription(text);
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(duration, TimeUnit.SECONDS);
		 }
	}
	
	public static void info(TextChannel channel, String text, Message message, int duration) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.info);
		 builder.setDescription(message.getContentDisplay().split(" ")[0].substring(1) + ": " + text);
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(duration, TimeUnit.SECONDS);
		 }
	}
	
	public static void error(TextChannel channel, String text, Message message) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.error);
		 builder.setDescription(message.getContentDisplay().split(" ")[0].substring(1) + ": " + text);
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(stdduration, TimeUnit.SECONDS);
		 }
	}
	
	public static void error(TextChannel channel, String text, String command) {
		 EmbedBuilder builder = new EmbedBuilder();
		 
		 builder.setColor(Colors.error);
		 builder.setDescription(command + ": " + text);
		 
		 if(channel != null) {
			 channel.sendMessage(builder.build()).complete().delete().queueAfter(stdduration, TimeUnit.SECONDS);
		 }
	}
}
