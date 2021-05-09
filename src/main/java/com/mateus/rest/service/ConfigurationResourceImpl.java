package com.mateus.rest.service;

import com.mateus.dao.ConfigurationDB;
import com.mateus.rest.domain.Configuration;
import com.mateus.rest.domain.Configurations;
import com.mateus.rest.domain.common.Message;
import com.mateus.rest.domain.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;
import java.util.List;


public class ConfigurationResourceImpl implements ConfigurationResource {

    private static Logger logger = LoggerFactory.getLogger(ConfigurationResource.class);

    @Context
    private UriInfo uriInfo;

    @Override
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

    @Override
	public Response getConfigurationById(@PathParam("id") Integer id) {
        logger.debug("getConfigurationById - id={}", id);
		Configuration config = ConfigurationDB.getConfiguration(id);
     
        if(config == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
          
        if(config != null){
            UriBuilder builder = UriBuilder.fromResource(ConfigurationResourceImpl.class)
                                            .path(ConfigurationResourceImpl.class, "getConfigurationById");
            Link link = Link.fromUri(builder.build(id)).rel("self").build();
            config.setLink(link);
        }
          
        return Response.status(Response.Status.OK).entity(config).build();
    }

    @Override
	public Response createConfiguration(Configuration config){
        if(config.getContent() == null)  {
            return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new Message("Config content not found"))
                            .build();
        }
 
        Integer id = ConfigurationDB.createConfiguration(config.getContent(), config.getStatus());
        Link lnk = Link.fromUri(uriInfo.getPath() + "/" + id).rel("self").build();
        return Response.status(Response.Status.CREATED).location(lnk.getUri()).build();
    }

    @Override
	public Response updateConfiguration(@PathParam("id") Integer id, Configuration config) {
		Configuration origConfig = ConfigurationDB.getConfiguration(id);
        if(origConfig == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
         
        if(config.getContent() == null)  {
            return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new Message("Config content not found"))
                            .build();
        }
 
        ConfigurationDB.updateConfiguration(id, config);
        return Response.status(Response.Status.OK).entity(new Message("Config Updated Successfully")).build();
	}

    @Override
	public Response deleteConfiguration(@PathParam("id") Integer id) {
		 Configuration origConfig = ConfigurationDB.getConfiguration(id);
	        if(origConfig == null) {
	            return Response.status(Response.Status.NOT_FOUND).build();
	        }
	         
	        ConfigurationDB.removeConfiguration(id);
	        return Response.status(Response.Status.OK).build();
	}
	
	 /**
     * Initialize the application with these two default configurations
     * */
    static {
        ConfigurationDB.createConfiguration("Some Content", Status.ACTIVE);
        ConfigurationDB.createConfiguration("Some More Content", Status.INACTIVE);
    }
}
