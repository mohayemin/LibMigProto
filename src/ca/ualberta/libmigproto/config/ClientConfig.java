package ca.ualberta.libmigproto.config;

import java.nio.file.Path;

public class ClientConfig extends RepositoryConfig{
    public boolean skip;
    public LibConfig libConfig;

    @Override
    public Path getRepositoryPath() {
        return libConfig.getDataRootPath().resolve(Path.of("clients", getRepositoryName()));
    }
}

