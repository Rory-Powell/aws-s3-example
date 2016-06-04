package org.rpowell.services;

import java.io.File;
import java.util.List;

public interface IS3Service {

    void uploadFile(File file);

    void uploadFiles(List<File> files);

    List<String> retrieveFileNames();

    File retrieveFile(String fileName);
}
