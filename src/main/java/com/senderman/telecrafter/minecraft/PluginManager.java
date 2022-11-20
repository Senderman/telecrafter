package com.senderman.telecrafter.minecraft;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PluginManager {

    private final MinecraftProvider minecraft;

    public PluginManager(MinecraftProvider minecraft) {
        this.minecraft = minecraft;
    }

    @Nullable
    public Plugin getPlugin(String pluginName) {
        return minecraft.getPluginManager().getPlugin(pluginName);
    }

    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        return minecraft.getPluginLoader().getPluginDescription(file);
    }

    public void installPluginFile(File pluginFile) throws IOException {
        var pluginDir = minecraft.getPluginsDirectory();
        var newFile = new File(pluginDir, pluginFile.getName());
        Files.move(Path.of(pluginFile.toURI()), Path.of(newFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
    }

    public void deletePluginFile(JavaPlugin plugin) {
        var oldFile = getPluginFile(plugin);
        oldFile.delete();
    }

    private File getPluginFile(JavaPlugin plugin) {
        try {
            var getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            return (File) getFileMethod.invoke(plugin);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
