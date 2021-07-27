package ca.ualberta.libmigproto.config;

import java.nio.file.Path;

public class LibConfig extends RepositoryConfig {
    public String rootPackage;
    public boolean skip;
    public ClientConfig[] clients;
    public Config mainConfig;

    @Override
    public Path getRepositoryPath() {
        return getDataRootPath().resolve("lib");
    }

    public Path getDataRootPath() {
        var parts = gitUrl.split("/");
        var name = parts[parts.length - 1];
        return Path.of(mainConfig.dataRoot, name).toAbsolutePath().normalize();
    }
}
