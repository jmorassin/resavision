package com.jmdev.resavision.web.rest;

import com.jmdev.resavision.ResavisionApp;

import com.jmdev.resavision.domain.TypeSite;
import com.jmdev.resavision.repository.TypeSiteRepository;
import com.jmdev.resavision.service.TypeSiteService;
import com.jmdev.resavision.repository.search.TypeSiteSearchRepository;
import com.jmdev.resavision.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jmdev.resavision.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TypeSiteResource REST controller.
 *
 * @see TypeSiteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResavisionApp.class)
public class TypeSiteResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private TypeSiteRepository typeSiteRepository;

    @Autowired
    private TypeSiteService typeSiteService;

    @Autowired
    private TypeSiteSearchRepository typeSiteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeSiteMockMvc;

    private TypeSite typeSite;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeSiteResource typeSiteResource = new TypeSiteResource(typeSiteService);
        this.restTypeSiteMockMvc = MockMvcBuilders.standaloneSetup(typeSiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSite createEntity(EntityManager em) {
        TypeSite typeSite = new TypeSite()
            .nom(DEFAULT_NOM);
        return typeSite;
    }

    @Before
    public void initTest() {
        typeSiteSearchRepository.deleteAll();
        typeSite = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeSite() throws Exception {
        int databaseSizeBeforeCreate = typeSiteRepository.findAll().size();

        // Create the TypeSite
        restTypeSiteMockMvc.perform(post("/api/type-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSite)))
            .andExpect(status().isCreated());

        // Validate the TypeSite in the database
        List<TypeSite> typeSiteList = typeSiteRepository.findAll();
        assertThat(typeSiteList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSite testTypeSite = typeSiteList.get(typeSiteList.size() - 1);
        assertThat(testTypeSite.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the TypeSite in Elasticsearch
        TypeSite typeSiteEs = typeSiteSearchRepository.findOne(testTypeSite.getId());
        assertThat(typeSiteEs).isEqualToComparingFieldByField(testTypeSite);
    }

    @Test
    @Transactional
    public void createTypeSiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeSiteRepository.findAll().size();

        // Create the TypeSite with an existing ID
        typeSite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeSiteMockMvc.perform(post("/api/type-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSite)))
            .andExpect(status().isBadRequest());

        // Validate the TypeSite in the database
        List<TypeSite> typeSiteList = typeSiteRepository.findAll();
        assertThat(typeSiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeSiteRepository.findAll().size();
        // set the field null
        typeSite.setNom(null);

        // Create the TypeSite, which fails.

        restTypeSiteMockMvc.perform(post("/api/type-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSite)))
            .andExpect(status().isBadRequest());

        List<TypeSite> typeSiteList = typeSiteRepository.findAll();
        assertThat(typeSiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeSites() throws Exception {
        // Initialize the database
        typeSiteRepository.saveAndFlush(typeSite);

        // Get all the typeSiteList
        restTypeSiteMockMvc.perform(get("/api/type-sites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getTypeSite() throws Exception {
        // Initialize the database
        typeSiteRepository.saveAndFlush(typeSite);

        // Get the typeSite
        restTypeSiteMockMvc.perform(get("/api/type-sites/{id}", typeSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeSite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeSite() throws Exception {
        // Get the typeSite
        restTypeSiteMockMvc.perform(get("/api/type-sites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeSite() throws Exception {
        // Initialize the database
        typeSiteService.save(typeSite);

        int databaseSizeBeforeUpdate = typeSiteRepository.findAll().size();

        // Update the typeSite
        TypeSite updatedTypeSite = typeSiteRepository.findOne(typeSite.getId());
        // Disconnect from session so that the updates on updatedTypeSite are not directly saved in db
        em.detach(updatedTypeSite);
        updatedTypeSite
            .nom(UPDATED_NOM);

        restTypeSiteMockMvc.perform(put("/api/type-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeSite)))
            .andExpect(status().isOk());

        // Validate the TypeSite in the database
        List<TypeSite> typeSiteList = typeSiteRepository.findAll();
        assertThat(typeSiteList).hasSize(databaseSizeBeforeUpdate);
        TypeSite testTypeSite = typeSiteList.get(typeSiteList.size() - 1);
        assertThat(testTypeSite.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the TypeSite in Elasticsearch
        TypeSite typeSiteEs = typeSiteSearchRepository.findOne(testTypeSite.getId());
        assertThat(typeSiteEs).isEqualToComparingFieldByField(testTypeSite);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeSite() throws Exception {
        int databaseSizeBeforeUpdate = typeSiteRepository.findAll().size();

        // Create the TypeSite

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeSiteMockMvc.perform(put("/api/type-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSite)))
            .andExpect(status().isCreated());

        // Validate the TypeSite in the database
        List<TypeSite> typeSiteList = typeSiteRepository.findAll();
        assertThat(typeSiteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeSite() throws Exception {
        // Initialize the database
        typeSiteService.save(typeSite);

        int databaseSizeBeforeDelete = typeSiteRepository.findAll().size();

        // Get the typeSite
        restTypeSiteMockMvc.perform(delete("/api/type-sites/{id}", typeSite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeSiteExistsInEs = typeSiteSearchRepository.exists(typeSite.getId());
        assertThat(typeSiteExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeSite> typeSiteList = typeSiteRepository.findAll();
        assertThat(typeSiteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeSite() throws Exception {
        // Initialize the database
        typeSiteService.save(typeSite);

        // Search the typeSite
        restTypeSiteMockMvc.perform(get("/api/_search/type-sites?query=id:" + typeSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSite.class);
        TypeSite typeSite1 = new TypeSite();
        typeSite1.setId(1L);
        TypeSite typeSite2 = new TypeSite();
        typeSite2.setId(typeSite1.getId());
        assertThat(typeSite1).isEqualTo(typeSite2);
        typeSite2.setId(2L);
        assertThat(typeSite1).isNotEqualTo(typeSite2);
        typeSite1.setId(null);
        assertThat(typeSite1).isNotEqualTo(typeSite2);
    }
}
