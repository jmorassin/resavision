package com.jmdev.resavision.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jmdev.resavision.domain.TypeSite;
import com.jmdev.resavision.service.TypeSiteService;
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
 * REST controller for managing TypeSite.
 */
@RestController
@RequestMapping("/api")
public class TypeSiteResource {

    private final Logger log = LoggerFactory.getLogger(TypeSiteResource.class);

    private static final String ENTITY_NAME = "typeSite";

    private final TypeSiteService typeSiteService;

    public TypeSiteResource(TypeSiteService typeSiteService) {
        this.typeSiteService = typeSiteService;
    }

    /**
     * POST  /type-sites : Create a new typeSite.
     *
     * @param typeSite the typeSite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeSite, or with status 400 (Bad Request) if the typeSite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-sites")
    @Timed
    public ResponseEntity<TypeSite> createTypeSite(@Valid @RequestBody TypeSite typeSite) throws URISyntaxException {
        log.debug("REST request to save TypeSite : {}", typeSite);
        if (typeSite.getId() != null) {
            throw new BadRequestAlertException("A new typeSite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeSite result = typeSiteService.save(typeSite);
        return ResponseEntity.created(new URI("/api/type-sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-sites : Updates an existing typeSite.
     *
     * @param typeSite the typeSite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeSite,
     * or with status 400 (Bad Request) if the typeSite is not valid,
     * or with status 500 (Internal Server Error) if the typeSite couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-sites")
    @Timed
    public ResponseEntity<TypeSite> updateTypeSite(@Valid @RequestBody TypeSite typeSite) throws URISyntaxException {
        log.debug("REST request to update TypeSite : {}", typeSite);
        if (typeSite.getId() == null) {
            return createTypeSite(typeSite);
        }
        TypeSite result = typeSiteService.save(typeSite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeSite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-sites : get all the typeSites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeSites in body
     */
    @GetMapping("/type-sites")
    @Timed
    public ResponseEntity<List<TypeSite>> getAllTypeSites(Pageable pageable) {
        log.debug("REST request to get a page of TypeSites");
        Page<TypeSite> page = typeSiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-sites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-sites/:id : get the "id" typeSite.
     *
     * @param id the id of the typeSite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeSite, or with status 404 (Not Found)
     */
    @GetMapping("/type-sites/{id}")
    @Timed
    public ResponseEntity<TypeSite> getTypeSite(@PathVariable Long id) {
        log.debug("REST request to get TypeSite : {}", id);
        TypeSite typeSite = typeSiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeSite));
    }

    /**
     * DELETE  /type-sites/:id : delete the "id" typeSite.
     *
     * @param id the id of the typeSite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-sites/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeSite(@PathVariable Long id) {
        log.debug("REST request to delete TypeSite : {}", id);
        typeSiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-sites?query=:query : search for the typeSite corresponding
     * to the query.
     *
     * @param query the query of the typeSite search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/type-sites")
    @Timed
    public ResponseEntity<List<TypeSite>> searchTypeSites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TypeSites for query {}", query);
        Page<TypeSite> page = typeSiteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-sites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
