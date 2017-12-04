package com.jmdev.resavision.service;

import com.jmdev.resavision.domain.Responsable;
import com.jmdev.resavision.repository.ResponsableRepository;
import com.jmdev.resavision.repository.search.ResponsableSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Responsable.
 */
@Service
@Transactional
public class ResponsableService {

    private final Logger log = LoggerFactory.getLogger(ResponsableService.class);

    private final ResponsableRepository responsableRepository;

    private final ResponsableSearchRepository responsableSearchRepository;

    public ResponsableService(ResponsableRepository responsableRepository, ResponsableSearchRepository responsableSearchRepository) {
        this.responsableRepository = responsableRepository;
        this.responsableSearchRepository = responsableSearchRepository;
    }

    /**
     * Save a responsable.
     *
     * @param responsable the entity to save
     * @return the persisted entity
     */
    public Responsable save(Responsable responsable) {
        log.debug("Request to save Responsable : {}", responsable);
        Responsable result = responsableRepository.save(responsable);
        responsableSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the responsables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Responsable> findAll(Pageable pageable) {
        log.debug("Request to get all Responsables");
        return responsableRepository.findAll(pageable);
    }

    /**
     * Get one responsable by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Responsable findOne(Long id) {
        log.debug("Request to get Responsable : {}", id);
        return responsableRepository.findOne(id);
    }

    /**
     * Delete the responsable by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Responsable : {}", id);
        responsableRepository.delete(id);
        responsableSearchRepository.delete(id);
    }

    /**
     * Search for the responsable corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Responsable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Responsables for query {}", query);
        Page<Responsable> result = responsableSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
