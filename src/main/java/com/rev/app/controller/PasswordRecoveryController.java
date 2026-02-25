package com.rev.app.controller;

import com.rev.app.service.Interface.IPasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/password-recovery")
public class PasswordRecoveryController {

    @Autowired
    private IPasswordRecoveryService passwordRecoveryService;

    @GetMapping
    public String showRecoveryForm() {
        return "password-recovery"; // assuming a view named password-recovery.html exists
    }

    @PostMapping
    public String handleRecoveryRequest(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            // Future implementation assuming service handles sending the email
            // passwordRecoveryService.sendRecoveryEmail(email);
            redirectAttributes.addFlashAttribute("msg", "If the email exists, a recovery link has been sent.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to process recovery request.");
        }
        return "redirect:/password-recovery";
    }
}
