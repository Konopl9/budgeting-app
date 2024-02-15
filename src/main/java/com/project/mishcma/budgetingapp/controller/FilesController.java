package com.project.mishcma.budgetingapp.controller;

import com.project.mishcma.budgetingapp.exception.StockSymbolNotFoundException;
import com.project.mishcma.budgetingapp.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("files")
public class FilesController {

  private final FileService fileService;

  public FilesController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping
  public String showFilesPage(
      @RequestParam(name = "portfolioName") String portfolioName, Model model) {
    model.addAttribute("fileNames", fileService.getFileNames());
    model.addAttribute("portfolioName", portfolioName);
    return "files";
  }

  @PostMapping
  public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
    fileService.uploadFile(file);
    model.addAttribute("fileNames", fileService.getFileNames());
    return "files";
  }

  @PostMapping(value = "/{fileName}")
  public String processFile(
      @RequestParam(name = "portfolioName") String portfolioName,
      @PathVariable String fileName,
      Model model) {
    try {
      Integer addedRows = fileService.processCsvFile(fileName, portfolioName);
      model.addAttribute("success", String.format("Transactions added: %d", addedRows));
      return "files :: alert-container";
    } catch (StockSymbolNotFoundException e) {
      model.addAttribute("error", e.getMessage());
      return "files :: alert-container";
    }
  }

  @ResponseBody
  @DeleteMapping(value = "/{fileName}", produces = MediaType.TEXT_HTML_VALUE)
  public String deleteFile(@PathVariable String fileName) {
    fileService.deleteFile(fileName);
    return "";
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  public ModelAndView handleUnsupportedOperationException(Exception ex) {
    ModelAndView model = new ModelAndView("files");
    model.addObject("error", ex);
    model.addObject("fileNames", fileService.getFileNames());
    return model;
  }
}
