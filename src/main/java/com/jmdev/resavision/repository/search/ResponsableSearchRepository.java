package com.jmdev.resavision.repository.search;

import com.jmdev.resavision.domain.Responsable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Responsable entity.
 */
public interface ResponsableSearchRepository extends ElasticsearchRepository<Responsable, Long> {
}
