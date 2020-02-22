package com.naucnacentrala.NaucnaCentrala.repository.elasticSearch;

import com.naucnacentrala.NaucnaCentrala.model.SciencePaperES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SciencePaperESRepository extends ElasticsearchRepository<SciencePaperES, String> {

    SciencePaperES findOneById(String id);

}
