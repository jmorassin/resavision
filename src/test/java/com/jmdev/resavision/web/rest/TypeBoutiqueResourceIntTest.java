package com.jmdev.resavision.web.rest;

import com.jmdev.resavision.ResavisionApp;

import com.jmdev.resavision.domain.TypeBoutique;
import com.jmdev.resavision.repository.TypeBoutiqueRepository;
import com.jmdev.resavision.service.TypeBoutiqueService;
import com.jmdev.resavision.repository.search.TypeBoutiqueSearchRepository;
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
 * Test class for the TypeBoutiqueResource REST controller.
 *
 * @see TypeBoutiqueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResavisionApp.class)
public class TypeBoutiqueResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private TypeBoutiqueRepository typeBoutiqueRepository;

    @Autowired
    private TypeBoutiqueService typeBoutiqueService;

    @Autowired
    private TypeBoutiqueSearchRepository typeBoutiqueSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeBoutiqueMockMvc;

    private TypeBoutique typeBoutique;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeBoutiqueResource typeBoutiqueResource = new TypeBoutiqueResource(typeBoutiqueService);
        this.restTypeBoutiqueMockMvc = MockMvcBuilders.standaloneSetup(typeBoutiqueResource)
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
    public static TypeBoutique createEntity(EntityManager em) {
        TypeBoutique typeBoutique = new TypeBoutique()
            .nom(DEFAULT_NOM);
        return typeBoutique;
    }

    @Before
    public void initTest() {
        typeBoutiqueSearchRepository.deleteAll();
        typeBoutique = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeBoutique() throws Exception {
        int databaseSizeBeforeCreate = typeBoutiqueRepository.findAll().size();

        // Create the TypeBoutique
        restTypeBoutiqueMockMvc.perform(post("/api/type-boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoutique)))
            .andExpect(status().isCreated());

        // Validate the TypeBoutique in the database
        List<TypeBoutique> typeBoutiqueList = typeBoutiqueRepository.findAll();
        assertThat(typeBoutiqueList).hasSize(databaseSizeBeforeCreate + 1);
        TypeBoutique testTypeBoutique = typeBoutiqueList.get(typeBoutiqueList.size() - 1);
        assertThat(testTypeBoutique.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the TypeBoutique in Elasticsearch
        TypeBoutique typeBoutiqueEs = typeBoutiqueSearchRepository.findOne(testTypeBoutique.getId());
        assertThat(typeBoutiqueEs).isEqualToComparingFieldByField(testTypeBoutique);
    }

    @Test
    @Transactional
    public void createTypeBoutiqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeBoutiqueRepository.findAll().size();

        // Create the TypeBoutique with an existing ID
        typeBoutique.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeBoutiqueMockMvc.perform(post("/api/type-boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoutique)))
            .andExpect(status().isBadRequest());

        // Validate the TypeBoutique in the database
        List<TypeBoutique> typeBoutiqueList = typeBoutiqueRepository.findAll();
        assertThat(typeBoutiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeBoutiqueRepository.findAll().size();
        // set the field null
        typeBoutique.setNom(null);

        // Create the TypeBoutique, which fails.

        restTypeBoutiqueMockMvc.perform(post("/api/type-boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoutique)))
            .andExpect(status().isBadRequest());

        List<TypeBoutique> typeBoutiqueList = typeBoutiqueRepository.findAll();
        assertThat(typeBoutiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeBoutiques() throws Exception {
        // Initialize the database
        typeBoutiqueRepository.saveAndFlush(typeBoutique);

        // Get all the typeBoutiqueList
        restTypeBoutiqueMockMvc.perform(get("/api/type-boutiques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBoutique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getTypeBoutique() throws Exception {
        // Initialize the database
        typeBoutiqueRepository.saveAndFlush(typeBoutique);

        // Get the typeBoutique
        restTypeBoutiqueMockMvc.perform(get("/api/type-boutiques/{id}", typeBoutique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeBoutique.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeBoutique() throws Exception {
        // Get the typeBoutique
        restTypeBoutiqueMockMvc.perform(get("/api/type-boutiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeBoutique() throws Exception {
        // Initialize the database
        typeBoutiqueService.save(typeBoutique);

        int databaseSizeBeforeUpdate = typeBoutiqueRepository.findAll().size();

        // Update the typeBoutique
        TypeBoutique updatedTypeBoutique = typeBoutiqueRepository.findOne(typeBoutique.getId());
        // Disconnect from session so that the updates on updatedTypeBoutique are not directly saved in db
        em.detach(updatedTypeBoutique);
        updatedTypeBoutique
            .nom(UPDATED_NOM);

        restTypeBoutiqueMockMvc.perform(put("/api/type-boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeBoutique)))
            .andExpect(status().isOk());

        // Validate the TypeBoutique in the database
        List<TypeBoutique> typeBoutiqueList = typeBoutiqueRepository.findAll();
        assertThat(typeBoutiqueList).hasSize(databaseSizeBeforeUpdate);
        TypeBoutique testTypeBoutique = typeBoutiqueList.get(typeBoutiqueList.size() - 1);
        assertThat(testTypeBoutique.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the TypeBoutique in Elasticsearch
        TypeBoutique typeBoutiqueEs = typeBoutiqueSearchRepository.findOne(testTypeBoutique.getId());
        assertThat(typeBoutiqueEs).isEqualToComparingFieldByField(testTypeBoutique);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeBoutique() throws Exception {
        int databaseSizeBeforeUpdate = typeBoutiqueRepository.findAll().size();

        // Create the TypeBoutique

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeBoutiqueMockMvc.perform(put("/api/type-boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoutique)))
            .andExpect(status().isCreated());

        // Validate the TypeBoutique in the database
        List<TypeBoutique> typeBoutiqueList = typeBoutiqueRepository.findAll();
        assertThat(typeBoutiqueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeBoutique() throws Exception {
        // Initialize the database
        typeBoutiqueService.save(typeBoutique);

        int databaseSizeBeforeDelete = typeBoutiqueRepository.findAll().size();

        // Get the typeBoutique
        restTypeBoutiqueMockMvc.perform(delete("/api/type-boutiques/{id}", typeBoutique.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeBoutiqueExistsInEs = typeBoutiqueSearchRepository.exists(typeBoutique.getId());
        assertThat(typeBoutiqueExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeBoutique> typeBoutiqueList = typeBoutiqueRepository.findAll();
        assertThat(typeBoutiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeBoutique() throws Exception {
        // Initialize the database
        typeBoutiqueService.save(typeBoutique);

        // Search the typeBoutique
        restTypeBoutiqueMockMvc.perform(get("/api/_search/type-boutiques?query=id:" + typeBoutique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBoutique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeBoutique.class);
        TypeBoutique typeBoutique1 = new TypeBoutique();
        typeBoutique1.setId(1L);
        TypeBoutique typeBoutique2 = new TypeBoutique();
        typeBoutique2.setId(typeBoutique1.getId());
        assertThat(typeBoutique1).isEqualTo(typeBoutique2);
        typeBoutique2.setId(2L);
        assertThat(typeBoutique1).isNotEqualTo(typeBoutique2);
        typeBoutique1.setId(null);
        assertThat(typeBoutique1).isNotEqualTo(typeBoutique2);
    }
}
