package edu.nuos.lab5.service;

import edu.nuos.lab5.entity.Car;
import edu.nuos.lab5.entity.Owner;
import edu.nuos.lab5.repository.CarRepository;
import edu.nuos.lab5.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final CarRepository carRepository;

    public OwnerService(OwnerRepository ownerRepository, CarRepository carRepository) {
        this.ownerRepository = ownerRepository;
        this.carRepository = carRepository;
    }

    public List<Owner> findAll() {
        return ownerRepository.findAllByOrderByIdAsc();
    }

    public Optional<Owner> findById(Long id) {
        return ownerRepository.findById(id);
    }

    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Transactional
    public void deleteById(Long id) {
        List<Car> cars = carRepository.findByOwnerIdOrderByIdAsc(id);
        for (Car car : cars) {
            car.setOwner(null);
            carRepository.save(car);
        }
        ownerRepository.deleteById(id);
    }
}
