package com.programmers.service;

import com.programmers.model.db.Question;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface DownloadExcelService {
    ByteArrayInputStream questionsToExcel(List<Question> question) throws IOException;
}
