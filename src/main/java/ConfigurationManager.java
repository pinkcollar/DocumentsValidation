import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

    ConfigurationManager() throws IOException {
        PROPERTIES.load(ConfigurationManager.class.getResourceAsStream("default-config.properties"));
        PROPERTIES.load(ConfigurationManager.class.getResourceAsStream("env-specific-config.properties"));
    }

    ConfigurationManager(String env) throws IOException {
        PROPERTIES.load(ConfigurationManager.class.getResourceAsStream("default-config.properties"));
        PROPERTIES.load(ConfigurationManager.class.getResourceAsStream(env + "-specific-config.properties"));
    }
    private static ConfigurationManager manager;

    private static final Properties PROPERTIES = new Properties();

    public static ConfigurationManager getInstance() {

        if (manager == null) {
            synchronized (ConfigurationManager.class) {
                if (manager == null) {
                    try {
                        manager = new ConfigurationManager();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return manager;
    }

    public static ConfigurationManager getInstance(String env) {

        if (manager == null) {
            synchronized (ConfigurationManager.class) {
                if (manager == null) {
                    try {
                        manager = new ConfigurationManager(env);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return manager;
    }



    public String getProperty(String name) {
        return System.getProperty(name, PROPERTIES.getProperty(name));
    }
}