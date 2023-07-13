package com.senderman.telecrafter.config;

import com.senderman.telecrafter.telegram.command.alias.Alias;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ConfigLoader {

    public static Config load(FileConfiguration config) throws IOException {

        return new ConfigImpl(
                config.getString("botToken"),
                config.getString("botName"),
                config.getLong("chatId"),
                config.getBoolean("allowForeignChats"),
                new HashSet<>(config.getLongList("admins")),
                new HashSet<>(config.getLongList("ignoredUsers")),
                new HashSet<>(config.getStringList("forceAdminCommands")),
                getAliases(config)
        );
    }

    private static Map<String, Alias> getAliases(FileConfiguration config) {
        var aliasesSection = config.getConfigurationSection("aliases");
        var result = new HashMap<String, Alias>();
        for (var key : aliasesSection.getKeys(false)) {
            var alias = aliasesSection.getConfigurationSection(key);
            result.put(key, new Alias(
                    alias.getString("command"),
                    alias.getString("description"),
                    alias.getBoolean("adminOnly")
            ));
        }
        return result;
    }

}
