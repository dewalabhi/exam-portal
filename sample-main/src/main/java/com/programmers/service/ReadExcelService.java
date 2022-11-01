package com.programmers.service;

import com.programmers.model.response.StatusResponse;

public interface ReadExcelService {
    StatusResponse processExcel(String fileBlob);
}