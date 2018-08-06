package br.com.chunkuploadserver.upload.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.ResourceUtils;

import br.com.chunkuploadserver.upload.service.UploadService;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadController.class)
public class UploadControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private UploadService uploadService;
	
	@Test
	@WithMockUser(username="admin", password="admin")
	public void shouldMakeUpload() throws Exception {
		File file = ResourceUtils.getFile("classpath:teste.txt");
		
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "teste.txt", 
				"text/plain", Files.readAllBytes(file.toPath()));

		MockHttpServletRequestBuilder builder =
	              MockMvcRequestBuilders.multipart("/api/upload")
	                                    .file(mockMultipartFile)
	                                    .param("name", file.getName())
	                                    .with(csrf());
		
		mvc.perform(builder)
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@WithMockUser(username="admin", password="admin")
	public void shouldListUploads() throws Exception {
		mvc.perform(get("/api/list"))
			.andExpect(status().isOk());
	}

}
