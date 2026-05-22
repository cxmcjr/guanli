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
import java.util.List;
import java.util.stream.Collectors;

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
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<SoftwareCopyright> all = copyrightService.findAll();
        if ("student".equals(user.getRole())) {
            String name = user.getRealName();
            all = all.stream()
                    .filter(s -> name.equals(s.getSubmitter()) || name.equals(s.getAuthor()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("list", all);
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
    public String editPage(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes ra) {
        SoftwareCopyright s = copyrightService.findById(id);
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole()) && !"pending".equals(s.getStatus())) {
            ra.addFlashAttribute("msg", "该软著已审核，无法修改");
            return "redirect:/software";
        }
        model.addAttribute("s", s);
        return "software/edit";
    }

    @PostMapping("/edit")
    public String edit(SoftwareCopyright copyright, HttpSession session, RedirectAttributes ra) {
        SoftwareCopyright existing = copyrightService.findById(copyright.getId());
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole()) && !"pending".equals(existing.getStatus())) {
            ra.addFlashAttribute("msg", "该软著已审核，无法修改");
            return "redirect:/software";
        }
        copyrightService.update(copyright);
        ra.addFlashAttribute("msg", "修改成功");
        return "redirect:/software";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        SoftwareCopyright s = copyrightService.findById(id);
        User user = (User) session.getAttribute("user");
        if ("student".equals(user.getRole()) && !"pending".equals(s.getStatus())) {
            ra.addFlashAttribute("msg", "该软著已审核，无法删除");
            return "redirect:/software";
        }
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
