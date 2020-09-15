package com.senderman.telecrafter.minecraft;

import com.google.common.io.Files;
import com.google.inject.Inject;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

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
    private final ServerStopDelayer serverStopDelayer;
    private final BukkitScheduler scheduler;
    private final Set<Plugin> lostPlugins = new HashSet<>();


    @Inject
    public PluginManager(Plugin mainPlugin, ServerStopDelayer serverStopDelayer) {
        this.mainPlugin = mainPlugin;
        this.serverStopDelayer = serverStopDelayer;
        this.pluginManager = mainPlugin.getServer().getPluginManager();
        this.pluginDirectory = this.mainPlugin.getDataFolder().getParentFile();
        this.scheduler = this.mainPlugin.getServer().getScheduler();
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

    public void installPlugin(File pluginJar) throws IOException, InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        String pluginName = getPluginName(pluginJar);
        Plugin oldPlugin = pluginManager.getPlugin(pluginName);
        if (oldPlugin != null)
            deletePlugin(pluginName, false);

        File copied = new File(pluginDirectory, pluginJar.getName());
        Files.copy(pluginJar, copied);
        if (oldPlugin != null)
            serverStopDelayer.scheduleServerStop("reload");
        else {
            try {
                Plugin newPlugin = pluginManager.loadPlugin(copied);
                scheduler.scheduleSyncDelayedTask(mainPlugin,
                        () -> pluginManager.enablePlugin(Objects.requireNonNull(newPlugin))
                );
            } catch (InvalidPluginException | InvalidDescriptionException | UnknownDependencyException e) {
                copied.delete();
                throw e;
            }
        }
    }

    private String getPluginName(File pluginJar) throws IOException, InvalidPluginException, InvalidDescriptionException {
        ZipFile zipFile = new ZipFile(pluginJar);
        ZipEntry zipEntry = zipFile.getEntry("plugin.yml");
        if (zipEntry == null) throw new InvalidPluginException("No plugin.yml in the file!");

        try (InputStream in = zipFile.getInputStream(zipEntry)) {
            return new PluginDescriptionFile(in).getName();
        }
    }

    private String getPluginStatus(Plugin plugin) {
        return plugin.isEnabled() ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ";
    }
}
