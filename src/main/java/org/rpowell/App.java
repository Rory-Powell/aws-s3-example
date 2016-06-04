package org.rpowell;

import org.rpowell.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private IS3Service s3Service;

    @Value("${s3.bucket}")
    private String BUCKET;

    @Value("${s3.bucket.directory}")
    private String S3_DIRECTORY;

    @Value("${filesystem.upload-directory}")
    private String UPLOAD_DIRECTORY;

    @Value("${filesystem.download-directory}")
    private String DOWNLOAD_DIRECTORY;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args)  throws Exception {
        // File on disk
        String fileName = "test-file.txt";
        File file = new File(UPLOAD_DIRECTORY + fileName);

        // Upload the file
        s3Service.uploadObject(file, BUCKET, S3_DIRECTORY);

        // Define target download path
        Path saveAs = Paths.get(DOWNLOAD_DIRECTORY + file.getName());

        // Download the file
        s3Service.downloadObject(BUCKET, S3_DIRECTORY + fileName, saveAs);

        // List the files in the bucket
        List<String> keys =  s3Service.listKeysInBucket(BUCKET);
    }
}
