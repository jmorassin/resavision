package com.jmdev.resavision.service;

import com.jmdev.resavision.domain.Boutique;
import com.jmdev.resavision.repository.BoutiqueRepository;
import com.jmdev.resavision.repository.search.BoutiqueSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Boutique.
 */
@Service
@Transactional
public class BoutiqueService {

    private final Logger log = LoggerFactory.getLogger(BoutiqueService.class);

    private final BoutiqueRepository boutiqueRepository;

    private final BoutiqueSearchRepository boutiqueSearchRepository;

    public BoutiqueService(BoutiqueRepository boutiqueRepository, BoutiqueSearchRepository boutiqueSearchRepository) {
        this.boutiqueRepository = boutiqueRepository;
        this.boutiqueSearchRepository = boutiqueSearchRepository;
    }

    /**
     * Save a boutique.
     *
     * @param boutique the entity to save
     * @return the persisted entity
     */
    public Boutique save(Boutique boutique) {
        log.debug("Request to save Boutique : {}", boutique);
        Boutique result = boutiqueRepository.save(boutique);
        boutiqueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the boutiques.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Boutique> findAll(Pageable pageable) {
        log.debug("Request to get all Boutiques");
        return boutiqueRepository.findAll(pageable);
    }


    /**
     *  get all the boutiques where Responsable is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Boutique> findAllWhereResponsableIsNull() {
        log.debug("Request to get all boutiques where Responsable is null");
        return StreamSupport
            .stream(boutiqueRepository.findAll().spliterator(), false)
            .filter(boutique -> boutique.getResponsable() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one boutique by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Boutique findOne(Long id) {
        log.debug("Request to get Boutique : {}", id);
        return boutiqueRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the boutique by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Boutique : {}", id);
        boutiqueRepository.delete(id);
        boutiqueSearchRepository.delete(id);
    }

    /**
     * Search for the boutique corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Boutique> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Boutiques for query {}", query);
        Page<Boutique> result = boutiqueSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
