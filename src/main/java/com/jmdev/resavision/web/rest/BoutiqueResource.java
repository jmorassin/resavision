package com.jmdev.resavision.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jmdev.resavision.domain.Boutique;
import com.jmdev.resavision.service.BoutiqueService;
import com.jmdev.resavision.web.rest.errors.BadRequestAlertException;
import com.jmdev.resavision.web.rest.util.HeaderUtil;
import com.jmdev.resavision.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Boutique.
 */
@RestController
@RequestMapping("/api")
public class BoutiqueResource {

    private final Logger log = LoggerFactory.getLogger(BoutiqueResource.class);

    private static final String ENTITY_NAME = "boutique";

    private final BoutiqueService boutiqueService;

    public BoutiqueResource(BoutiqueService boutiqueService) {
        this.boutiqueService = boutiqueService;
    }

    /**
     * POST  /boutiques : Create a new boutique.
     *
     * @param boutique the boutique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boutique, or with status 400 (Bad Request) if the boutique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/boutiques")
    @Timed
    public ResponseEntity<Boutique> createBoutique(@Valid @RequestBody Boutique boutique) throws URISyntaxException {
        log.debug("REST request to save Boutique : {}", boutique);
        if (boutique.getId() != null) {
            throw new BadRequestAlertException("A new boutique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Boutique result = boutiqueService.save(boutique);
        return ResponseEntity.created(new URI("/api/boutiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boutiques : Updates an existing boutique.
     *
     * @param boutique the boutique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boutique,
     * or with status 400 (Bad Request) if the boutique is not valid,
     * or with status 500 (Internal Server Error) if the boutique couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/boutiques")
    @Timed
    public ResponseEntity<Boutique> updateBoutique(@Valid @RequestBody Boutique boutique) throws URISyntaxException {
        log.debug("REST request to update Boutique : {}", boutique);
        if (boutique.getId() == null) {
            return createBoutique(boutique);
        }
        Boutique result = boutiqueService.save(boutique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boutique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boutiques : get all the boutiques.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of boutiques in body
     */
    @GetMapping("/boutiques")
    @Timed
    public ResponseEntity<List<Boutique>> getAllBoutiques(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("responsable-is-null".equals(filter)) {
            log.debug("REST request to get all Boutiques where responsable is null");
            return new ResponseEntity<>(boutiqueService.findAllWhereResponsableIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Boutiques");
        Page<Boutique> page = boutiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/boutiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /boutiques/:id : get the "id" boutique.
     *
     * @param id the id of the boutique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boutique, or with status 404 (Not Found)
     */
    @GetMapping("/boutiques/{id}")
    @Timed
    public ResponseEntity<Boutique> getBoutique(@PathVariable Long id) {
        log.debug("REST request to get Boutique : {}", id);
        Boutique boutique = boutiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(boutique));
    }

    /**
     * DELETE  /boutiques/:id : delete the "id" boutique.
     *
     * @param id the id of the boutique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/boutiques/{id}")
    @Timed
    public ResponseEntity<Void> deleteBoutique(@PathVariable Long id) {
        log.debug("REST request to delete Boutique : {}", id);
        boutiqueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/boutiques?query=:query : search for the boutique corresponding
     * to the query.
     *
     * @param query the query of the boutique search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/boutiques")
    @Timed
    public ResponseEntity<List<Boutique>> searchBoutiques(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Boutiques for query {}", query);
        Page<Boutique> page = boutiqueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/boutiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
