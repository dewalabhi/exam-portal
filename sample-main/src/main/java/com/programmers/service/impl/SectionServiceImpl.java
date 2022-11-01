package com.programmers.service.impl;

import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.db.Section;
import com.programmers.repository.SectionRepository;
import com.programmers.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public Section createSection(Section section) {
        validate(section);
        final Optional<Section> findSection = sectionRepository.findByMarksPerQuestion(section.getMarksPerQuestion());
        if (findSection.isPresent() && findSection.get().getId() != section.getId()) {
            throw new InvalidEntityException("Duplicate Marks Per Question");
        }
        return sectionRepository.save(section);
    }

    @Override
    public Section updateSection(Long sectionID, Section section) {
        validate(section);
        Optional<Section> findSection = sectionRepository.findById(sectionID);
        if (!findSection.isPresent()) {
            throw new EntityNotFoundException("Section not found with - " + sectionID);
        }

        findSection = sectionRepository.findByMarksPerQuestion(section.getMarksPerQuestion());
        if (findSection.isPresent() && findSection.get().getId() != sectionID) {
            throw new InvalidEntityException("Duplicate Marks Per Question");
        }
        section.setId(sectionID);
        return sectionRepository.save(section);
    }

    public void validate(Section section) {
        final List<String> errorMessage = new ArrayList<>();
        try {
            final int marksPerQuestion = section.getMarksPerQuestion();
            if (marksPerQuestion <= 0) {
                errorMessage.add("Marks Per Question should be valid value greater than 0");
            }
        } catch (NumberFormatException ex) {
            errorMessage.add(ex.getMessage());
        }
        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid Section detail - " + errorMessage);
        }
    }
}
