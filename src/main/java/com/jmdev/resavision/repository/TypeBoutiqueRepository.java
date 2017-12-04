package com.jmdev.resavision.repository;

import com.jmdev.resavision.domain.TypeBoutique;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TypeBoutique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBoutiqueRepository extends JpaRepository<TypeBoutique, Long> {

}
