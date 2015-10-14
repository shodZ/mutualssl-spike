import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;

public class ServerWithMutualSSLTest {

    @Test public void shouldRespondWith200UsingApacheHttpClientWithMutualSsl() throws Exception {
        CloseableHttpResponse httpResponse = customHttpsClient().execute(new HttpGet("https://localhost:8080/application/blah"));
        assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    }

    @Test public void shouldRespondWith200UsingJerseyClientWithMutualSsl() throws Exception {
        Response response = sslJerseyClient().target("https://localhost:8080/application/blah").request().buildGet().invoke();
        assertEquals(response.getStatus(), 200);
    }

    @BeforeClass public static void startServer() throws Exception {
        ServerWithMutualSSL server = new ServerWithMutualSSL();
        server.run(new String[]{"server", "src/test/resources/config.yml"});
    }
    
    private CloseableHttpClient customHttpsClient() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(customSSLContextFactory());
        return httpClientBuilder.build();
    }

    private SSLConnectionSocketFactory customSSLContextFactory() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        return new SSLConnectionSocketFactory(customSSLContext(), NoopHostnameVerifier.INSTANCE);
    }

    private SSLContext customSSLContext() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException {
        SSLContextBuilder sslContextBuilder = SSLContexts.custom();
        sslContextBuilder.loadTrustMaterial(new File("/Users/ssheth/p/mutualssl-spike/src/test/resources/keystore.jks"), "password".toCharArray());
        sslContextBuilder.loadKeyMaterial(new File("/Users/ssheth/p/mutualssl-spike/src/test/resources/keystore.jks"), "password".toCharArray(), "password".toCharArray());
        return sslContextBuilder.build();
    }

    private JerseyClient sslJerseyClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException {
        return new JerseyClientBuilder().sslContext(customSSLContext()).build();
    }

}
