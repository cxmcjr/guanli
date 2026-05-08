package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/";
        }
        ra.addFlashAttribute("error", "用户名或密码错误");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String realName,
                           RedirectAttributes ra) {
        if (username.length() < 3 || password.length() < 6) {
            ra.addFlashAttribute("error", "用户名至少3位，密码至少6位");
            return "redirect:/register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        if (userService.register(user)) {
            ra.addFlashAttribute("msg", "注册成功，请登录");
            return "redirect:/login";
        }
        ra.addFlashAttribute("error", "用户名已存在");
        return "redirect:/register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin/users")
    public String userList(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/admin/users/edit")
    public String editUser(@RequestParam Integer id,
                           @RequestParam String role,
                           HttpSession session,
                           RedirectAttributes ra) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            return "redirect:/login";
        }
        User user = userService.findById(id);
        if (user != null) {
            user.setRole(role);
            userService.update(user);
            ra.addFlashAttribute("msg", "修改成功");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            return "redirect:/login";
        }
        userService.delete(id);
        ra.addFlashAttribute("msg", "删除成功");
        return "redirect:/admin/users";
    }
}
