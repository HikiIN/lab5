package edu.nuos.lab5.controller;

import edu.nuos.lab5.entity.Car;
import edu.nuos.lab5.entity.ServiceItem;
import edu.nuos.lab5.service.CarService;
import edu.nuos.lab5.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;
    private final OwnerService ownerService;

    public CarController(CarService carService, OwnerService ownerService) {
        this.carService = carService;
        this.ownerService = ownerService;
    }

    @GetMapping
    public String listCars(@RequestParam(required = false) Long ownerId, Model model) {
        if (!model.containsAttribute("car")) {
            Car defaultCar = new Car();
            defaultCar.setYear(2022);
            defaultCar.setMileage(35000);
            defaultCar.setPrice(17500.0);
            defaultCar.setInspectionDate(LocalDate.now());
            model.addAttribute("car", defaultCar);
        }
        List<Car> cars = (ownerId != null) ? carService.findByOwnerId(ownerId) : carService.findAll();
        model.addAttribute("cars", cars);
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("selectedOwnerId", ownerId);
        if (ownerId != null) {
            ownerService.findById(ownerId).ifPresent(o -> model.addAttribute("filterOwner", o));
        }
        model.addAttribute("editMode", false);
        return "cars";
    }

    @GetMapping("/edit/{id}")
    public String editCarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return carService.findById(id)
                .map(car -> {
                    model.addAttribute("car", car);
                    model.addAttribute("cars", carService.findAll());
                    model.addAttribute("owners", ownerService.findAll());
                    model.addAttribute("selectedOwnerId", car.getOwner() != null ? car.getOwner().getId() : null);
                    model.addAttribute("editMode", true);
                    return "cars";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Автомобіль не знайдено!");
                    return "redirect:/cars";
                });
    }

    @PostMapping("/save")
    public String saveCar(@ModelAttribute("car") Car car,
                          @RequestParam(required = false) Long ownerId,
                          RedirectAttributes redirectAttributes) {
        boolean isUpdate = (car.getId() != null);
        carService.save(car, ownerId);
        if (isUpdate) {
            redirectAttributes.addFlashAttribute("successMessage", "Дані автомобіля успішно оновлено!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Автомобіль успішно додано до БД!");
        }
        return "redirect:/cars";
    }

    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        carService.deleteById(id);
        redirectAttributes.addFlashAttribute("infoMessage", "Запис автомобіля успішно видалено з БД!");
        return "redirect:/cars";
    }

    @GetMapping("/{id}/services")
    public String carServices(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return carService.findById(id)
                .map(car -> {
                    List<ServiceItem> assigned = carService.findServicesByCarId(id);
                    List<ServiceItem> available = carService.findAvailableServices(id);
                    double totalCost = assigned.stream().mapToDouble(ServiceItem::getCost).sum();
                    model.addAttribute("car", car);
                    model.addAttribute("services", assigned);
                    model.addAttribute("available", available);
                    model.addAttribute("totalCost", totalCost);
                    return "car_services";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Автомобіль не знайдено!");
                    return "redirect:/cars";
                });
    }

    @PostMapping("/{carId}/services/add/{serviceId}")
    public String addServiceToCar(@PathVariable Long carId,
                                  @PathVariable Long serviceId,
                                  RedirectAttributes redirectAttributes) {
        carService.addServiceToCar(carId, serviceId);
        redirectAttributes.addFlashAttribute("successMessage", "Сервісну послугу призначено автомобілю!");
        return "redirect:/cars/" + carId + "/services";
    }

    @PostMapping("/{carId}/services/remove/{serviceId}")
    public String removeServiceFromCar(@PathVariable Long carId,
                                       @PathVariable Long serviceId,
                                       RedirectAttributes redirectAttributes) {
        carService.removeServiceFromCar(carId, serviceId);
        redirectAttributes.addFlashAttribute("infoMessage", "Сервісну послугу відкріплено від автомобіля!");
        return "redirect:/cars/" + carId + "/services";
    }
}
