package com.mateus.rest.service;

import com.mateus.rest.domain.Configuration;
import com.mateus.rest.domain.Configurations;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/configurations")
@Produces("application/json")
public interface ConfigurationResource {

    @PermitAll
    @GET
    Configurations getConfigurations();

    @RolesAllowed("ADMIN")
    @GET
    @Path("/{id}")
    Response getConfigurationById(@PathParam("id") Integer id);

    @RolesAllowed("ADMIN")
    @POST
    @Consumes("application/json")
    Response createConfiguration(Configuration config);

    @RolesAllowed("ADMIN")
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    Response updateConfiguration(@PathParam("id") Integer id, Configuration config);

    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/{id}")
    Response deleteConfiguration(@PathParam("id") Integer id);
}
