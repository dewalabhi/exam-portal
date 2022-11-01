package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.Section;
import com.programmers.repository.SectionRepository;
import com.programmers.service.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.programmers.common.RequestMappingConstant.CREATE_SECTION;
import static com.programmers.common.RequestMappingConstant.GET_ALL_SECTION;
import static com.programmers.common.RequestMappingConstant.GET_SECTION;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;
import static com.programmers.common.RequestMappingConstant.UPDATE_SECTION;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class SectionController {

    private final Logger logger = LoggerFactory.getLogger(SectionController.class);
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SectionRepository sectionRepository;

    @PostMapping(value = CREATE_SECTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, Long> createSection(@RequestBody Section section) {
        logger.debug("Section: {}", section);
        sectionService.createSection(section);
        return new JsonRest<>(true, new HashMap<>(), "Section created successfully", "", HttpStatus.CREATED);
    }

    @PutMapping(value = UPDATE_SECTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, Long> updateSection(@PathVariable(value = "sectionID") long sectionId, @RequestBody Section section) {
        logger.debug("Section: {}", section);
        sectionService.updateSection(sectionId, section);
        return new JsonRest<>(true, new HashMap<>(), "Section updated successfully", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_SECTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<Section>> getAllSections() {
        final List<Section> sectionList = sectionRepository.findAll();
        final HashMap<String, List<Section>> sectionListMap = new HashMap<>();
        sectionListMap.put("sectionList", sectionList);
        if (sectionList.isEmpty()) {
            return new JsonRest<>(true, sectionListMap, "Section list empty", "", HttpStatus.OK);
        }
        return new JsonRest<>(true, sectionListMap, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_SECTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, Section> getSection(@PathVariable(value = "sectionID") long sectionId) {
        final Optional<Section> section = sectionRepository.findById(sectionId);
        final HashMap<String, Section> sectionMap = new HashMap<>();
        section.ifPresent(getSection -> sectionMap.put("Admin", getSection));
        return section.map(getSection -> new JsonRest<>(true, sectionMap, "", "", HttpStatus.OK))
                .orElseGet(() -> new JsonRest<>(true, new HashMap<>(), "Section not found with - " + sectionId, "", HttpStatus.NOT_FOUND));
    }
}
