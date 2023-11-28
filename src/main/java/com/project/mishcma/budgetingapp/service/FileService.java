package com.project.mishcma.budgetingapp.service;


import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<S3ObjectSummary> getFilesSummary();

    List<String> getFileNames();

    void deleteFile(String key);

    String uploadFile(MultipartFile file);

    S3Object getFileByName(String name);

    Integer processCsvFile(String name, String portfolioName);
}
