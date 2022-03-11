package com.senderman.telecrafter.config;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class ConfigLoader {

    public static Config load(File file) throws IOException {
        if (!file.exists()) {
            throw new NoSuchFileException(file.getAbsolutePath());
        }
        return new YAMLMapper().readValue(file, ConfigImpl.class);
    }

}
