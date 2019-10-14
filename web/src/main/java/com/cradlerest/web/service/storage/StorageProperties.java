package com.cradlerest.web.service.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Properties for storage service
 */
@ConfigurationProperties("storage")
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "file-dir";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
