import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DataManager {

    private static final Properties PROPERTIES = new Properties();

    private static final String ENV = ConfigurationManager.getInstance().getProperty("env");

    private DataManager() throws IOException {
        PROPERTIES.load(getInputStream("env-test-data.properties"));
    }

    private static DataManager manager;

    public static DataManager getInstance() {

        if (manager == null) {
            synchronized (ConfigurationManager.class) {
                if (manager == null) {
                    try {
                        manager = new DataManager();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return manager;
    }

    public String getString(String name) {
        String key = ENV + "." + name;
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    private InputStream getInputStream(String file) {

        try {
            List<URL> urls = Collections.list(Thread.currentThread().getContextClassLoader().getResources(file));
            return urls == null || urls.isEmpty() ? null : urls.get(0).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
