package com.project.mishcma.budgetingapp.service;


import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.util.List;

public interface FileService {

    List<S3ObjectSummary> getFilesSummary();

    List<String> getFileNames();

    void deleteFile(String key);

    String uploadFile(String name, File file);
}
