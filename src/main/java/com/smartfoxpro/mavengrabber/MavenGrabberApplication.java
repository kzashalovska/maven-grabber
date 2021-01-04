package com.smartfoxpro.mavengrabber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.smartfoxpro.mavengrabber.beans.Library;
import com.smartfoxpro.mavengrabber.services.MavenGrabberService;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class MavenGrabberApplication implements CommandLineRunner {

    private static final String ARTIFACT_ID_OPTION = "a";
    private static final String GROUP_ID_OPTION = "g";
    private static final String ARTIFACT_ID_DESC = "artifactId";
    private static final String GROUP_ID_DESC = "groupId";
    private static final String RESPONSE = "response";
    private static final String DOCS = "docs";

    @Autowired
    MavenGrabberService mavenGrabberService;

    private Logger logger = LoggerFactory.getLogger(MavenGrabberApplication.class);

    @Override
    public void run(String... args) throws Exception {
        CommandLine line = getCommandLine(args);
        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        String mavenData = "";

        if (line.hasOption(ARTIFACT_ID_OPTION)) {
            mavenData = mavenGrabberService
                    .searchMavenData(ARTIFACT_ID_OPTION, line.getOptionValue(ARTIFACT_ID_OPTION));
        }
        else if (line.hasOption(GROUP_ID_OPTION)) {
            mavenData = mavenGrabberService
                    .searchMavenData(GROUP_ID_OPTION, line.getOptionValue(GROUP_ID_OPTION));
        }

        ArrayNode responseArray = (ArrayNode)objectMapper.readTree(mavenData).path(RESPONSE).get(DOCS);
        List<Library> libraries = objectMapper
                .readValue(responseArray.toString(), new TypeReference<List<Library>>() {});

        logger.info(gson.toJson(libraries));
    }

    private CommandLine getCommandLine(String[] args) throws ParseException {
        Option artifactId = Option.builder(ARTIFACT_ID_OPTION).required(false).hasArg(true)
                .longOpt(ARTIFACT_ID_DESC).build();
        Option groupId = Option.builder(GROUP_ID_OPTION).required(false).hasArg(true)
                .longOpt(GROUP_ID_DESC).build();

        Options options = new Options();

        options.addOption(artifactId);
        options.addOption(groupId);

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    public static void main(String[] args) {

            SpringApplication.run(MavenGrabberApplication.class, args);
    }
}
