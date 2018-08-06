package br.com.chunkuploadserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ChunkUploadServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChunkUploadServerApplication.class, args);
	}
}
