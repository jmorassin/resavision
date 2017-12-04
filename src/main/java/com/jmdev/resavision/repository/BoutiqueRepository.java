package com.jmdev.resavision.repository;

import com.jmdev.resavision.domain.Boutique;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Boutique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoutiqueRepository extends JpaRepository<Boutique, Long> {
    @Query("select distinct boutique from Boutique boutique left join fetch boutique.typeBoutiques")
    List<Boutique> findAllWithEagerRelationships();

    @Query("select boutique from Boutique boutique left join fetch boutique.typeBoutiques where boutique.id =:id")
    Boutique findOneWithEagerRelationships(@Param("id") Long id);

}
