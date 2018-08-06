package br.com.chunkuploadserver.upload.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import br.com.chunkuploadserver.config.UserAuthentication;
import br.com.chunkuploadserver.upload.enums.UploadStatus;
import br.com.chunkuploadserver.upload.exception.UploadException;
import br.com.chunkuploadserver.upload.model.Upload;
import br.com.chunkuploadserver.user.model.User;
import br.com.chunkuploadserver.user.service.UserService;
import br.com.chunkuploadserver.utils.FileUtils;
import br.com.chunkuploadserver.utils.PathUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
public class UploadServiceTest {

	private static final Integer ONE_BYTE = 1;
	
	@Autowired
	private PathUtils pathUtils;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private UserService userService;
	
	private User user;
	
	@Before
	public void init() {
		String token = Jwts.builder()
	        .setSubject("admin")
	        .signWith(SignatureAlgorithm.HS512, "tooManySecrets")
	        .compact();
		
		user = new User();
		user.setId(1L);
		user.setUsername("admin");
		user.setPassword("admin");
		userService.save(user);
		
		UserAuthentication userAuthentication = new UserAuthentication(user, token);
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
	
	@Test
	public void shouldMakeUploadFile() throws Exception {
		String fileName = "file.txt";
		LocalDateTime start = LocalDateTime.now();
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
				fileName, fileName, "text/plain", "Arquivo de teste".getBytes());
		
		uploadService.makeUploadFile(mockMultipartFile, mockMultipartFile.getOriginalFilename(), null, null, null);
		
		File file =  pathUtils.getPath(fileName).toFile();
		assertTrue(file.exists());
		assertTrue(file.isFile());
		assertTrue(file.length() > 0);

		Upload upload = uploadService.findByUserNameAndFileName(user.getUsername(), fileName);
		assertNotNull(upload.getId());
		assertEquals(UploadStatus.DONE, upload.getStatus());
		assertTrue(upload.getEndUpload().compareTo(start) == 1);
		
		file.delete();
		file.getParentFile().delete();
		
	}
	
	@Test(expected=Exception.class)
	public void shouldThrowExceptionOfFileEmpty() throws UploadException {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
				"file.txt", "file.txt", "text/plain", "".getBytes());
		
		uploadService.makeUploadFile(mockMultipartFile, mockMultipartFile.getOriginalFilename(), null, null, null);
	}
	
	@Test
	public void shouldMergeChunksFile() throws IOException, UploadException {
		String pathUpload = pathUtils.getUploadPath().resolve("tmp").toString();
		String fileName = "teste.txt";
		LocalDateTime start = LocalDateTime.now();
		File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);
		List<File> files = FileUtils.split(pathUpload, file, ONE_BYTE);
		
		int index = 0;
		for (File f : files) {
			MockMultipartFile mockMultipartFile = new MockMultipartFile(f.getName(), f.getName(), 
					"text/plain", Files.readAllBytes(f.toPath()));
			
			uploadService.makeUploadFile(mockMultipartFile, fileName, index, files.size(), file.length());
			index++;
			
			Upload upload = uploadService.findByUserNameAndFileName(user.getUsername(), fileName);
			assertNotNull(upload.getId());
			assertEquals(index == files.size() ? UploadStatus.DONE : UploadStatus.IN_PROGRESS, upload.getStatus());
			assertTrue(upload.getEndUpload().compareTo(start) == 1);
		}
		
		File mergedFile = uploadService.getFileByName(file.getName());
		
		assertNotNull(mergedFile);
		assertTrue(mergedFile.length() > 0);
		assertEquals(file.length(), mergedFile.length());
		
		FileUtils.deleteFilesAndFolder(pathUpload, files);
		FileUtils.deleteFilesAndFolder(pathUtils.getPath(file.getName()).toString(), mergedFile);
	}

}
