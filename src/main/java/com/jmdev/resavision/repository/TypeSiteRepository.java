package com.jmdev.resavision.repository;

import com.jmdev.resavision.domain.TypeSite;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TypeSite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSiteRepository extends JpaRepository<TypeSite, Long> {

}
