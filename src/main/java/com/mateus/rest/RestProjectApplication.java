package com.mateus.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.mateus.rest.security.SecurityFilter;
import com.mateus.rest.service.ConfigurationResource;

@ApplicationPath("/rest")
public class RestProjectApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public RestProjectApplication() {
		singletons.add(new ConfigurationResource());
		singletons.add(new SecurityFilter());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
