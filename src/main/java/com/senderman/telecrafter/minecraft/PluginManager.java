package com.senderman.telecrafter.minecraft;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginManager {

    private final JavaPlugin telecrafterPlugin;
    private final File pluginDirectory;
    private final org.bukkit.plugin.PluginManager pluginManager;
    private final BukkitScheduler scheduler;
    private final ObjectMapper objectMapper;
    private final Set<Plugin> lostPlugins = new HashSet<>();


    public PluginManager(JavaPlugin telecrafterPlugin, ObjectMapper objectMapper) {
        this.telecrafterPlugin = telecrafterPlugin;
        this.pluginDirectory = telecrafterPlugin.getDataFolder().getParentFile();
        this.scheduler = telecrafterPlugin.getServer().getScheduler();
        this.pluginManager = telecrafterPlugin.getServer().getPluginManager();
        this.objectMapper = objectMapper;
    }

    public String listPlugins() {
        Plugin[] plugins = pluginManager.getPlugins();
        return Stream.of(plugins)
                .filter(p -> !lostPlugins.contains(p))
                .map(p -> getPluginStatus(p) + p.getName() + " v" + p.getDescription().getVersion())
                .collect(Collectors.joining("\n"));
    }

    public boolean enablePlugin(String pluginName) {
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) return false;
        if (plugin.isEnabled()) return false;
        scheduler.scheduleSyncDelayedTask(telecrafterPlugin, () ->
                pluginManager.enablePlugin(plugin));
        return true;
    }

    public boolean disablePlugin(String pluginName) {
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) return false;
        if (!plugin.isEnabled()) return false;
        scheduler.scheduleSyncDelayedTask(telecrafterPlugin, () ->
                pluginManager.disablePlugin(plugin));

        while (plugin.isEnabled()) { // wait for plugin disabling
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean deletePlugin(String pluginName) {
        JavaPlugin plugin = (JavaPlugin) pluginManager.getPlugin(pluginName);
        if (plugin == null) return false;
        disablePlugin(pluginName);

        try {
            Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
            getFile.setAccessible(true);
            File pluginFile = (File) getFile.invoke(plugin);
            pluginFile.delete();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return false;
        }

        lostPlugins.add(plugin);
        return true;
    }

    public boolean installPlugin(File pluginJar) throws IOException {
        String pluginName = getPluginName(pluginJar);
        if (pluginName == null) return false;

        Plugin oldPlugin = pluginManager.getPlugin(pluginName);
        if (oldPlugin != null) deletePlugin(pluginName);
        File copied = new File(pluginDirectory, pluginJar.getName());
        FileUtils.copyFile(pluginJar, copied);
        try {
            Plugin newPlugin = pluginManager.loadPlugin(copied);
            if (newPlugin == null) {
                return false;
            } else {
                enablePlugin(newPlugin.getName());
                return true;
            }
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    private String getPluginName(File pluginJar) throws IOException {
        ZipFile zipFile = new ZipFile(pluginJar);
        ZipEntry zipEntry = zipFile.getEntry("plugin.yml");
        if (zipEntry == null) return null;
        File pluginYml = new File(pluginJar.getName() + "-plugin.yml");
        try (
                FileOutputStream out = new FileOutputStream(pluginYml);
                InputStream in = zipFile.getInputStream(zipEntry)
        ) {
            int length;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) != -1)
                out.write(buffer, 0, length);
        }

        try {
            PluginData pluginData = objectMapper.readValue(pluginYml, PluginData.class);
            return pluginData.getName();
        } catch (JsonParseException | JsonMappingException e) {
            return null;
        } finally {
            pluginYml.delete();
        }
    }

    private String getPluginStatus(Plugin plugin) {
        return plugin.isEnabled() ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ";
    }
}
