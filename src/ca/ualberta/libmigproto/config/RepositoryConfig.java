package ca.ualberta.libmigproto.config;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.nio.file.Path;

public abstract class RepositoryConfig {
    public String sourceRoot;
    public String gitUrl;

    public abstract Path getRepositoryPath();

    public String getRepositoryName() {
        var parts = gitUrl.split("/");
        return parts[parts.length - 1];
    }

    public void cloneRepository() throws GitAPIException {
        var repoFile = getRepositoryPath().toFile();
        if (repoFile.exists())
            return;
        var x = Git.cloneRepository().setURI(gitUrl).setDirectory(repoFile).call();
    }

    public Path getSourceRootPath() {
        return getRepositoryPath().resolve(sourceRoot);
    }
}
