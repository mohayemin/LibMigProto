package ca.ualberta.libmigproto.config;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public LibConfig[] libs;
    public String dataRoot;

    public static Config load() throws IOException {
        var configPath = Path.of("config.json");
        var instance = new Gson().fromJson(Files.readString(configPath), Config.class);
        for (var lib : instance.libs) {
            lib.mainConfig = instance;
            for (var client : lib.clients) {
                client.libConfig = lib;
            }
        }
        return instance;
    }

}
