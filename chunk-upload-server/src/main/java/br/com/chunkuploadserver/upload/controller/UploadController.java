package br.com.chunkuploadserver.upload.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.chunkuploadserver.upload.exception.UploadException;
import br.com.chunkuploadserver.upload.model.Upload;
import br.com.chunkuploadserver.upload.service.UploadService;

@RestController
public class UploadController {

	@Autowired
	private UploadService uploadService;
	
	@CrossOrigin
	@PostMapping(value="/api/upload")
	public ResponseEntity<Upload> upload(
			@RequestParam(required=true) MultipartFile file,
			@RequestParam(required=true) String name,
			@RequestParam(required=false) Integer index,
			@RequestParam(required=false) Integer totalParts,
			@RequestParam(required=false) Long totalFileSize) throws UploadException  {
		
		uploadService.makeUploadFile(file, name, index, totalParts, totalFileSize);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/api/list")
	public ResponseEntity<List<Upload>> list(){
		return ResponseEntity.ok(uploadService.listAll());
	}
	
	@GetMapping("/api/download/{fileId}")
	@ResponseBody
	public FileSystemResource download(@PathVariable Long fileId, HttpServletResponse response) throws UploadException {
		try {
			return new FileSystemResource( uploadService.getFileById(fileId));
		} catch (Exception e) {
			throw new UploadException("Error to download", e);
		}
	}
}
