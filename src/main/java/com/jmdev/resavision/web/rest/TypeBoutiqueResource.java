package com.jmdev.resavision.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jmdev.resavision.domain.TypeBoutique;
import com.jmdev.resavision.service.TypeBoutiqueService;
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
 * REST controller for managing TypeBoutique.
 */
@RestController
@RequestMapping("/api")
public class TypeBoutiqueResource {

    private final Logger log = LoggerFactory.getLogger(TypeBoutiqueResource.class);

    private static final String ENTITY_NAME = "typeBoutique";

    private final TypeBoutiqueService typeBoutiqueService;

    public TypeBoutiqueResource(TypeBoutiqueService typeBoutiqueService) {
        this.typeBoutiqueService = typeBoutiqueService;
    }

    /**
     * POST  /type-boutiques : Create a new typeBoutique.
     *
     * @param typeBoutique the typeBoutique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeBoutique, or with status 400 (Bad Request) if the typeBoutique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-boutiques")
    @Timed
    public ResponseEntity<TypeBoutique> createTypeBoutique(@Valid @RequestBody TypeBoutique typeBoutique) throws URISyntaxException {
        log.debug("REST request to save TypeBoutique : {}", typeBoutique);
        if (typeBoutique.getId() != null) {
            throw new BadRequestAlertException("A new typeBoutique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeBoutique result = typeBoutiqueService.save(typeBoutique);
        return ResponseEntity.created(new URI("/api/type-boutiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-boutiques : Updates an existing typeBoutique.
     *
     * @param typeBoutique the typeBoutique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeBoutique,
     * or with status 400 (Bad Request) if the typeBoutique is not valid,
     * or with status 500 (Internal Server Error) if the typeBoutique couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-boutiques")
    @Timed
    public ResponseEntity<TypeBoutique> updateTypeBoutique(@Valid @RequestBody TypeBoutique typeBoutique) throws URISyntaxException {
        log.debug("REST request to update TypeBoutique : {}", typeBoutique);
        if (typeBoutique.getId() == null) {
            return createTypeBoutique(typeBoutique);
        }
        TypeBoutique result = typeBoutiqueService.save(typeBoutique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeBoutique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-boutiques : get all the typeBoutiques.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeBoutiques in body
     */
    @GetMapping("/type-boutiques")
    @Timed
    public ResponseEntity<List<TypeBoutique>> getAllTypeBoutiques(Pageable pageable) {
        log.debug("REST request to get a page of TypeBoutiques");
        Page<TypeBoutique> page = typeBoutiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-boutiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-boutiques/:id : get the "id" typeBoutique.
     *
     * @param id the id of the typeBoutique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeBoutique, or with status 404 (Not Found)
     */
    @GetMapping("/type-boutiques/{id}")
    @Timed
    public ResponseEntity<TypeBoutique> getTypeBoutique(@PathVariable Long id) {
        log.debug("REST request to get TypeBoutique : {}", id);
        TypeBoutique typeBoutique = typeBoutiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeBoutique));
    }

    /**
     * DELETE  /type-boutiques/:id : delete the "id" typeBoutique.
     *
     * @param id the id of the typeBoutique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-boutiques/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeBoutique(@PathVariable Long id) {
        log.debug("REST request to delete TypeBoutique : {}", id);
        typeBoutiqueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-boutiques?query=:query : search for the typeBoutique corresponding
     * to the query.
     *
     * @param query the query of the typeBoutique search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/type-boutiques")
    @Timed
    public ResponseEntity<List<TypeBoutique>> searchTypeBoutiques(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TypeBoutiques for query {}", query);
        Page<TypeBoutique> page = typeBoutiqueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-boutiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
