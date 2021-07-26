package ca.ualberta.libmigproto.usageminer;


import ca.ualberta.libmigproto.config.Config;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var config = Config.load();

        for (var lib : config.libs) {
            for (var client : lib.clients) {
                new UsageMiner(client).mine();
            }
        }
    }
}
