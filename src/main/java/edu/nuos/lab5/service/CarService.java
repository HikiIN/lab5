package edu.nuos.lab5.service;

import edu.nuos.lab5.entity.Car;
import edu.nuos.lab5.entity.Owner;
import edu.nuos.lab5.entity.ServiceItem;
import edu.nuos.lab5.repository.CarRepository;
import edu.nuos.lab5.repository.OwnerRepository;
import edu.nuos.lab5.repository.ServiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;
    private final ServiceItemRepository serviceItemRepository;

    public CarService(CarRepository carRepository,
                      OwnerRepository ownerRepository,
                      ServiceItemRepository serviceItemRepository) {
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    public List<Car> findAll() {
        return carRepository.findAllByOrderByIdAsc();
    }

    public List<Car> findByOwnerId(Long ownerId) {
        return carRepository.findByOwnerIdOrderByIdAsc(ownerId);
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    public Car save(Car car, Long ownerId) {
        if (car.getId() != null) {
            carRepository.findById(car.getId()).ifPresent(existing -> car.setServices(existing.getServices()));
        }
        if (ownerId != null) {
            Owner owner = ownerRepository.findById(ownerId).orElse(null);
            car.setOwner(owner);
        } else {
            car.setOwner(null);
        }
        return carRepository.save(car);
    }

    @Transactional
    public void deleteById(Long id) {
        carRepository.findById(id).ifPresent(car -> {
            car.getServices().clear();
            carRepository.delete(car);
        });
    }

    public List<ServiceItem> findServicesByCarId(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            return new ArrayList<>(optionalCar.get().getServices());
        }
        return Collections.emptyList();
    }

    public List<ServiceItem> findAvailableServices(Long carId) {
        return carRepository.findAvailableServices(carId);
    }

    @Transactional
    public void addServiceToCar(Long carId, Long serviceId) {
        carRepository.findById(carId).ifPresent(car -> {
            serviceItemRepository.findById(serviceId).ifPresent(service -> {
                car.getServices().add(service);
                carRepository.save(car);
            });
        });
    }

    @Transactional
    public void removeServiceFromCar(Long carId, Long serviceId) {
        carRepository.findById(carId).ifPresent(car -> {
            car.getServices().removeIf(service -> service.getId().equals(serviceId));
            carRepository.save(car);
        });
    }
}
