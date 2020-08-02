package de.mineking;

import java.util.concurrent.ConcurrentHashMap;

import de.mineking.commands.*;
import de.mineking.commands.console.*;
import de.mineking.types.ConsoleCommand;
import de.mineking.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {
	
	public ConcurrentHashMap<String, ServerCommand> commands;
	
	public ConcurrentHashMap<String, ConsoleCommand> consolecommands;

	public CommandManager() {
		commands = new ConcurrentHashMap<String, ServerCommand>();
		consolecommands = new ConcurrentHashMap<String, ConsoleCommand>();
		
		this.commands.put("help", new HelpCommand());
		
		this.commands.put("play", new PlayCommand());
		this.commands.put("load", new LoadPlaylistCommand());
		this.commands.put("stop", new StopCommand());
		this.commands.put("volume", new VolumeCommand());
		this.commands.put("pause", new PauseCommand());
		this.commands.put("loop", new LoopCommand());
		this.commands.put("skip", new SkipCommand());
		this.commands.put("goto", new GotoCommand());
		this.commands.put("delete", new DeleteTrackCommand());
		this.commands.put("getPos", new GetPosCommand());
		
		this.commands.put("setDJ", new SetDJCommand());
		this.commands.put("delDJ", new DeleteDJCommand());
		this.commands.put("setMusicChannel", new SetMusicChannelCommand());
		this.commands.put("delMusicChannel", new DeleteMusicChannelCommand());

		this.commands.put("addFavourite", new AddMusicFavouriteCommand());
		this.commands.put("removeFavourite", new RemoveMusicFavouriteCommand());
		this.commands.put("getFavourites", new GetMusicFavouritesCommand());
		
		this.commands.put("savePlaylist", new SavePlaylistCommand());
		this.commands.put("removePlaylist", new RemovePlaylistCommand());
		this.commands.put("getPlaylists", new GetPlaylistsCommand());
		
		
		this.consolecommands.put("exit", new ExitCommand());
	}
	
	public boolean perform(String command, Member m, TextChannel channel, Message message) {	
		ServerCommand cmd;

		if((cmd = this.commands.get(command)) != null) {
			cmd.performCommand(m, channel, message);
			
			return true;
		}
		
		return false;
	}
	
	public boolean execute(String command) {	
		ConsoleCommand cmd;

		if((cmd = this.consolecommands.get(command)) != null) {
			cmd.performCommand(command);
			
			return true;
		}
		
		return false;
	}
}