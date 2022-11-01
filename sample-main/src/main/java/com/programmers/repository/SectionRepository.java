package com.programmers.repository;

import com.programmers.model.db.Section;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Section repository.
 */
@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {

    Optional<Section> findById(Long id);

    List<Section> findAll();

    Optional<Section> findByMarksPerQuestion(int marks);

}
