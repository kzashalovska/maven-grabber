package com.smartfoxpro.mavengrabber.services;

import com.smartfoxpro.mavengrabber.configuration.AsyncConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class MavenGrabberService {

    @Autowired
    AsyncConfiguration config;
    @Value("${app.sourceprovider}")
    private String sourceProvider;
    @Value("${app.pools.minConnectionsPerPool}")
    private int minConnections;
    private PoolingHttpClientConnectionManager cm;

    public String searchMavenData(String option, String value) throws Exception {

        return get(sourceProvider + "select?q=" + option + ":%22" + value + "%22+AND+p:%22jar%22&rows=20&wt=json");
    }

    private String get(String uri) throws Exception {
        cm = getPoolingHttpClientConnectionManager();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();

        HttpGet httpGet = new HttpGet(uri);

        if (cm.getTotalStats().getAvailable() > minConnections)
        {
            Future future = config.taskExecutor()
                    .submit((Callable<Object>) () -> executeConnection(httpClient, httpGet));
            return (String) future.get();
        }
        else return executeConnection(httpClient, httpGet);

    }

    private String executeConnection(CloseableHttpClient httpClient, HttpGet httpGet) {
        try (CloseableHttpResponse response = httpClient.execute(
                httpGet, HttpClientContext.create())) {

            return EntityUtils.toString(response.getEntity());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {

        if (cm != null) {
            return cm;
        }

        return new PoolingHttpClientConnectionManager();
    }
}
