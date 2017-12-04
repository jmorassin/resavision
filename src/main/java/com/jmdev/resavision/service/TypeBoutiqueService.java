package com.jmdev.resavision.service;

import com.jmdev.resavision.domain.TypeBoutique;
import com.jmdev.resavision.repository.TypeBoutiqueRepository;
import com.jmdev.resavision.repository.search.TypeBoutiqueSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TypeBoutique.
 */
@Service
@Transactional
public class TypeBoutiqueService {

    private final Logger log = LoggerFactory.getLogger(TypeBoutiqueService.class);

    private final TypeBoutiqueRepository typeBoutiqueRepository;

    private final TypeBoutiqueSearchRepository typeBoutiqueSearchRepository;

    public TypeBoutiqueService(TypeBoutiqueRepository typeBoutiqueRepository, TypeBoutiqueSearchRepository typeBoutiqueSearchRepository) {
        this.typeBoutiqueRepository = typeBoutiqueRepository;
        this.typeBoutiqueSearchRepository = typeBoutiqueSearchRepository;
    }

    /**
     * Save a typeBoutique.
     *
     * @param typeBoutique the entity to save
     * @return the persisted entity
     */
    public TypeBoutique save(TypeBoutique typeBoutique) {
        log.debug("Request to save TypeBoutique : {}", typeBoutique);
        TypeBoutique result = typeBoutiqueRepository.save(typeBoutique);
        typeBoutiqueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the typeBoutiques.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeBoutique> findAll(Pageable pageable) {
        log.debug("Request to get all TypeBoutiques");
        return typeBoutiqueRepository.findAll(pageable);
    }

    /**
     * Get one typeBoutique by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TypeBoutique findOne(Long id) {
        log.debug("Request to get TypeBoutique : {}", id);
        return typeBoutiqueRepository.findOne(id);
    }

    /**
     * Delete the typeBoutique by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeBoutique : {}", id);
        typeBoutiqueRepository.delete(id);
        typeBoutiqueSearchRepository.delete(id);
    }

    /**
     * Search for the typeBoutique corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeBoutique> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeBoutiques for query {}", query);
        Page<TypeBoutique> result = typeBoutiqueSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
