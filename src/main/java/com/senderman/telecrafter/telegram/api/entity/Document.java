package com.senderman.telecrafter.telegram.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {

    @JsonProperty("file_id")
    private String fileId;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("mime_type")
    private String mimeType;

    public String getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isJar() {
        return mimeType.equals("application/x-java-archive");
    }
}
