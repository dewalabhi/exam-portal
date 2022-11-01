package com.programmers.repository;

import com.programmers.model.db.ResultDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultDetailRepository extends CrudRepository<ResultDetail, Long> {

}
