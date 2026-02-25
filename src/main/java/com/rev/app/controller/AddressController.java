package com.rev.app.controller;

import com.rev.app.entity.Address;
import com.rev.app.entity.User;
import com.rev.app.service.Interface.IAddressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @PostMapping("/add")
    public String addAddress(@ModelAttribute Address address, HttpSession session,
            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            address.setUser(user);
            addressService.addAddress(address);
            redirectAttributes.addFlashAttribute("msg", "Address added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding address: " + e.getMessage());
        }

        // This redirects back to the referring page (which is usually the checkout
        // page)
        return "redirect:/checkout";
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Address address = addressService.getAddressById(id);
            if (address != null && address.getUser().getId().equals(user.getId())) {
                addressService.deleteAddress(id);
                redirectAttributes.addFlashAttribute("msg", "Address removed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Address not found or unauthorized.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting address.");
        }

        return "redirect:/checkout";
    }
}
