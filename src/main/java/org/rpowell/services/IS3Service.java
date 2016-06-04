package org.rpowell.services;

import java.io.File;
import java.nio.file.Path;

public interface IS3Service {

    void uploadObject(File file, String bucket, String uploadDirectory);

    void DownloadObject(String bucket, String key, Path saveAs);
}
