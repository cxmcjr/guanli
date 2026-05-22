package com.example.controller;

import com.example.entity.SoftwareCopyright;
import com.example.entity.User;
import com.example.service.SoftwareCopyrightService;
import com.example.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/software")
public class SoftwareCopyrightController {
    private final SoftwareCopyrightService copyrightService;
    private final FileService fileService;

    public SoftwareCopyrightController(SoftwareCopyrightService copyrightService, FileService fileService) {
        this.copyrightService = copyrightService;
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", copyrightService.findAll());
        return "software/list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "software/add";
    }

    @PostMapping("/add")
    public String add(SoftwareCopyright copyright,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session,
            RedirectAttributes ra) {
        User user = (User) session.getAttribute("user");
        copyright.setSubmitter(user.getRealName());
        int id = copyrightService.add(copyright);
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        fileService.upload(file, "software", "software", id);
                    } catch (IOException e) {
                        ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                        return "redirect:/software";
                    }
                }
            }
        }
        ra.addFlashAttribute("msg", "提交成功");
        return "redirect:/software";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        model.addAttribute("s", copyrightService.findById(id));
        return "software/edit";
    }

    @PostMapping("/edit")
    public String edit(SoftwareCopyright copyright, RedirectAttributes ra) {
        copyrightService.update(copyright);
        ra.addFlashAttribute("msg", "修改成功");
        return "redirect:/software";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        copyrightService.delete(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/software";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("s", copyrightService.findById(id));
        model.addAttribute("files", copyrightService.getFiles(id));
        model.addAttribute("reviews", copyrightService.getReviewRecords(id));
        return "software/detail";
    }

    @PostMapping("/upload/{id}")
    public String uploadFile(@PathVariable Integer id,
            @RequestParam("files") MultipartFile[] files,
            RedirectAttributes ra) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    fileService.upload(file, "software", "software", id);
                } catch (IOException e) {
                    ra.addFlashAttribute("msg", "文件上传失败: " + e.getMessage());
                    return "redirect:/software/detail/" + id;
                }
            }
        }
        ra.addFlashAttribute("msg", "上传成功");
        return "redirect:/software/detail/" + id;
    }

    @PostMapping("/review/{id}")
    public String review(@PathVariable Integer id,
            @RequestParam String reviewer,
            @RequestParam String reviewLevel,
            @RequestParam String status,
            @RequestParam String opinion,
            RedirectAttributes ra) {
        copyrightService.review(id, reviewer, reviewLevel, status, opinion);
        ra.addFlashAttribute("msg", "审核完成");
        return "redirect:/software/detail/" + id;
    }
}
