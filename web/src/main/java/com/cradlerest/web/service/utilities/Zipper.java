package com.cradlerest.web.service.utilities;


import java.io.*;

import java.util.List;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class Zipper {
	private static final int BUFFER_SIZE = 1024*1024;

	public static File zip(List<File> files, File zipFile) {
		// source: https://stackoverflow.com/questions/25562262/how-to-compress-files-into-zip-folder-in-android
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFile.getAbsolutePath());
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			byte data[] = new byte[BUFFER_SIZE];

			for (File file : files) {
//                Log.v("Compress", "Adding: " + file);
				FileInputStream fi = new FileInputStream(file);
				origin = new BufferedInputStream(fi, BUFFER_SIZE);

				ZipEntry entry = new ZipEntry(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1));
				out.putNextEntry(entry);
				int count;

				while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return zipFile;
	}

	// Source: https://www.journaldev.com/960/java-unzip-file-example
	public static HashMap<String, byte[]> unZip(InputStream zipFile, String destDir) {

		HashMap<String, byte[]> zippedFiles = new HashMap<String, byte[]>();

		//buffer for read and write data to file
		byte[] buffer = new byte[BUFFER_SIZE];
		try { ;
			ZipInputStream zis = new ZipInputStream(zipFile);
			ZipEntry ze = zis.getNextEntry();
			while(ze != null){

				String fileName = ze.getName();
				ByteArrayOutputStream zippedFile = new ByteArrayOutputStream();

				int len;
				while ((len = zis.read(buffer)) > 0) {
					zippedFile.write(buffer, 0, len);
				}

				zippedFile.flush();
				byte[] byteArray = zippedFile.toByteArray();
				zippedFiles.put(fileName, byteArray);

//                File newFile = new File(destDir + File.separator + fileName);
//                System.out.println("Unzipping to " + newFile.getAbsolutePath());
//                //create directories for sub directories in zip
//                new File(newFile.getParent()).mkdirs();
//                FileOutputStream fos = new FileOutputStream(newFile);
//                int len;
//                while ((len = zis.read(buffer)) > 0) {
//                    fos.write(buffer, 0, len);
//                }
//                fos.close();
				//close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			//close last ZipEntry
			zis.closeEntry();
			zis.close();
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return zippedFiles;
	}

}