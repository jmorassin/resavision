package com.jmdev.resavision.service;

import com.jmdev.resavision.domain.Client;
import com.jmdev.resavision.repository.ClientRepository;
import com.jmdev.resavision.repository.search.ClientSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    private final ClientSearchRepository clientSearchRepository;

    public ClientService(ClientRepository clientRepository, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.clientSearchRepository = clientSearchRepository;
    }

    /**
     * Save a client.
     *
     * @param client the entity to save
     * @return the persisted entity
     */
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        Client result = clientRepository.save(client);
        clientSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable);
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Client findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findOne(id);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.delete(id);
        clientSearchRepository.delete(id);
    }

    /**
     * Search for the client corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Client> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clients for query {}", query);
        Page<Client> result = clientSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
