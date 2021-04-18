package com.mateus.rest.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.mateus.dao.ConfigurationDB;
import com.mateus.rest.domain.Configuration;
import com.mateus.rest.domain.Configurations;
import com.mateus.rest.domain.common.Message;
import com.mateus.rest.domain.common.Status;

@Path("/configurations")
@Produces("application/xml")
public class ConfigurationResource {

	@Context
	UriInfo uriInfo;
	
	@GET
	public Configurations getConfigurations() {
		List<Configuration> list = ConfigurationDB.getAllConfigurations();
        
        Configurations configurations = new Configurations();
        configurations.setConfigurations(list);
        configurations.setSize(list.size());
          
        //Set link for primary collection
        Link link = Link.fromUri(uriInfo.getPath()).rel("uri").build();
        configurations.setLink(link);
          
        //Set links in configuration items
        for(Configuration c: list){
            Link lnk = Link.fromUri(uriInfo.getPath() + "/" + c.getId()).rel("self").build();
            c.setLink(lnk);
        }
        return configurations;
	}
	
	@GET
	@Path("/{id}")
	public Response getConfigurationById(@PathParam("id") Integer id) {
		Configuration config = ConfigurationDB.getConfiguration(id);
     
        if(config == null) {
            return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
        }
          
        if(config != null){
            UriBuilder builder = UriBuilder.fromResource(ConfigurationResource.class)
                                            .path(ConfigurationResource.class, "getConfigurationById");
            Link link = Link.fromUri(builder.build(id)).rel("self").build();
            config.setLink(link);
        }
          
        return Response.status(javax.ws.rs.core.Response.Status.OK).entity(config).build();
    }

	@POST
	@Consumes("application/xml")
	public Response createConfiguration(Configuration config){
        if(config.getContent() == null)  {
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
                            .entity(new Message("Config content not found"))
                            .build();
        }
 
        Integer id = ConfigurationDB.createConfiguration(config.getContent(), config.getStatus());
        Link lnk = Link.fromUri(uriInfo.getPath() + "/" + id).rel("self").build();
        return Response.status(javax.ws.rs.core.Response.Status.CREATED).location(lnk.getUri()).build();
    }
	
	@PUT
	@Path("/{id}")
	@Consumes("application/xml")
	public Response updateConfiguration(@PathParam("id") Integer id, Configuration config) {
		Configuration origConfig = ConfigurationDB.getConfiguration(id);
        if(origConfig == null) {
            return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
        }
         
        if(config.getContent() == null)  {
            return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
                            .entity(new Message("Config content not found"))
                            .build();
        }
 
        ConfigurationDB.updateConfiguration(id, config);
        return Response.status(javax.ws.rs.core.Response.Status.OK).entity(new Message("Config Updated Successfully")).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteConfiguration(@PathParam("id") Integer id) {
		 Configuration origConfig = ConfigurationDB.getConfiguration(id);
	        if(origConfig == null) {
	            return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
	        }
	         
	        ConfigurationDB.removeConfiguration(id);
	        return Response.status(javax.ws.rs.core.Response.Status.OK).build();
	}
	
	 /**
     * Initialize the application with these two default configurations
     * */
    static {
        ConfigurationDB.createConfiguration("Some Content", Status.ACTIVE);
        ConfigurationDB.createConfiguration("Some More Content", Status.INACTIVE);
    }
}
