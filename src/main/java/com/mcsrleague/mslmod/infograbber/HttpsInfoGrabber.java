package com.mcsrleague.mslmod.infograbber;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

public class HttpsInfoGrabber extends InfoGrabber {

    private final String urlString;
    private HttpClient httpClient;

    public HttpsInfoGrabber(String url) {
        super();
        this.urlString = url;
        httpClient = null;
    }

    @Override
    public String getError() {
        return super.getError().replace(urlString, "[urlString]");
    }

    private HttpClient getHttpClient() throws Exception {

        //https://stackoverflow.com/a/28847175

        HttpClientBuilder b = HttpClientBuilder.create();

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) {
                return true;
            }
        }).build();
        b.setSslcontext(sslContext);

        X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        b.setConnectionManager(connMgr);
        return b.build();
    }

    @Override
    public String grab() throws Exception {
        if (httpClient == null) {
            httpClient = getHttpClient();
        }
        HttpGet request = new HttpGet(urlString);
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String out = EntityUtils.toString(entity);
        response.close();
        return out;
    }

}
