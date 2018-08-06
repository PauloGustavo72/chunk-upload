package br.com.chunkuploadserver.upload.service;

import static org.springframework.util.Assert.isTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.chunkuploadserver.upload.enums.UploadStatus;
import br.com.chunkuploadserver.upload.exception.UploadException;
import br.com.chunkuploadserver.upload.model.Upload;
import br.com.chunkuploadserver.upload.repository.UploadRepository;
import br.com.chunkuploadserver.upload.utils.UploadBuilder;
import br.com.chunkuploadserver.user.model.User;
import br.com.chunkuploadserver.utils.PathUtils;

@Service
public class UploadService {

	@Autowired
	private PathUtils pathUtils;
	
	@Autowired
	private UploadRepository uploadRespository;
	
	public void makeUploadFile(MultipartFile file, String originalFileName, Integer index, Integer totalParts, Long totalFileSize) throws UploadException {
		
		isTrue(!file.isEmpty(), "File is empty");
		
		Upload upload = UploadBuilder.create()
				.withIndex(index)
				.withTotalParts(totalParts)
				.withFileSize(totalFileSize)
				.withName(originalFileName)
				.build();
		
		Path source = pathUtils.getPath(originalFileName + "-tmp");
		try {			
			makeUpload(file, source, upload.getFileName());
			makeMergeFiles(file, source, upload, originalFileName);
			saveOrUpdateUpload(upload, index, originalFileName);
		} catch (Exception e) {
			upload.setStatus(UploadStatus.FAIL);
			saveOrUpdateUpload(upload, index, originalFileName);
			throw new UploadException(e.getMessage(), e);
		}
	}
	
	private void makeMergeFiles(MultipartFile file, Path source, Upload upload, String originalFileName) throws Exception {
		Path target = pathUtils.getNewFilePath(originalFileName);
		if (!upload.itWasSplited()) {
			upload.setStatus(UploadStatus.DONE);
			Files.copy(file.getInputStream(), target);
			
			Files.delete(source.resolve(upload.getFileName()));
			Files.delete(source);
		} else if (upload.finishedUpload(source.toFile())) {
			try {
				upload.setStatus(UploadStatus.DONE);
				mergeFiles(originalFileName, upload.getChunks(), target.toFile(), source);
			} catch (Exception e) {
				throw new UploadException("Error ocurred when merging chunks", e);
			} finally {
				Files.delete(source);				
			}
		}
	}

	private void makeUpload(MultipartFile file, Path source, String fileName) throws UploadException {
		try {
			Files.createDirectories(source);
			Files.copy(file.getInputStream(), source.resolve(fileName));
		} catch (IOException e) {
			throw new UploadException("Error ocurred when uploading file", e);
		}
	}

	private synchronized void saveOrUpdateUpload(Upload upload, Integer index, String originalFileName) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Upload savedUpload = uploadRespository.findByUserUsernameAndFileName(user.getUsername(), originalFileName);
		if (savedUpload == null) {
			savedUpload = new Upload();
			BeanUtils.copyProperties(upload, savedUpload);
			savedUpload.setFileName(originalFileName);
		}
		
		savedUpload.setEndUpload(LocalDateTime.now());
		savedUpload.setStatus(upload.getStatus());
		uploadRespository.save(savedUpload);
	}

	@SuppressWarnings("resource")
	private void mergeFiles(String name, Integer totalParts, File target, Path source) throws Exception {
		try (FileChannel dest = new FileOutputStream(target, true).getChannel()) {
			for(int i = 0; i < totalParts; i++) {
				File sourceFile = source.resolve(name + "." + i).toFile();
				try (FileChannel src = new FileInputStream(sourceFile).getChannel()) {
					dest.position(dest.size());
					src.transferTo(0, src.size(), dest);
				}
				sourceFile.delete();
			}
		} 
	}
	
	public File getFileByName(String name) {
		Path target = pathUtils.getPath(name);
		return target.toFile();
	}
	
	public File getFileById(Long id) {
		Optional<Upload> optional = uploadRespository.findById(id);
		if (optional.isPresent())
			return getFileByName(optional.get().getFileName());
		return null;
	}

	public List<Upload> listAll() {
		return uploadRespository.findAll();
	}

	public Upload findByUserNameAndFileName(String username, String fileName) {
		return uploadRespository.findByUserUsernameAndFileName(username, fileName);
	}

}
