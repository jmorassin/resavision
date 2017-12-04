package com.jmdev.resavision.service;

import com.jmdev.resavision.domain.TypeSite;
import com.jmdev.resavision.repository.TypeSiteRepository;
import com.jmdev.resavision.repository.search.TypeSiteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TypeSite.
 */
@Service
@Transactional
public class TypeSiteService {

    private final Logger log = LoggerFactory.getLogger(TypeSiteService.class);

    private final TypeSiteRepository typeSiteRepository;

    private final TypeSiteSearchRepository typeSiteSearchRepository;

    public TypeSiteService(TypeSiteRepository typeSiteRepository, TypeSiteSearchRepository typeSiteSearchRepository) {
        this.typeSiteRepository = typeSiteRepository;
        this.typeSiteSearchRepository = typeSiteSearchRepository;
    }

    /**
     * Save a typeSite.
     *
     * @param typeSite the entity to save
     * @return the persisted entity
     */
    public TypeSite save(TypeSite typeSite) {
        log.debug("Request to save TypeSite : {}", typeSite);
        TypeSite result = typeSiteRepository.save(typeSite);
        typeSiteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the typeSites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeSite> findAll(Pageable pageable) {
        log.debug("Request to get all TypeSites");
        return typeSiteRepository.findAll(pageable);
    }

    /**
     * Get one typeSite by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TypeSite findOne(Long id) {
        log.debug("Request to get TypeSite : {}", id);
        return typeSiteRepository.findOne(id);
    }

    /**
     * Delete the typeSite by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeSite : {}", id);
        typeSiteRepository.delete(id);
        typeSiteSearchRepository.delete(id);
    }

    /**
     * Search for the typeSite corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeSite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeSites for query {}", query);
        Page<TypeSite> result = typeSiteSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
