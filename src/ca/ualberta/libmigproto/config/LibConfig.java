package ca.ualberta.libmigproto.config;

import java.nio.file.Path;

public class LibConfig {
    public String name;
    public String gitUrl;
    public String sourceRoot;
    public String rootPackage;
    public ClientConfig[] clients;
    public Config mainConfig;

    public Path getSourceRootPath() {
        return getDataRootPath().resolve(Path.of("lib", sourceRoot));
    }

    public Path getDataRootPath() {
        return Path.of(mainConfig.dataRoot, name).toAbsolutePath().normalize();
    }
}
