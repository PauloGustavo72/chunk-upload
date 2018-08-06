package br.com.chunkuploadserver.upload.model;

import java.io.File;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.chunkuploadserver.upload.enums.UploadStatus;
import br.com.chunkuploadserver.user.model.User;

@Entity
public class Upload {

	@Id
	@GeneratedValue
	private Long id;
	
	private String fileName;
	
	@Enumerated(EnumType.STRING)
	private UploadStatus status;
	
	private LocalDateTime startUpload;
	
	private LocalDateTime endUpload;
	
	private Integer chunks;
	
	private Long fileSize;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;

	@Transient
	@JsonIgnore
	private Integer index;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public UploadStatus getStatus() {
		return status;
	}

	public void setStatus(UploadStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartUpload() {
		return startUpload;
	}

	public void setStartUpload(LocalDateTime startUpload) {
		this.startUpload = startUpload;
	}

	public LocalDateTime getEndUpload() {
		return endUpload;
	}

	public void setEndUpload(LocalDateTime endUpload) {
		this.endUpload = endUpload;
	}

	public Integer getChunks() {
		return chunks;
	}

	public void setChunks(Integer chunks) {
		this.chunks = chunks;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Long fileSize){
		this.fileSize = fileSize;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public Integer getIndex() {
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}

	public boolean finishedUpload(File filePath) {
		return (filePath.list() != null && chunks != null) && 
				filePath.list().length == chunks.intValue();
	}

	public boolean itWasSplited() {
		return chunks != null && chunks > 1;
	}
}
