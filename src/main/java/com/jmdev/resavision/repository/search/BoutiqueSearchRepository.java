package com.jmdev.resavision.repository.search;

import com.jmdev.resavision.domain.Boutique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Boutique entity.
 */
public interface BoutiqueSearchRepository extends ElasticsearchRepository<Boutique, Long> {
}
