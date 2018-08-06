package br.com.chunkuploadserver.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<File> split(String pathUpload, File file, Integer splitSize) throws IOException {
		Files.createDirectories(Paths.get(pathUpload));
		
		List<File> files = new ArrayList<>();
		FileInputStream fstream = new FileInputStream(file);
		long length = file.length();
		int index = 0;
		do {
			String newFileName = file.getName() + "." + index;
			
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(pathUpload + "/" + newFileName));
			writeFile(fstream, bw, splitSize);
			bw.close();
			
			files.add(new File(pathUpload, newFileName));
			length-= splitSize;
			index++;
			
		} while (length > 0);
		fstream.close();
		
		return files;
	}

	private static void writeFile(FileInputStream fstream, BufferedOutputStream bw, Integer splitSize) throws IOException {
		byte[] buf = new byte[(int) splitSize];
		int val = fstream.read(buf);
		if(val != -1) {
			bw.write(buf);
		}
	}
	
	public static void deleteFilesAndFolder(String path, List<File> files) {
		files.forEach(File::delete);
		new File(path).delete();
	}
	
	public static void deleteFilesAndFolder(String path, File file) {
		file.delete();
		new File(path).delete();
	}

}
