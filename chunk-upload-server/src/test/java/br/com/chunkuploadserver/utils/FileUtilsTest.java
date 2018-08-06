package br.com.chunkuploadserver.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class FileUtilsTest {

	private static final int ONE_BYTE = 1;

	@Test
	public void shouldSliptFile() throws IOException {
		File file = ResourceUtils.getFile("classpath:teste.txt");
		
		String pathUpload = "./upload-test";
		List<File> files = FileUtils.split(pathUpload, file, ONE_BYTE);
		
		String filePath = pathUpload + "/" + file.getName();
		
		assertFalse(files.isEmpty());
		assertEquals(file.length() / ONE_BYTE, files.size());
		
		for (int i = 0; i < file.length(); i++) {
			assertTrue(new File(filePath + "." + i).exists());
		}
		
		FileUtils.deleteFilesAndFolder(pathUpload, files);
	}
	
	

}
