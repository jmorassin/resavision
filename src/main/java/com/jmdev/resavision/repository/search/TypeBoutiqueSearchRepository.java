package com.jmdev.resavision.repository.search;

import com.jmdev.resavision.domain.TypeBoutique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeBoutique entity.
 */
public interface TypeBoutiqueSearchRepository extends ElasticsearchRepository<TypeBoutique, Long> {
}
