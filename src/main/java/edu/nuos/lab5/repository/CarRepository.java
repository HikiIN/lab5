package edu.nuos.lab5.repository;

import edu.nuos.lab5.entity.Car;
import edu.nuos.lab5.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findAllByOrderByIdAsc();

    List<Car> findByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("select s from ServiceItem s where not exists (select 1 from Car c where s member of c.services and c.id = ?1) order by s.id asc")
    List<ServiceItem> findAvailableServices(Long carId);
}
