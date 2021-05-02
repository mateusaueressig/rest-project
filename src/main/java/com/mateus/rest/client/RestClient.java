package com.mateus.rest.client;

import com.mateus.rest.domain.Configuration;
import org.jboss.resteasy.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class RestClient {

    String authorizationHeaderValue = "Basic " + Base64.encodeBytes( "testuser:abcabc".getBytes() );

    public Configuration getConfiguration(Integer id) {
        Client client = ClientBuilder.newClient();
        return client.target("http://localhost:8080/rest-project/rest/")
                .path("configurations/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", authorizationHeaderValue)
                .get(Configuration.class);
    }

    public static void main(String[] args) {
        RestClient restClient = new RestClient();
        Configuration configuration = restClient.getConfiguration(1);

        System.out.println(configuration.getContent());
    }
}
