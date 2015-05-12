package com.github.czyzby.tests.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gwtref.client.ReflectionCache;
import com.github.czyzby.tests.Main;

public class HtmlLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(1024, 512);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		logAllProperties(getCache());
		return new Main();
	}

	private native void logAllProperties(Object object)/*-{
		var prototype=object.__proto__;
		for( var property in prototype){
		console.log(prototype[property]);
		}
		console.log(prototype);
	}-*/;

	private native void log(Object message)/*-{
		console.log(message);
	}-*/;

	private native ReflectionCache getCache() /*-{
		return @com.badlogic.gwtref.client.ReflectionCache::instance;
	}-*/;
}