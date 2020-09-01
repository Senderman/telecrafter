package com.senderman.telecrafter.minecraft;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginManager {

    private final Plugin mainPlugin;
    private final File pluginDirectory;
    private final org.bukkit.plugin.PluginManager pluginManager;
    private final BukkitScheduler scheduler;
    private final Set<Plugin> lostPlugins = new HashSet<>();


    @Inject
    public PluginManager(JavaPlugin telecrafterPlugin) {
        this.pluginManager = telecrafterPlugin.getServer().getPluginManager();
        this.mainPlugin = telecrafterPlugin;
        this.pluginDirectory = mainPlugin.getDataFolder().getParentFile();
        this.scheduler = mainPlugin.getServer().getScheduler();
    }

    public String listPlugins() {
        Plugin[] plugins = pluginManager.getPlugins();
        return Stream.of(plugins)
                .filter(p -> !lostPlugins.contains(p))
                .map(p -> getPluginStatus(p) + p.getName() + " v" + p.getDescription().getVersion())
                .collect(Collectors.joining("\n"));
    }

    // Fires EnablePluginEvent, without checking result
    public boolean enablePlugin(String pluginName) {
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) return false;
        if (plugin.isEnabled()) return false;
        scheduler.scheduleSyncDelayedTask(mainPlugin, () ->
                pluginManager.enablePlugin(plugin));
        return true;
    }

    // Fires DisablePluginEvent, without checking result
    public boolean disablePlugin(String pluginName) {
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) return false;
        if (!plugin.isEnabled()) return false;
        scheduler.scheduleSyncDelayedTask(mainPlugin, () ->
                pluginManager.disablePlugin(plugin));

        return true;
    }

    public boolean deletePlugin(String pluginName) {
        return deletePlugin(pluginName, true);
    }

    public boolean deletePlugin(String pluginName, boolean disablePlugin) {
        JavaPlugin plugin = (JavaPlugin) pluginManager.getPlugin(pluginName);

        if (plugin == null) return false;
        try {
            Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
            getFile.setAccessible(true);
            File pluginFile = (File) getFile.invoke(plugin);
            pluginFile.delete();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return false;
        }

        if (disablePlugin)
            disablePlugin(pluginName);
        lostPlugins.add(plugin);
        return true;
    }

    // if plugin has to be replaced, reload server
    public boolean installPlugin(File pluginJar) throws IOException {
        String pluginName = getPluginName(pluginJar);
        if (pluginName == null) return false;

        Plugin oldPlugin = pluginManager.getPlugin(pluginName);
        if (oldPlugin != null)
            deletePlugin(pluginName, false);

        File copied = new File(pluginDirectory, pluginJar.getName());
        FileUtils.copyFile(pluginJar, copied);
        if (oldPlugin != null)
            scheduler.scheduleSyncDelayedTask(mainPlugin,
                    () -> mainPlugin.getServer().dispatchCommand(mainPlugin.getServer().getConsoleSender(), "reload"));
        else {
            try {
                Plugin newPlugin = pluginManager.loadPlugin(copied);
                scheduler.scheduleSyncDelayedTask(mainPlugin,
                        () -> pluginManager.enablePlugin(Objects.requireNonNull(newPlugin))
                );
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Nullable
    private String getPluginName(File pluginJar) throws IOException {
        ZipFile zipFile = new ZipFile(pluginJar);
        ZipEntry zipEntry = zipFile.getEntry("plugin.yml");
        if (zipEntry == null) return null;
        try (InputStream in = zipFile.getInputStream(zipEntry)) {
            return new PluginDescriptionFile(in).getName();
        } catch (InvalidDescriptionException e) {
            return null;
        }
    }

    private String getPluginStatus(Plugin plugin) {
        return plugin.isEnabled() ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ";
    }
}
