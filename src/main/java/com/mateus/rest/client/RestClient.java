package com.mateus.rest.client;

import com.mateus.rest.domain.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {

    public Configuration getConfiguration(Integer id) {
        Client client = ClientBuilder.newClient();
        return client.target("http://localhost:8080/rest-project/rest/")
                .path("configurations/" + id).request(MediaType.APPLICATION_JSON).get(Configuration.class);
    }

    public static void main(String[] args) {
        RestClient restClient = new RestClient();
        Configuration configuration = restClient.getConfiguration(1);

        System.out.println(configuration.getContent());
    }
}
