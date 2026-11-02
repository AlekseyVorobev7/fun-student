package com.example.student.web;

import com.example.student.dto.StudentFullResponse;
import com.example.student.dto.StudentRequest;
import com.example.student.dto.StudentUpdateRequest;
import com.example.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentWebController {
    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public String list(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "students/view";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("student", new StudentRequest());
        return "students/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@ModelAttribute("student") StudentRequest request, RedirectAttributes redirect) {
        Long id = studentService.save(request).getId();
        redirect.addFlashAttribute("success", "Студент добавлен");
        return "redirect:/students/" + id;
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        StudentFullResponse student = studentService.findById(id);
        model.addAttribute("student", student);
        model.addAttribute("form", new StudentUpdateRequest(student.getPhoneNumber(), student.getEmail()));
        return "students/edit";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @ModelAttribute("form") StudentUpdateRequest request,
                         RedirectAttributes redirect) {
        studentService.update(id, request);
        redirect.addFlashAttribute("success", "Изменения сохранены");
        return "redirect:/students/" + id;
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        studentService.deleteById(id);
        redirect.addFlashAttribute("success", "Студент удалён");
        return "redirect:/students";
    }
}
