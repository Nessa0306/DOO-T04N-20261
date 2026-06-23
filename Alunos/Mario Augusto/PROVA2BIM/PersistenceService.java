package com.tvtracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvtracker.model.UserProfile;

import java.io.File;
import java.io.IOException;

public class PersistenceService {
    private static final String DIR  = System.getProperty("user.home")
                                       + File.separator + ".tvtracker";
    private static final String FILE = DIR + File.separator + "userdata.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public PersistenceService() { new File(DIR).mkdirs(); }

    public UserProfile load() {
        File f = new File(FILE);
        if (!f.exists()) return null;
        try {
            return mapper.readValue(f, UserProfile.class);
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return null;
        }
    }

    public void save(UserProfile profile) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(new File(FILE), profile);
    }
}