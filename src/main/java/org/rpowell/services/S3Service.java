package org.rpowell.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:config.properties")
public class S3Service implements IS3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    private AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());

    /**
     * Upload a file to S3.
     * @param file              The file to upload.
     * @param bucket            The S3 bucket.
     * @param uploadDirectory   The subdirectory in the bucket.
     */
    @Override
    public void uploadObject(File file, String bucket, String uploadDirectory) {
        log.info("Uploading a new object to S3 from a file\n" + file);

        // Combine upload directory and file name to get S3 key
        String key = uploadDirectory + file.getName();

        try {
            // Upload the file to S3
            s3Client.putObject(new PutObjectRequest(bucket, key, file));
        } catch (AmazonClientException e) {
            log.error("Error uploading object to S3 from file\n" + file, e);
        }
    }

    /**
     * Download a file from S3.
     * @param bucket    The S3 bucket.
     * @param key       The key of the target object.
     * @param saveAs    The file path to save to save the download as.
     */
    @Override
    public void downloadObject(String bucket, String key, Path saveAs) {
        log.info("Downloading object from S3\n" + key);
        try {
            // Download from S3
            S3Object object = s3Client.getObject(new GetObjectRequest(bucket, key));

            // Get the contents as an input stream
            InputStream objectData = object.getObjectContent();

            // Write the stream to disk
            Files.copy(objectData, saveAs);

            // Close the stream
            objectData.close();
        } catch (AmazonClientException e) {
            log.error("Error downloading object from S3\n" + key, e);
        } catch (IOException e) {
            log.error("Error writing file\n" + key, e);
        }
    }

    /**
     * Retrieve a list of all object keys from an S3 bucket.
     * @param bucket    The bucket to list keys from.
     * @return          The keys.
     */
    public List<String> listKeysInBucket(String bucket) {

        return listSummariesInBucket(bucket).stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }


    /**
     * Retrieve a list of all object summaries from an S3 bucket.
     * @param bucket    The bucket to list summaries from.
     * @return          The {@link S3ObjectSummary}'s.
     */
    private List<S3ObjectSummary> listSummariesInBucket(String bucket) {
        // Get initial object listing from bucket
        ObjectListing listing = s3Client.listObjects(bucket);

        // Populate summaries from listing
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        // Step over all listings in the bucket and continue populating summaries
        while (listing.isTruncated()) {
            listing = s3Client.listNextBatchOfObjects(listing);
            summaries.addAll(listing.getObjectSummaries());
        }

        return summaries;
    }
}
