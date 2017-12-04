package com.jmdev.resavision.repository.search;

import com.jmdev.resavision.domain.Employe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Employe entity.
 */
public interface EmployeSearchRepository extends ElasticsearchRepository<Employe, Long> {
}
