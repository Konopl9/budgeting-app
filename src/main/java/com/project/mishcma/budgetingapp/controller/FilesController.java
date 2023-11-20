package com.project.mishcma.budgetingapp.controller;


import com.project.mishcma.budgetingapp.service.FileService;
import io.github.wimdeblauwe.hsbt.mvc.HtmxResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        String info = fileService.uploadFile(file);
        model.addAttribute("fileNames", fileService.getFileNames());
        return "files";
    }

    @ResponseBody
    @DeleteMapping(value = "/{fileName}", produces = MediaType.TEXT_HTML_VALUE)
    public String deleteFile(@PathVariable String fileName) {
        fileService.deleteFile(fileName);
        return "";
    }

}
