package ca.ualberta.libmigproto.config;

import java.nio.file.Path;

public class ClientConfig {
    public String name;
    public String sourceRoot;
    public String gitUrl;
    public LibConfig libConfig;

    public Path getSourceRootPath() {
        return libConfig.getDataRootPath().resolve(Path.of("clients", name, sourceRoot));
    }
}
