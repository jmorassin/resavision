package com.jmdev.resavision.web.rest;

import com.jmdev.resavision.ResavisionApp;

import com.jmdev.resavision.domain.Boutique;
import com.jmdev.resavision.repository.BoutiqueRepository;
import com.jmdev.resavision.service.BoutiqueService;
import com.jmdev.resavision.repository.search.BoutiqueSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jmdev.resavision.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BoutiqueResource REST controller.
 *
 * @see BoutiqueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResavisionApp.class)
public class BoutiqueResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_2 = "BBBBBBBBBB";

    @Autowired
    private BoutiqueRepository boutiqueRepository;

    @Autowired
    private BoutiqueService boutiqueService;

    @Autowired
    private BoutiqueSearchRepository boutiqueSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBoutiqueMockMvc;

    private Boutique boutique;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoutiqueResource boutiqueResource = new BoutiqueResource(boutiqueService);
        this.restBoutiqueMockMvc = MockMvcBuilders.standaloneSetup(boutiqueResource)
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
    public static Boutique createEntity(EntityManager em) {
        Boutique boutique = new Boutique()
            .nom(DEFAULT_NOM)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .url(DEFAULT_URL)
            .numTelephone(DEFAULT_NUM_TELEPHONE)
            .mail(DEFAULT_MAIL)
            .adresse1(DEFAULT_ADRESSE_1)
            .adresse2(DEFAULT_ADRESSE_2);
        return boutique;
    }

    @Before
    public void initTest() {
        boutiqueSearchRepository.deleteAll();
        boutique = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoutique() throws Exception {
        int databaseSizeBeforeCreate = boutiqueRepository.findAll().size();

        // Create the Boutique
        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isCreated());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeCreate + 1);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBoutique.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testBoutique.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testBoutique.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testBoutique.getNumTelephone()).isEqualTo(DEFAULT_NUM_TELEPHONE);
        assertThat(testBoutique.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testBoutique.getAdresse1()).isEqualTo(DEFAULT_ADRESSE_1);
        assertThat(testBoutique.getAdresse2()).isEqualTo(DEFAULT_ADRESSE_2);

        // Validate the Boutique in Elasticsearch
        Boutique boutiqueEs = boutiqueSearchRepository.findOne(testBoutique.getId());
        assertThat(boutiqueEs).isEqualToComparingFieldByField(testBoutique);
    }

    @Test
    @Transactional
    public void createBoutiqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boutiqueRepository.findAll().size();

        // Create the Boutique with an existing ID
        boutique.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = boutiqueRepository.findAll().size();
        // set the field null
        boutique.setNom(null);

        // Create the Boutique, which fails.

        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = boutiqueRepository.findAll().size();
        // set the field null
        boutique.setNumTelephone(null);

        // Create the Boutique, which fails.

        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = boutiqueRepository.findAll().size();
        // set the field null
        boutique.setMail(null);

        // Create the Boutique, which fails.

        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresse1IsRequired() throws Exception {
        int databaseSizeBeforeTest = boutiqueRepository.findAll().size();
        // set the field null
        boutique.setAdresse1(null);

        // Create the Boutique, which fails.

        restBoutiqueMockMvc.perform(post("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoutiques() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        // Get all the boutiqueList
        restBoutiqueMockMvc.perform(get("/api/boutiques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boutique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL.toString())))
            .andExpect(jsonPath("$.[*].adresse1").value(hasItem(DEFAULT_ADRESSE_1.toString())))
            .andExpect(jsonPath("$.[*].adresse2").value(hasItem(DEFAULT_ADRESSE_2.toString())));
    }

    @Test
    @Transactional
    public void getBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        // Get the boutique
        restBoutiqueMockMvc.perform(get("/api/boutiques/{id}", boutique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boutique.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.numTelephone").value(DEFAULT_NUM_TELEPHONE.toString()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL.toString()))
            .andExpect(jsonPath("$.adresse1").value(DEFAULT_ADRESSE_1.toString()))
            .andExpect(jsonPath("$.adresse2").value(DEFAULT_ADRESSE_2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoutique() throws Exception {
        // Get the boutique
        restBoutiqueMockMvc.perform(get("/api/boutiques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoutique() throws Exception {
        // Initialize the database
        boutiqueService.save(boutique);

        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Update the boutique
        Boutique updatedBoutique = boutiqueRepository.findOne(boutique.getId());
        // Disconnect from session so that the updates on updatedBoutique are not directly saved in db
        em.detach(updatedBoutique);
        updatedBoutique
            .nom(UPDATED_NOM)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .url(UPDATED_URL)
            .numTelephone(UPDATED_NUM_TELEPHONE)
            .mail(UPDATED_MAIL)
            .adresse1(UPDATED_ADRESSE_1)
            .adresse2(UPDATED_ADRESSE_2);

        restBoutiqueMockMvc.perform(put("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoutique)))
            .andExpect(status().isOk());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBoutique.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testBoutique.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testBoutique.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testBoutique.getNumTelephone()).isEqualTo(UPDATED_NUM_TELEPHONE);
        assertThat(testBoutique.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testBoutique.getAdresse1()).isEqualTo(UPDATED_ADRESSE_1);
        assertThat(testBoutique.getAdresse2()).isEqualTo(UPDATED_ADRESSE_2);

        // Validate the Boutique in Elasticsearch
        Boutique boutiqueEs = boutiqueSearchRepository.findOne(testBoutique.getId());
        assertThat(boutiqueEs).isEqualToComparingFieldByField(testBoutique);
    }

    @Test
    @Transactional
    public void updateNonExistingBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Create the Boutique

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBoutiqueMockMvc.perform(put("/api/boutiques")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isCreated());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBoutique() throws Exception {
        // Initialize the database
        boutiqueService.save(boutique);

        int databaseSizeBeforeDelete = boutiqueRepository.findAll().size();

        // Get the boutique
        restBoutiqueMockMvc.perform(delete("/api/boutiques/{id}", boutique.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean boutiqueExistsInEs = boutiqueSearchRepository.exists(boutique.getId());
        assertThat(boutiqueExistsInEs).isFalse();

        // Validate the database is empty
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBoutique() throws Exception {
        // Initialize the database
        boutiqueService.save(boutique);

        // Search the boutique
        restBoutiqueMockMvc.perform(get("/api/_search/boutiques?query=id:" + boutique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boutique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL.toString())))
            .andExpect(jsonPath("$.[*].adresse1").value(hasItem(DEFAULT_ADRESSE_1.toString())))
            .andExpect(jsonPath("$.[*].adresse2").value(hasItem(DEFAULT_ADRESSE_2.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boutique.class);
        Boutique boutique1 = new Boutique();
        boutique1.setId(1L);
        Boutique boutique2 = new Boutique();
        boutique2.setId(boutique1.getId());
        assertThat(boutique1).isEqualTo(boutique2);
        boutique2.setId(2L);
        assertThat(boutique1).isNotEqualTo(boutique2);
        boutique1.setId(null);
        assertThat(boutique1).isNotEqualTo(boutique2);
    }
}
