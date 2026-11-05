package edu.nuos.lab5.repository;

import edu.nuos.lab5.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    List<Owner> findAllByOrderByIdAsc();
}
