package com.project.mishcma.budgetingapp.controller;


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
    public String showFilesPage(Model model) {
        model.addAttribute("fileNames", fileService.getFileNames());
        return "files";
    }

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        fileService.uploadFile(file);
        model.addAttribute("fileNames", fileService.getFileNames());
        return "files";
    }

    @PostMapping(value = "/{fileName}")
    public String processFile(@PathVariable String fileName, Model model) {
        Integer addedRows = fileService.processCsvFile(fileName, "My Portfolio");
        if (addedRows <= 0) {
            model.addAttribute("error", "The error occurred.");
            return "files :: alert-container";
        }
        model.addAttribute("success", String.format("Transactions added: %d", addedRows));
        return "files :: alert-container";
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
