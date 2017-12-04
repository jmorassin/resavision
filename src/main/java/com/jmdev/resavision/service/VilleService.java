package com.jmdev.resavision.service;

import com.jmdev.resavision.domain.Ville;
import com.jmdev.resavision.repository.VilleRepository;
import com.jmdev.resavision.repository.search.VilleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ville.
 */
@Service
@Transactional
public class VilleService {

    private final Logger log = LoggerFactory.getLogger(VilleService.class);

    private final VilleRepository villeRepository;

    private final VilleSearchRepository villeSearchRepository;

    public VilleService(VilleRepository villeRepository, VilleSearchRepository villeSearchRepository) {
        this.villeRepository = villeRepository;
        this.villeSearchRepository = villeSearchRepository;
    }

    /**
     * Save a ville.
     *
     * @param ville the entity to save
     * @return the persisted entity
     */
    public Ville save(Ville ville) {
        log.debug("Request to save Ville : {}", ville);
        Ville result = villeRepository.save(ville);
        villeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the villes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ville> findAll(Pageable pageable) {
        log.debug("Request to get all Villes");
        return villeRepository.findAll(pageable);
    }

    /**
     * Get one ville by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Ville findOne(Long id) {
        log.debug("Request to get Ville : {}", id);
        return villeRepository.findOne(id);
    }

    /**
     * Delete the ville by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ville : {}", id);
        villeRepository.delete(id);
        villeSearchRepository.delete(id);
    }

    /**
     * Search for the ville corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ville> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Villes for query {}", query);
        Page<Ville> result = villeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
