package com.naucnacentrala.NaucnaCentrala.services.elasticSearch;

import com.naucnacentrala.NaucnaCentrala.model.ReviewerES;
import com.naucnacentrala.NaucnaCentrala.repository.elasticSearch.ReviewerESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerESService {

    @Autowired
    private ReviewerESRepository reviewerESRepo;

    public ReviewerES save(ReviewerES reviewerES){
        return reviewerESRepo.save(reviewerES);
    }

    public ReviewerES findOneById(String id){
        return reviewerESRepo.findOneById(id);
    }

}