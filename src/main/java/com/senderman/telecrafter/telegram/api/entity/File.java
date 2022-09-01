package com.senderman.telecrafter.telegram.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class File {

    @JsonProperty("file_size")
    long fileSize;
    @JsonProperty("file_path")
    String filePath;
    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    public String getFileId() {
        return fileId;
    }

    public String getFileUniqueId() {
        return fileUniqueId;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFilePath() {
        return filePath;
    }
}
