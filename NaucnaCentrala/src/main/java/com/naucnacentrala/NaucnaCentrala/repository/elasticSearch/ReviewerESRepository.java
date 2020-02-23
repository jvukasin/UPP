package com.naucnacentrala.NaucnaCentrala.repository.elasticSearch;

import com.naucnacentrala.NaucnaCentrala.model.ReviewerES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewerESRepository extends ElasticsearchRepository <ReviewerES,Long> {
    ReviewerES findOneById(String id);

    List<ReviewerES> findAll();
}