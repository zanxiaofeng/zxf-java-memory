package zxf.java.memory.leak;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UnclosedResourcesLeak {
    private static Integer count = 0;

    public Integer test() throws IOException {
        for (int i = 0; i < 1000000000; i++) {
            URL url = new URL("http://www.baidu.com");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
        }
        count = count + 1000000000;
        return count;
    }
}
