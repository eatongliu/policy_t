package com.gpdata.wanyou.utils;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class SSLConnectionSocketFactoryCreator {

    /**
     * @return
     */
    public static final SSLConnectionSocketFactory getSSLConnectionSocketFactory() {

        KeyStore trustStore = null;
        SSLContext sslContext = null;
        SSLConnectionSocketFactory result = null;
        try {
            trustStore = KeyStore
                    .getInstance(KeyStore.getDefaultType());
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                    .build();
            result = new SSLConnectionSocketFactory(
                    sslContext,
                    NoopHostnameVerifier.INSTANCE);

        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        return result;
    }
}
