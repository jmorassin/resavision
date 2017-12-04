package com.jmdev.resavision.repository.search;

import com.jmdev.resavision.domain.TypeSite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeSite entity.
 */
public interface TypeSiteSearchRepository extends ElasticsearchRepository<TypeSite, Long> {
}
