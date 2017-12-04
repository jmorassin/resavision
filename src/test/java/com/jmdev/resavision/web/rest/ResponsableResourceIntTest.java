package com.jmdev.resavision.web.rest;

import com.jmdev.resavision.ResavisionApp;

import com.jmdev.resavision.domain.Responsable;
import com.jmdev.resavision.repository.ResponsableRepository;
import com.jmdev.resavision.service.ResponsableService;
import com.jmdev.resavision.repository.search.ResponsableSearchRepository;
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

import com.jmdev.resavision.domain.enumeration.Civilite;
/**
 * Test class for the ResponsableResource REST controller.
 *
 * @see ResponsableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResavisionApp.class)
public class ResponsableResourceIntTest {

    private static final Civilite DEFAULT_CIVILITE = Civilite.MME;
    private static final Civilite UPDATED_CIVILITE = Civilite.MELLE;

    private static final Boolean DEFAULT_CHECK_TEL = false;
    private static final Boolean UPDATED_CHECK_TEL = true;

    private static final Boolean DEFAULT_CHEK_MAIL = false;
    private static final Boolean UPDATED_CHEK_MAIL = true;

    @Autowired
    private ResponsableRepository responsableRepository;

    @Autowired
    private ResponsableService responsableService;

    @Autowired
    private ResponsableSearchRepository responsableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResponsableMockMvc;

    private Responsable responsable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResponsableResource responsableResource = new ResponsableResource(responsableService);
        this.restResponsableMockMvc = MockMvcBuilders.standaloneSetup(responsableResource)
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
    public static Responsable createEntity(EntityManager em) {
        Responsable responsable = new Responsable()
            .civilite(DEFAULT_CIVILITE)
            .checkTel(DEFAULT_CHECK_TEL)
            .chekMail(DEFAULT_CHEK_MAIL);
        return responsable;
    }

    @Before
    public void initTest() {
        responsableSearchRepository.deleteAll();
        responsable = createEntity(em);
    }

    @Test
    @Transactional
    public void createResponsable() throws Exception {
        int databaseSizeBeforeCreate = responsableRepository.findAll().size();

        // Create the Responsable
        restResponsableMockMvc.perform(post("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isCreated());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeCreate + 1);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getCivilite()).isEqualTo(DEFAULT_CIVILITE);
        assertThat(testResponsable.isCheckTel()).isEqualTo(DEFAULT_CHECK_TEL);
        assertThat(testResponsable.isChekMail()).isEqualTo(DEFAULT_CHEK_MAIL);

        // Validate the Responsable in Elasticsearch
        Responsable responsableEs = responsableSearchRepository.findOne(testResponsable.getId());
        assertThat(responsableEs).isEqualToComparingFieldByField(testResponsable);
    }

    @Test
    @Transactional
    public void createResponsableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = responsableRepository.findAll().size();

        // Create the Responsable with an existing ID
        responsable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsableMockMvc.perform(post("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isBadRequest());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllResponsables() throws Exception {
        // Initialize the database
        responsableRepository.saveAndFlush(responsable);

        // Get all the responsableList
        restResponsableMockMvc.perform(get("/api/responsables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsable.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].checkTel").value(hasItem(DEFAULT_CHECK_TEL.booleanValue())))
            .andExpect(jsonPath("$.[*].chekMail").value(hasItem(DEFAULT_CHEK_MAIL.booleanValue())));
    }

    @Test
    @Transactional
    public void getResponsable() throws Exception {
        // Initialize the database
        responsableRepository.saveAndFlush(responsable);

        // Get the responsable
        restResponsableMockMvc.perform(get("/api/responsables/{id}", responsable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(responsable.getId().intValue()))
            .andExpect(jsonPath("$.civilite").value(DEFAULT_CIVILITE.toString()))
            .andExpect(jsonPath("$.checkTel").value(DEFAULT_CHECK_TEL.booleanValue()))
            .andExpect(jsonPath("$.chekMail").value(DEFAULT_CHEK_MAIL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResponsable() throws Exception {
        // Get the responsable
        restResponsableMockMvc.perform(get("/api/responsables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResponsable() throws Exception {
        // Initialize the database
        responsableService.save(responsable);

        int databaseSizeBeforeUpdate = responsableRepository.findAll().size();

        // Update the responsable
        Responsable updatedResponsable = responsableRepository.findOne(responsable.getId());
        // Disconnect from session so that the updates on updatedResponsable are not directly saved in db
        em.detach(updatedResponsable);
        updatedResponsable
            .civilite(UPDATED_CIVILITE)
            .checkTel(UPDATED_CHECK_TEL)
            .chekMail(UPDATED_CHEK_MAIL);

        restResponsableMockMvc.perform(put("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResponsable)))
            .andExpect(status().isOk());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate);
        Responsable testResponsable = responsableList.get(responsableList.size() - 1);
        assertThat(testResponsable.getCivilite()).isEqualTo(UPDATED_CIVILITE);
        assertThat(testResponsable.isCheckTel()).isEqualTo(UPDATED_CHECK_TEL);
        assertThat(testResponsable.isChekMail()).isEqualTo(UPDATED_CHEK_MAIL);

        // Validate the Responsable in Elasticsearch
        Responsable responsableEs = responsableSearchRepository.findOne(testResponsable.getId());
        assertThat(responsableEs).isEqualToComparingFieldByField(testResponsable);
    }

    @Test
    @Transactional
    public void updateNonExistingResponsable() throws Exception {
        int databaseSizeBeforeUpdate = responsableRepository.findAll().size();

        // Create the Responsable

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResponsableMockMvc.perform(put("/api/responsables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(responsable)))
            .andExpect(status().isCreated());

        // Validate the Responsable in the database
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResponsable() throws Exception {
        // Initialize the database
        responsableService.save(responsable);

        int databaseSizeBeforeDelete = responsableRepository.findAll().size();

        // Get the responsable
        restResponsableMockMvc.perform(delete("/api/responsables/{id}", responsable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean responsableExistsInEs = responsableSearchRepository.exists(responsable.getId());
        assertThat(responsableExistsInEs).isFalse();

        // Validate the database is empty
        List<Responsable> responsableList = responsableRepository.findAll();
        assertThat(responsableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResponsable() throws Exception {
        // Initialize the database
        responsableService.save(responsable);

        // Search the responsable
        restResponsableMockMvc.perform(get("/api/_search/responsables?query=id:" + responsable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsable.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].checkTel").value(hasItem(DEFAULT_CHECK_TEL.booleanValue())))
            .andExpect(jsonPath("$.[*].chekMail").value(hasItem(DEFAULT_CHEK_MAIL.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Responsable.class);
        Responsable responsable1 = new Responsable();
        responsable1.setId(1L);
        Responsable responsable2 = new Responsable();
        responsable2.setId(responsable1.getId());
        assertThat(responsable1).isEqualTo(responsable2);
        responsable2.setId(2L);
        assertThat(responsable1).isNotEqualTo(responsable2);
        responsable1.setId(null);
        assertThat(responsable1).isNotEqualTo(responsable2);
    }
}
