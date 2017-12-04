package com.jmdev.resavision.web.rest;

import com.jmdev.resavision.ResavisionApp;

import com.jmdev.resavision.domain.Employe;
import com.jmdev.resavision.repository.EmployeRepository;
import com.jmdev.resavision.service.EmployeService;
import com.jmdev.resavision.repository.search.EmployeSearchRepository;
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
 * Test class for the EmployeResource REST controller.
 *
 * @see EmployeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResavisionApp.class)
public class EmployeResourceIntTest {

    private static final Civilite DEFAULT_CIVILITE = Civilite.MME;
    private static final Civilite UPDATED_CIVILITE = Civilite.MELLE;

    private static final Boolean DEFAULT_CHECK_TEL = false;
    private static final Boolean UPDATED_CHECK_TEL = true;

    private static final Boolean DEFAULT_CHEK_MAIL = false;
    private static final Boolean UPDATED_CHEK_MAIL = true;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeSearchRepository employeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeMockMvc;

    private Employe employe;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeResource employeResource = new EmployeResource(employeService);
        this.restEmployeMockMvc = MockMvcBuilders.standaloneSetup(employeResource)
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
    public static Employe createEntity(EntityManager em) {
        Employe employe = new Employe()
            .civilite(DEFAULT_CIVILITE)
            .checkTel(DEFAULT_CHECK_TEL)
            .chekMail(DEFAULT_CHEK_MAIL);
        return employe;
    }

    @Before
    public void initTest() {
        employeSearchRepository.deleteAll();
        employe = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmploye() throws Exception {
        int databaseSizeBeforeCreate = employeRepository.findAll().size();

        // Create the Employe
        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isCreated());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeCreate + 1);
        Employe testEmploye = employeList.get(employeList.size() - 1);
        assertThat(testEmploye.getCivilite()).isEqualTo(DEFAULT_CIVILITE);
        assertThat(testEmploye.isCheckTel()).isEqualTo(DEFAULT_CHECK_TEL);
        assertThat(testEmploye.isChekMail()).isEqualTo(DEFAULT_CHEK_MAIL);

        // Validate the Employe in Elasticsearch
        Employe employeEs = employeSearchRepository.findOne(testEmploye.getId());
        assertThat(employeEs).isEqualToComparingFieldByField(testEmploye);
    }

    @Test
    @Transactional
    public void createEmployeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeRepository.findAll().size();

        // Create the Employe with an existing ID
        employe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeMockMvc.perform(post("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmployes() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get all the employeList
        restEmployeMockMvc.perform(get("/api/employes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].checkTel").value(hasItem(DEFAULT_CHECK_TEL.booleanValue())))
            .andExpect(jsonPath("$.[*].chekMail").value(hasItem(DEFAULT_CHEK_MAIL.booleanValue())));
    }

    @Test
    @Transactional
    public void getEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get the employe
        restEmployeMockMvc.perform(get("/api/employes/{id}", employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employe.getId().intValue()))
            .andExpect(jsonPath("$.civilite").value(DEFAULT_CIVILITE.toString()))
            .andExpect(jsonPath("$.checkTel").value(DEFAULT_CHECK_TEL.booleanValue()))
            .andExpect(jsonPath("$.chekMail").value(DEFAULT_CHEK_MAIL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmploye() throws Exception {
        // Get the employe
        restEmployeMockMvc.perform(get("/api/employes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmploye() throws Exception {
        // Initialize the database
        employeService.save(employe);

        int databaseSizeBeforeUpdate = employeRepository.findAll().size();

        // Update the employe
        Employe updatedEmploye = employeRepository.findOne(employe.getId());
        // Disconnect from session so that the updates on updatedEmploye are not directly saved in db
        em.detach(updatedEmploye);
        updatedEmploye
            .civilite(UPDATED_CIVILITE)
            .checkTel(UPDATED_CHECK_TEL)
            .chekMail(UPDATED_CHEK_MAIL);

        restEmployeMockMvc.perform(put("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmploye)))
            .andExpect(status().isOk());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeUpdate);
        Employe testEmploye = employeList.get(employeList.size() - 1);
        assertThat(testEmploye.getCivilite()).isEqualTo(UPDATED_CIVILITE);
        assertThat(testEmploye.isCheckTel()).isEqualTo(UPDATED_CHECK_TEL);
        assertThat(testEmploye.isChekMail()).isEqualTo(UPDATED_CHEK_MAIL);

        // Validate the Employe in Elasticsearch
        Employe employeEs = employeSearchRepository.findOne(testEmploye.getId());
        assertThat(employeEs).isEqualToComparingFieldByField(testEmploye);
    }

    @Test
    @Transactional
    public void updateNonExistingEmploye() throws Exception {
        int databaseSizeBeforeUpdate = employeRepository.findAll().size();

        // Create the Employe

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeMockMvc.perform(put("/api/employes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employe)))
            .andExpect(status().isCreated());

        // Validate the Employe in the database
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmploye() throws Exception {
        // Initialize the database
        employeService.save(employe);

        int databaseSizeBeforeDelete = employeRepository.findAll().size();

        // Get the employe
        restEmployeMockMvc.perform(delete("/api/employes/{id}", employe.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean employeExistsInEs = employeSearchRepository.exists(employe.getId());
        assertThat(employeExistsInEs).isFalse();

        // Validate the database is empty
        List<Employe> employeList = employeRepository.findAll();
        assertThat(employeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmploye() throws Exception {
        // Initialize the database
        employeService.save(employe);

        // Search the employe
        restEmployeMockMvc.perform(get("/api/_search/employes?query=id:" + employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].checkTel").value(hasItem(DEFAULT_CHECK_TEL.booleanValue())))
            .andExpect(jsonPath("$.[*].chekMail").value(hasItem(DEFAULT_CHEK_MAIL.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employe.class);
        Employe employe1 = new Employe();
        employe1.setId(1L);
        Employe employe2 = new Employe();
        employe2.setId(employe1.getId());
        assertThat(employe1).isEqualTo(employe2);
        employe2.setId(2L);
        assertThat(employe1).isNotEqualTo(employe2);
        employe1.setId(null);
        assertThat(employe1).isNotEqualTo(employe2);
    }
}
