package com.project.mishcma.budgetingapp.helper;

import com.project.mishcma.budgetingapp.dto.TransactionDTO;
import com.project.mishcma.budgetingapp.entity.TransactionType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = {"Symbol", "Action", "Date", "Quantity", "Price", "Commission"};

  public static boolean hasCSVFormat(MultipartFile file) {
    return TYPE.equals(file.getContentType());
  }

  public static List<TransactionDTO> csvToTransactions(InputStream is) {
    try (BufferedReader fileReader =
            new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        CSVParser csvParser =
            new CSVParser(
                fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

      List<TransactionDTO> transactions = new ArrayList<>();
      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

      for (CSVRecord csvRecord : csvRecords) {
        TransactionDTO transaction =
            new TransactionDTO(
                csvRecord.get("Symbol"),
                TransactionType.valueOf(csvRecord.get("Action").toUpperCase()),
                dateFormat.parse(csvRecord.get("Date")),
                Double.valueOf(csvRecord.get("Quantity")),
                Double.valueOf(csvRecord.get("Price")),
                Double.valueOf(csvRecord.get("Commission")));
        transactions.add(transaction);
      }
      return transactions;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
