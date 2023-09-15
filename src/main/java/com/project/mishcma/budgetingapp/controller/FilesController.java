package com.project.mishcma.budgetingapp.controller;


import com.project.mishcma.budgetingapp.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FilesController {

    private final FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/showFiles")
    public String showFilesPage(Model model) {
        model.addAttribute("fileNames", fileService.getFileNames());
        return "files";
    }

    @DeleteMapping(value = "/{fileName}", produces = MediaType.TEXT_HTML_VALUE)
    public void deleteFile(@PathVariable String fileName) {
        fileService.deleteFile(fileName);
    }
}
