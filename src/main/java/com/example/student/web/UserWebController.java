package com.example.student.web;

import com.example.student.dto.user.UserRequest;
import com.example.student.exception.EmailAlreadyExistsException;
import com.example.student.model.Role;
import com.example.student.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class UserWebController {
    private final UserService userService;

    @ModelAttribute("allRoles")
    public Role[] allRoles() {
        return Role.values();
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new UserRequest(null, null, new HashSet<>()));
        return "users/form";
    }

    @PostMapping
    public String create(@ModelAttribute("user") UserRequest request, Model model, RedirectAttributes redirect) {
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            model.addAttribute("error", "Email и пароль обязательны");
            return "users/form";
        }
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            model.addAttribute("error", "Выберите хотя бы одну роль");
            return "users/form";
        }
        try {
            userService.save(request);
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "users/form";
        }
        redirect.addFlashAttribute("success", "Пользователь создан");
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        userService.deleteById(id);
        redirect.addFlashAttribute("success", "Пользователь удалён");
        return "redirect:/users";
    }
}
