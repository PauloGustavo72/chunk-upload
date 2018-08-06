package br.com.chunkuploadserver.upload.utils;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.chunkuploadserver.upload.enums.UploadStatus;
import br.com.chunkuploadserver.upload.model.Upload;
import br.com.chunkuploadserver.user.model.User;

public class UploadBuilder {

	private Upload upload;
	
	public UploadBuilder(Upload upload) {
		this.upload = upload;
	}
	
	public static UploadBuilder create() {
		Upload upload = new Upload();
		upload.setStartUpload(LocalDateTime.now());
		UploadBuilder uploadBuilder = new UploadBuilder(upload);
		return uploadBuilder;
	}

	public UploadBuilder withIndex(Integer index) {
		this.upload.setIndex(index);
		return this;
	}

	public UploadBuilder withTotalParts(Integer totalParts) {
		this.upload.setChunks(totalParts);
		return this;
	}

	public UploadBuilder withName(String name) {
		String fileName = name;
		if (upload.itWasSplited())
			fileName += "." + upload.getIndex();
		
		this.upload.setFileName(fileName);
		return this;
	}
	
	public UploadBuilder withFileSize(Long fileSize) {
		this.upload.setFileSize(fileSize);
		return this;
	}

	public Upload build() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.upload.setUser(user);

//		this.upload.setStatus(UploadStatus.DONE);
//		if (upload.itWasSplited() && !upload.finishedUpload(filePath))
			this.upload.setStatus(UploadStatus.IN_PROGRESS);
		
		return this.upload;
	}
}
