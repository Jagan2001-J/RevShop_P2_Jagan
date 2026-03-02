package com.rev.app.controller;

import com.rev.app.service.Interface.IPasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/forgot-password")
@Slf4j
public class PasswordRecoveryController {

    @Autowired
    private IPasswordRecoveryService passwordRecoveryService;

    @GetMapping
    public String showRecoveryForm() {
        log.info("Accessed password recovery form.");
        return "forgot-password";
    }

    @PostMapping
    public String handleRecoveryRequest(@RequestParam String email, RedirectAttributes redirectAttributes) {
        log.info("Password recovery requested for email: {}", email);
        try {
            // Future implementation assuming service handles sending the email
            // passwordRecoveryService.sendRecoveryEmail(email);
            log.debug("Recovery email placeholder executed for {}", email);
            redirectAttributes.addFlashAttribute("msg", "If the email exists, a recovery link has been sent.");
        } catch (Exception e) {
            log.error("Failed to process password recovery for {}: {}", email, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to process recovery request.");
        }
        return "redirect:/forgot-password";
    }
}
