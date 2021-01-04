package com.smartfoxpro.mavengrabber.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Library {

    private static final String ARTIFACT_ID = "a";
    private static final String GROUP_ID = "g";

    @JsonProperty(ARTIFACT_ID)
    private String artifactId;
    @JsonProperty(GROUP_ID)
    private String groupId;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Library{" +
                "artifactId='" + artifactId + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
