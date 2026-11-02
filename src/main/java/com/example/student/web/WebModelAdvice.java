package com.example.student.web;

import com.example.student.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.example.student.web")
@Slf4j
public class WebModelAdvice {

    @ModelAttribute
    public void addCurrentUser(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("currentUser", authentication.getName());
        model.addAttribute("isSuperAdmin", roles.contains("ROLE_SUPER_ADMIN"));
        model.addAttribute("isAdmin", roles.contains("ROLE_ADMIN"));
        model.addAttribute("canViewStudents", roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TEACHER"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(EntityNotFoundException e, Model model) {
        log.error(e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "not-found";
    }
}
