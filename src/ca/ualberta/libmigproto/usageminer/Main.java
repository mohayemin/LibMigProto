package ca.ualberta.libmigproto.usageminer;


import ca.ualberta.libmigproto.config.Config;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, GitAPIException {
        var config = Config.load();

        for (var lib : config.libs) {
            if (lib.skip) continue;
            for (var client : lib.clients) {
                if (client.skip) continue;
                new UsageMiner(client)
                        .mine();
            }
        }
    }
}
