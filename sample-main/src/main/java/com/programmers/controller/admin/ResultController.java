package com.programmers.controller.admin;

import com.programmers.model.JsonRest;
import com.programmers.model.db.Result;
import com.programmers.repository.ResultDetailRepository;
import com.programmers.repository.ResultRepository;
import com.programmers.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static com.programmers.common.RequestMappingConstant.DELETE_RESULT;
import static com.programmers.common.RequestMappingConstant.GET_ALL_RESULT;
import static com.programmers.common.RequestMappingConstant.GET_RESULT;
import static com.programmers.common.RequestMappingConstant.PATH_ADMIN;

/**
 * @author Programmers.io
 * @created by HiteshSoni on 21-05-2021
 * @project otp-service
 */
@RestController
@RequestMapping(PATH_ADMIN)
public class ResultController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ResultDetailRepository resultDetailRepository;

    @GetMapping(value = GET_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<Result>> getResult(@RequestParam(value = "id") long id) {
        final List<Result> resultList = resultService.getResult(id);
        if (resultList.isEmpty()) {
            return new JsonRest<>(true, new HashMap<>(), "Exam not found with - " + id, "", HttpStatus.NOT_FOUND);
        }
        final HashMap<String, List<Result>> resultListmap = new HashMap<>();
        resultListmap.put("resultList", resultList);
        return new JsonRest<>(true, resultListmap, "", "", HttpStatus.OK);
    }

    @GetMapping(value = GET_ALL_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, List<Result>> getAllResult() {
        final List<Result> resultList = resultService.getAllResult();
        final HashMap<String, List<Result>> resultListMap = new HashMap<>();
        resultListMap.put("resultList", resultList);
        if (resultList.isEmpty()) {
            return new JsonRest<>(true, resultListMap, "Result list empty", "", HttpStatus.OK);
        }
        return new JsonRest<>(true, resultListMap, "", "", HttpStatus.OK);
    }

    @DeleteMapping(value = DELETE_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRest<String, String> deleteAllResult() {
        final List<Result> resultList = resultService.getAllResult();
        if (resultList.isEmpty()) {
            return new JsonRest<>(true, new HashMap<>(), "Result list empty", "", HttpStatus.OK);
        }
        resultDetailRepository.deleteAll();
        resultRepository.deleteAll();
        //changes done
        return new JsonRest<>(true, new HashMap<>(), "All Results Deleted Sucessfully", "", HttpStatus.OK);
    }
}
