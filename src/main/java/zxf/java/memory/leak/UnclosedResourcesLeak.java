package zxf.java.memory.leak;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;

@Slf4j
public class UnclosedResourcesLeak {
    private static Integer count = 0;

    public Integer test() throws IOException {
        for (int i = 0; i < 1000000000; i++) {
            testUnclosedInputStream();
        }
        count = count + 1000000000;
        return count;
    }

    private void testUnclosedInputStream() throws IOException {
        URI uri = URI.create("http://www.baidu.com");
        URLConnection urlConnection = uri.toURL().openConnection();
        InputStream is = urlConnection.getInputStream();
    }

    public Integer testRestTemplate() throws Exception {
        for (int i = 0; i < 1000000000; i++) {
            int len = testUnclosedHttpComponentsClientHttpRequestFactory();
            log.info("{}:######{}", i, len);
        }
        count = count + 1000000000;
        return count;
    }

    private int testUnclosedHttpComponentsClientHttpRequestFactory() throws IOException {
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
        connManager.setSocketConfig(SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(10000))
                .build());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String value = restTemplate.getForObject("http://localhost:8089/api/perf-test", String.class);
        log.info(value);
        return value.length();
    }
}
