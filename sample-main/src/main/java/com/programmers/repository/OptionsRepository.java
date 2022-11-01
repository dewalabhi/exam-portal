package com.programmers.repository;

import com.programmers.model.db.Options;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Option repository.
 */
@Repository
public interface OptionsRepository extends CrudRepository<Options, Long> {

}
