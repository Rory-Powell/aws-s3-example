package org.rpowell.services;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface IS3Service {

    void uploadObject(File file, String bucket, String uploadDirectory);

    void downloadObject(String bucket, String key, Path saveAs);

    List<String> listKeysInBucket(String bucket);
}
