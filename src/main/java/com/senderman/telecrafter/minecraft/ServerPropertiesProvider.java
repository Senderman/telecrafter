package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Properties;

public class ServerPropertiesProvider {

    private final Properties properties;
    private final File propFile;

    @Inject
    public ServerPropertiesProvider(Plugin plugin) {
        properties = new Properties();
        String propertiesFileName = "server.properties";
        propFile = new File(plugin.getDataFolder().getParentFile().getParentFile(), propertiesFileName);
        try (InputStream input = new FileInputStream(propFile)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to find " + propFile.getAbsolutePath());
        }
    }

    /**
     * Get value of property by key
     *
     * @param key key of property
     * @return value of property, or null
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }


    /**
     * Change existing server property
     *
     * @param key   key of property to change
     * @param value value of property to change
     * @return true if property exists, else false
     */
    public boolean setProperty(String key, String value) {
        if (!properties.containsKey(key)) return false;
        properties.setProperty(key, value);
        try (OutputStream out = new FileOutputStream(propFile)) {
            properties.store(out, "Minecraft server properties\n");
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to " + propFile.getAbsolutePath());
        }
    }

}
