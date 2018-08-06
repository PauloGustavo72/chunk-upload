package br.com.chunkuploadserver.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.chunkuploadserver.upload.model.Upload;

public interface UploadRepository extends JpaRepository<Upload, Long>{

	Upload findByUserUsernameAndFileName(String username, String fileName);

}
