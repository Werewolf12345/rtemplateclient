package com.alexeiboriskin.sbtask.rtemplateclient.services;

import com.alexeiboriskin.sbtask.rtemplateclient.models.User;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceRestTemplateImpl implements UserService {

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    private static final String URL = "https://localhost:8081/users/";

    private final RestTemplate restTemplate;

    public UserServiceRestTemplateImpl(@Value("${trust.store}") Resource trustStore,
                                       @Value("${trust.store.password}") String trustStorePassword) {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException | IOException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor(USER, PASSWORD));

        this.restTemplate = restTemplate;
    }

    @Override
    public User saveUser(User user) {
        User userInDb = findByUserName(user.getUsername());

        if (userInDb != null) {
            user.setId(userInDb.getId());
            restTemplate.put(URL + "{id}", user, user.getId());
            return restTemplate.getForObject(URL + "{id}", User.class,
                    user.getId());
        } else {
            return restTemplate.postForObject(URL, user, User.class);
        }
    }

    @Override
    public User updateUser(User user) {
        restTemplate.put(URL + "{id}", user, user.getId());
        return restTemplate.getForObject(URL + "{id}", User.class, user.getId());
    }

    @Override
    public User getUserById(Long id) {
        return restTemplate.getForObject(URL + "{id}", User.class, id);
    }

    @Override
    public List<User> listAllUsers() {
        User[] usersArray = restTemplate.getForObject(URL, User[].class);

        if (usersArray != null) {
            return  Arrays.asList(usersArray);
        }

        return Collections.emptyList();
    }

    @Override
    public void deleteUser(Long id) {
        restTemplate.delete(URL + "{id}", id);
    }

    @Override
    public User findByUserName(String username) {
        return restTemplate.getForObject(URL + "find?username={username}", User.class, username);
    }
}
