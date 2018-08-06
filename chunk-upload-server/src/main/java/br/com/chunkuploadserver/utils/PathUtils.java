package br.com.chunkuploadserver.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.chunkuploadserver.user.model.User;

@Component
public class PathUtils {
	
	@Value("${upload.base-dir}")
	private String baseDir;
	
	public Path getUploadUserPath() {
		User customUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Path target = Paths.get(baseDir).resolve(customUser.getUsername());
		return target;
	}
	
	public Path getPath(String fileName) {
		User customUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Path target = Paths.get(baseDir).resolve(customUser.getUsername()).resolve(fileName);
		return target;
	}

	public Path getNewFilePath(String fileName) {
		Path target = getPath(fileName);
		target.toFile().delete();
		return target;
	}
	
	public Path getUploadPath() {
		Path target = Paths.get(baseDir);
		return target;
	}
	
}
