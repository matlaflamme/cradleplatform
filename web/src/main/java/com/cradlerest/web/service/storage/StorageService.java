package com.cradlerest.web.service.storage;

// Storage system taken from this example project
// https://github.com/spring-guides/gs-uploading-files

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	void storeBytes(ByteArrayInputStream bytesInput, String fileName);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
