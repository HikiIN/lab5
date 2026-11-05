package edu.nuos.lab5.controller;

import edu.nuos.lab5.entity.ServiceItem;
import edu.nuos.lab5.service.ServiceItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/services")
public class ServiceItemController {

    private final ServiceItemService serviceItemService;

    public ServiceItemController(ServiceItemService serviceItemService) {
        this.serviceItemService = serviceItemService;
    }

    @GetMapping
    public String listServices(Model model) {
        if (!model.containsAttribute("serviceItem")) {
            ServiceItem defaultService = new ServiceItem();
            defaultService.setCost(50.0);
            model.addAttribute("serviceItem", defaultService);
        }
        model.addAttribute("services", serviceItemService.findAll());
        model.addAttribute("editMode", false);
        return "services";
    }

    @GetMapping("/edit/{id}")
    public String editService(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return serviceItemService.findById(id)
                .map(serviceItem -> {
                    model.addAttribute("serviceItem", serviceItem);
                    model.addAttribute("services", serviceItemService.findAll());
                    model.addAttribute("editMode", true);
                    return "services";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Послугу не знайдено!");
                    return "redirect:/services";
                });
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute("serviceItem") ServiceItem serviceItem, RedirectAttributes redirectAttributes) {
        boolean isUpdate = (serviceItem.getId() != null);
        serviceItemService.save(serviceItem);
        if (isUpdate) {
            redirectAttributes.addFlashAttribute("successMessage", "Сервісну послугу успішно оновлено!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Нову сервісну послугу успішно додано!");
        }
        return "redirect:/services";
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        serviceItemService.deleteById(id);
        redirectAttributes.addFlashAttribute("infoMessage", "Сервісну послугу видалено з бази даних!");
        return "redirect:/services";
    }
}
