package com.programmers.service;

import com.programmers.model.db.Section;

public interface SectionService {
    Section createSection(Section section);

    Section updateSection(Long sectionId, Section section);
}
