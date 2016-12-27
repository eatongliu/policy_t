package com.gpdata.wanyou.utils;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;


public class HttpClientUtil {


    private static Pattern HTTP_HOST_URL = Pattern.compile("^((?:https?://)?[^/]*(?::\\d+)?).*$");

    private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 3) {  // 如果已经重试了 3 次，就放弃                     
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试                    
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常                   
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时                   
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达                   
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {// 连接被拒绝                   
                return false;
            }
            if (exception instanceof SSLException) {// ssl握手异常                    
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试  
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        }

    };

    private static PoolingHttpClientConnectionManager connManager;

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = null;
        //sslsf = SSLConnectionSocketFactory.getSocketFactory();
        sslsf = SSLConnectionSocketFactoryCreator.getSSLConnectionSocketFactory();

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();

        connManager = new PoolingHttpClientConnectionManager(registry);

        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(20);
//
//        Set<String> availableHosts = getAvailableHosts(WebApiInvokerFactory::getHttpUrls);
//
//        availableHosts.stream()
//            .filter(StringUtils::isNotBlank)
//            .peek(url -> {
//                System.out.println("url : "+ url);
//            })
//            .map(input -> HTTP_HOST_URL.matcher(input))
//            .filter(Matcher::find)
//            .map(matcher -> matcher.group(1))
//            .map(HttpHost::create)
////            .peek(httpHost -> {
////                System.out.printf("httpHost : %s, %s, %s, %s%n", httpHost.getSchemeName()
////                    , httpHost.getHostName(), httpHost.getPort(),
////                        httpHost.getAddress());
////            })
//            .forEach(httpHost ->  connManager.setMaxPerRoute(new HttpRoute(httpHost), 20));
    }

    public static Set<String> getAvailableHosts(Supplier<Set<String>> supplier) {
        return supplier.get();
    }

    /**
     * @return
     */
    public static final CloseableHttpClient getCloseableHttpClient() {

        CloseableHttpClient result = HttpClients.custom()
                .setConnectionManager(connManager)
                .setRetryHandler(retryHandler)
                .build();
        return result;

    }
}
