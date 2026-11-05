package edu.nuos.lab5.service;

import edu.nuos.lab5.entity.Car;
import edu.nuos.lab5.entity.ServiceItem;
import edu.nuos.lab5.repository.CarRepository;
import edu.nuos.lab5.repository.ServiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceItemService {

    private final ServiceItemRepository serviceItemRepository;
    private final CarRepository carRepository;

    public ServiceItemService(ServiceItemRepository serviceItemRepository, CarRepository carRepository) {
        this.serviceItemRepository = serviceItemRepository;
        this.carRepository = carRepository;
    }

    public List<ServiceItem> findAll() {
        return serviceItemRepository.findAllByOrderByIdAsc();
    }

    public Optional<ServiceItem> findById(Long id) {
        return serviceItemRepository.findById(id);
    }

    public ServiceItem save(ServiceItem serviceItem) {
        return serviceItemRepository.save(serviceItem);
    }

    @Transactional
    public void deleteById(Long id) {
        serviceItemRepository.findById(id).ifPresent(serviceItem -> {
            for (Car car : serviceItem.getCars()) {
                car.getServices().remove(serviceItem);
                carRepository.save(car);
            }
            serviceItemRepository.delete(serviceItem);
        });
    }
}
