package com.programmers.service;

import com.programmers.model.db.Exam;
import com.programmers.model.db.Result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface DownloadResultService {
    ByteArrayInputStream resultToExcel(List<Result> result, Exam exam) throws IOException;
}
