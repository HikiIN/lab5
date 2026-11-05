package edu.nuos.lab5.controller;

import edu.nuos.lab5.entity.Owner;
import edu.nuos.lab5.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public String listOwners(Model model) {
        if (!model.containsAttribute("owner")) {
            model.addAttribute("owner", new Owner());
        }
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("editMode", false);
        return "owners";
    }

    @GetMapping("/edit/{id}")
    public String editOwner(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return ownerService.findById(id)
                .map(owner -> {
                    model.addAttribute("owner", owner);
                    model.addAttribute("owners", ownerService.findAll());
                    model.addAttribute("editMode", true);
                    return "owners";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Власника не знайдено!");
                    return "redirect:/owners";
                });
    }

    @PostMapping("/save")
    public String saveOwner(@ModelAttribute("owner") Owner owner, RedirectAttributes redirectAttributes) {
        boolean isUpdate = (owner.getId() != null);
        ownerService.save(owner);
        if (isUpdate) {
            redirectAttributes.addFlashAttribute("successMessage", "Дані власника успішно оновлено!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Нового власника успішно додано!");
        }
        return "redirect:/owners";
    }

    @PostMapping("/delete/{id}")
    public String deleteOwner(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ownerService.deleteById(id);
        redirectAttributes.addFlashAttribute("infoMessage", "Власника видалено з бази даних!");
        return "redirect:/owners";
    }
}
