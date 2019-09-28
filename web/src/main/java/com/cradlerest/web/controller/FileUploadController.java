package com.cradlerest.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.stream.Collectors;

import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.util.Zipper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.json.JSONObject;

import com.cradlerest.web.service.storage.StorageFileNotFoundException;
import com.cradlerest.web.service.storage.StorageService;
import com.cradlerest.web.service.PatientManagerService;


import com.cradlerest.web.util.HybridFileDecrypter;


@Controller
public class FileUploadController {

	private final StorageService storageService;
	private final PatientManagerService patientManagerService;


	@Autowired
	public FileUploadController(StorageService storageService, PatientManagerService patientManagerService) {
		this.storageService = storageService;
		this.patientManagerService = patientManagerService;

	}

	@GetMapping("/upload")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
								   RedirectAttributes redirectAttributes) {

//		storageService.store(file);
		saveEncryptedFile(file);

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/upload";
	}

	@PostMapping(value = "/upload_reading", consumes = "multipart/form-data")
	public String handleReadingUpload(@RequestParam("userDataFile") MultipartFile file,
									  RedirectAttributes redirectAttributes) throws Exception {

		storageService.store(file);
		saveEncryptedFile(file);
		patientManagerService.constructReadingFromEncrypted(file);

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/upload";
	}


	private void saveEncryptedFile(MultipartFile file) {
		try {
			// Unzip uploaded file
			HashMap<String, byte[]> encryptedFiles = Zipper.unZip(file.getInputStream());

			// Decrypt unzipped files
			ByteArrayInputStream decryptedZip = HybridFileDecrypter.hybridDecryptFile(encryptedFiles);
			// Unzip the decrypted data




//				System.out.println(readingFile.getKey());

//			JSONObject reading = new JSONObject(new String(decryptedZip.readAllBytes()));
//				System.out.println(reading.toString());

//			ByteArrayInputStream newFile = new ByteArrayInputStream(decryptedZip.readAllBytes());
			storageService.storeBytes(decryptedZip, "reading.json");


		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
