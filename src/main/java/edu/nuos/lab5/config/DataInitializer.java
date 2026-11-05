package edu.nuos.lab5.config;

import edu.nuos.lab5.entity.Car;
import edu.nuos.lab5.entity.Owner;
import edu.nuos.lab5.entity.ServiceItem;
import edu.nuos.lab5.repository.CarRepository;
import edu.nuos.lab5.repository.OwnerRepository;
import edu.nuos.lab5.repository.ServiceItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final CarRepository carRepository;

    public DataInitializer(OwnerRepository ownerRepository,
                           ServiceItemRepository serviceItemRepository,
                           CarRepository carRepository) {
        this.ownerRepository = ownerRepository;
        this.serviceItemRepository = serviceItemRepository;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (ownerRepository.count() > 0 || carRepository.count() > 0) {
            return;
        }

        Owner owner1 = new Owner("Коваленко Олександр Петрович", "+380501234567", "Миколаїв");
        Owner owner2 = new Owner("Мельник Ірина Василівна", "+380679876543", "Одеса");
        Owner owner3 = new Owner("Шевченко Дмитро Ігорович", "+380631112233", "Київ");
        ownerRepository.saveAll(List.of(owner1, owner2, owner3));

        ServiceItem s1 = new ServiceItem("Заміна мастила та фільтрів", "Планова заміна моторного мастила 5W-30 та фільтрів", 85.0);
        ServiceItem s2 = new ServiceItem("Комп'ютерна діагностика", "Перевірка електронних блоків керування та датчиків", 40.0);
        ServiceItem s3 = new ServiceItem("Шиномонтаж і балансування", "Сезонна заміна гуми та балансування 4 коліс", 55.0);
        ServiceItem s4 = new ServiceItem("Регулювання розвал-сходження", "3D-стенд регулювання кутів встановлення коліс", 60.0);
        serviceItemRepository.saveAll(List.of(s1, s2, s3, s4));

        Car car1 = new Car("Toyota Camry", "AA1234EE", 2020, 62000, 18500.0, LocalDate.of(2026, 10, 28), owner1);
        car1.getServices().add(s1);
        car1.getServices().add(s2);

        Car car2 = new Car("Nissan Leaf", "BE4321TT", 2022, 35000, 16200.0, LocalDate.of(2026, 10, 21), owner1);
        car2.getServices().add(s2);
        car2.getServices().add(s3);

        Car car3 = new Car("Volkswagen Passat", "KA7788CI", 2019, 94000, 15800.0, LocalDate.of(2026, 11, 2), owner2);
        car3.getServices().add(s1);
        car3.getServices().add(s4);

        carRepository.saveAll(List.of(car1, car2, car3));
    }
}
