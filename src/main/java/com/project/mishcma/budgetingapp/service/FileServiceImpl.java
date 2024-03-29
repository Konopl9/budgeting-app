package com.project.mishcma.budgetingapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.helper.CSVHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

  private static final String BUCKET_NAME = "budgeting-app-storage";
  private final AmazonS3 s3Client;
  private final TransactionService transactionService;

  private final MarketDataService marketDataService;

  public FileServiceImpl(
      AmazonS3 s3Client,
      TransactionService transactionService,
      MarketDataService marketDataService) {
    this.s3Client = s3Client;
    this.transactionService = transactionService;
    this.marketDataService = marketDataService;
  }

  @Override
  public List<S3ObjectSummary> getFilesSummary() {
    ObjectListing objectListing = s3Client.listObjects(BUCKET_NAME);
    return objectListing.getObjectSummaries();
  }

  @Override
  public List<String> getFileNames() {
    List<S3ObjectSummary> objectSummaries = getFilesSummary();
    List<String> fileNames = new ArrayList<>(objectSummaries.size());

    for (S3ObjectSummary objectSummary : objectSummaries) {
      fileNames.add(objectSummary.getKey());
    }

    return fileNames;
  }

  @Override
  public S3Object getFileByName(String name) {
    GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, name);
    return s3Client.getObject(getObjectRequest);
  }

  @Override
  public Integer processCsvFile(String fileName, String portfolioName)
      throws StockSymbolNotFoundException {
    S3Object file = getFileByName(fileName);
    List<TransactionDTO> transactionsToAdd =
        CSVHelper.csvToTransactions(file.getObjectContent().getDelegateStream());
    marketDataService.postStockSymbolsData(
        transactionsToAdd.stream().map(TransactionDTO::getTicker).toList());
    return transactionService.saveTransactions(portfolioName, transactionsToAdd);
  }

  @Override
  public void deleteFile(String key) {
    DeleteObjectRequest request = new DeleteObjectRequest(BUCKET_NAME, key);
    s3Client.deleteObject(request);
  }

  @Override
  public String uploadFile(MultipartFile multipartFile) {
    if (!CSVHelper.hasCSVFormat(multipartFile)) {
      throw new UnsupportedOperationException("Exception! Please upload file of type .csv ");
    }
    File file = convertMultiPartToFile(multipartFile);
    PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, file.getName(), file);
    PutObjectResult result = s3Client.putObject(request);
    return result.getContentMd5();
  }

  private File convertMultiPartToFile(MultipartFile file) {
    File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return convertedFile;
  }
}
