package de.mineking;

public class SQLManager {
	public static void onCreate() {
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS djs(guildid INTEGER, roleid INTEGER)");
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS mcs(guildid INTEGER, channelid INTEGER)");
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS mfs(memberid INTEGER, shortcut TEXT, url TEXT)");
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS pls(guildid INTEGER, shortcut TEXT, urls TEXT)");
	}
}
