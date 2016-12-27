package com.gpdata.wanyou.servlet;

import com.gpdata.wanyou.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by chengchao on 16-10-12.
 */
public class ForeignHtmlPreviewServlet extends BaseServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForeignHtmlPreviewServlet.class);

    @Override
    protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getParameter("url");
        LOGGER.debug("request url : {}", url);

        if (StringUtils.isBlank(url)) {
            resp.sendError(404);
            return;
        }

        if (!url.toLowerCase().startsWith("http")) {
            url = "http://" + url;
        }

        AsyncContext asyncContext = req.startAsync();

        new Thread(new Executor(asyncContext, url)).start();

    }

    private String retrieveBaseUrl(String input) {
        int p = input.lastIndexOf("/");
        if (p != -1 && input.toLowerCase().startsWith("http") && p > 7) {

            String x = input.substring(0, p);
            return x;
        }
        return "";
    }

    private String retrieveRootUrl(String input) {
        Pattern rootUrlPattern = Pattern.compile("^(http(s)?://[a-zA-Z0-9._-]+(:\\d+)?)(?:/)?");
        Matcher matcher = rootUrlPattern.matcher(input);

        if (matcher.find()) {
            return (matcher.group(1));
        }
        return "";
    }

    private String replaceCssPrefix(String url, String content) {

        String rootUrl = this.retrieveRootUrl(url);
        String baseUrl = this.retrieveBaseUrl(url) + "/";

        Pattern linkHref = Pattern.compile("(<link[^>]*href(?:\\s*)=(?:\\s*)(?:\"|\'))(.*)((?:\"|\')(?:\\s*|/))");

        Matcher matcher = linkHref.matcher(content);
        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            String linkHrefValueHeader = matcher.group(1);
            String linkHrefValue = matcher.group(2);
            String linkHrefValueTail = matcher.group(3);
            if (!linkHrefValue.toLowerCase().startsWith("http")) {
                if (linkHrefValue.startsWith("/")) {
                    linkHrefValue = linkHrefValueHeader + rootUrl + linkHrefValue + linkHrefValueTail;
                    System.out.println("linkHrefValue : " + linkHrefValue);
                } else if (linkHrefValue.startsWith(".")) {
                    linkHrefValue = linkHrefValueHeader + baseUrl + linkHrefValue + linkHrefValueTail;
                    System.out.println("linkHrefValue : " + linkHrefValue);
                }
                matcher.appendReplacement(stringBuffer, linkHrefValue);
            }
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }


    private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");

    private class Executor implements Runnable {


        private HttpServletRequest req;
        private HttpServletResponse resp;
        private String url;

        Executor(AsyncContext asyncContext, String url) {
            this.req = (HttpServletRequest) asyncContext.getRequest();
            this.resp = (HttpServletResponse) asyncContext.getResponse();
            this.url = url;
        }


        private Charset getCharset(byte[] bytes) {

            String content = new String(bytes);
            Matcher matcher = patternForCharset.matcher(content);
            if (matcher.find()) {
                String charset = matcher.group(1);
                if (Charset.isSupported(charset)) {
                    return Charset.forName(charset);
                }
            }

            return Charset.forName("UTF-8");

        }

        /**
         *
         */
        @Override
        public void run() {

            CloseableHttpClient httpClient = HttpClientUtil.getCloseableHttpClient();
            HttpGet httpGet = new HttpGet(url);

            httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0");
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.addHeader("Accept-Language", "zh,zh-CN;q=0.8,zh-TW;q=0.6,en-US;q=0.4,en;q=0.2");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
            httpGet.addHeader("Referer", "http://www.sogou.com/");
            httpGet.addHeader("Cookie", "JSESSIONID=659CACE186D35F08F9F11C88287AA89C");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("Upgrade-Insecure-Requests", "1");
            httpGet.addHeader("Cache-Control", "max-age=0");


            try (
                    OutputStream outputStream = resp.getOutputStream();
                    CloseableHttpResponse response = httpClient.execute(httpGet)
            ) {
                if (response != null && response.getEntity() != null) {

                    HttpEntity entity = response.getEntity();
                    Header[] allHeaders = response.getAllHeaders();

                    Stream.of(allHeaders)
                            .forEach(header -> resp.setHeader(header.getName(), header.getValue()));

                    ContentType contentType = ContentType.get(entity);
                    Charset charset = contentType.getCharset();
                    String mineType = contentType.getMimeType();

                    boolean isHtml = "text/html".equalsIgnoreCase(mineType);
                    LOGGER.debug("{} >> {}", mineType, isHtml);

                    byte[] buff = EntityUtils.toByteArray(entity);

                    if (isHtml == true) {

                        if (charset == null) {
                            charset = this.getCharset(buff);
                        }

                        LOGGER.debug("charset >> {}", charset);
                        resp.setCharacterEncoding(charset.toString());
                        String content = new String(buff, charset);
                        String result = ForeignHtmlPreviewServlet.this.replaceCssPrefix(url, content);
                        buff = result.getBytes(charset);
                        outputStream.write(buff);
                        outputStream.flush();
                    } else {
                        outputStream.write(buff);
                        outputStream.flush();
                    }

                    EntityUtils.consume(entity);
                    resp.flushBuffer();
                } else {
                    resp.sendError(204);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    resp.sendError(404);
                } catch (Exception ex) {
                    //
                }

            }

        }
    }
}
