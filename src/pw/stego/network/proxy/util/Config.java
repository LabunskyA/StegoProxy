package pw.stego.network.proxy.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Dis is class to share configuration options
 * .conf file format is described in files if "conf" folder
 * Created by lina on 17.01.17.
 */
public class Config {
    private Map<String, String> options = new HashMap<>();

    /**
     * Creates new config from specified path
     * @param conf - path to .conf file
     * @throws IOException on reading failure
     */
    public Config(Path conf) throws IOException {
        String[] lines = new String(Files.readAllBytes(conf)).split("\n");
        for (String line : lines) {
            if (line.length() == 0 || line.charAt(0) == '#')
                continue;

            String[] tokens = line.replaceAll(" +", " ").split(" ");
            options.put(tokens[0], tokens[1]);
        }
    }

    public String getValue(String variable) {
        return options.get(variable);
    }

    public void setVariable(String variable, String value) {
        options.put(variable, value);
    }

    @Override
    public String toString() {
        return options.toString();
    }
}