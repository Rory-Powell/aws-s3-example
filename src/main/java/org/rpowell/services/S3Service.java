package org.rpowell.services;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;

@Service
@PropertySource("classpath:config.properties")
public class S3Service implements IS3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    @Value("${s3.bucket}")
    private String BUCKET;

    @Value("${s3.bucket.upload-directory}")
    private String BUCKET_DIRECTORY;

    private AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());

    @Override
    public void uploadFile(File file) {
        uploadFile(file, BUCKET_DIRECTORY + file.getName());
    }

    @Override
    public void uploadFiles(List<File> files) {
        files.forEach(this::uploadFile);
    }

    @Override
    public List<String> retrieveFileNames() {
        return null;
    }

    @Override
    public File retrieveFile(String fileName) {
        return null;
    }

    /**
     * Upload a file to S3.
     * @param file  The file to upload.
     * @param key   The destination path of the file.
     */
    private void uploadFile(File file, String key) {
        log.info("Uploading a new object to S3 from a file\n" + file);
        try {
            s3Client.putObject(new PutObjectRequest(BUCKET, key, file));
        } catch (Exception e) {
            log.error("Error uploading object to S3 from file\n" + file, e);
        }
    }

}
