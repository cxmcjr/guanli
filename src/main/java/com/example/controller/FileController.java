package com.example.controller;

import com.example.entity.UploadFile;
import com.example.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("files", fileService.findAll());
        return "file/list";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile[] files,
                         @RequestParam String category,
                         @RequestParam(required = false) String relatedType,
                         @RequestParam(required = false) Integer relatedId,
                         RedirectAttributes ra) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    fileService.upload(file, category, relatedType, relatedId);
                } catch (IOException e) {
                    ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                    return "redirect:/file";
                }
            }
        }
        ra.addFlashAttribute("msg", "上传成功");
        return "redirect:/file";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Integer id) {
        UploadFile uploadFile = fileService.findById(id);
        if (uploadFile == null) {
            return ResponseEntity.notFound().build();
        }
        File file = new File(uploadFile.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(file);
        String encodedName = URLEncoder.encode(uploadFile.getOriginalName(), StandardCharsets.UTF_8)
                .replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .body(resource);
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> preview(@PathVariable Integer id) {
        UploadFile uploadFile = fileService.findById(id);
        if (uploadFile == null) {
            return ResponseEntity.notFound().build();
        }
        File file = new File(uploadFile.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(file);
        String ext = uploadFile.getFileType().toLowerCase();
        MediaType mediaType;
        switch (ext) {
            case "jpg":
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "pdf":
                mediaType = MediaType.APPLICATION_PDF;
                break;
            default:
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok().contentType(mediaType).body(resource);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        fileService.delete(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/file";
    }
}
