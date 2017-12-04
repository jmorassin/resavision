package com.jmdev.resavision.repository;

import com.jmdev.resavision.domain.Ville;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ville entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {

}
