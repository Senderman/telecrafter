package com.senderman.telecrafter.minecraft;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PluginManager {

    private final File pluginDirectory;

    public PluginManager(File pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    public String listPlugins() {
        File[] plugins = pluginDirectory.listFiles(file -> file.getName().endsWith(".jar"));
        if (plugins == null) {
            return "";
        }
        return Stream.of(plugins).map(File::getName).collect(Collectors.joining("\n"));
    }

    public boolean deletePlugin(String fileName) {
        File pluginToRemove = new File(pluginDirectory, fileName);
        if (!pluginToRemove.exists()) return false;
        return pluginToRemove.delete();
    }

    public void installPlugin(File pluginJar) throws IOException {
        File copied = new File(pluginDirectory, pluginJar.getName());
        FileUtils.copyFile(pluginJar, copied);
    }
}
