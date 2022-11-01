package com.programmers.service.impl;

import com.programmers.model.db.Exam;
import com.programmers.model.db.ExamDetail;
import com.programmers.model.db.Result;
import com.programmers.model.db.ResultDetail;
import com.programmers.service.DownloadResultService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DownloadResultServiceImpl implements DownloadResultService {

    public ByteArrayInputStream resultToExcel(List<Result> result, Exam exam) throws IOException {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final Sheet sheet = workbook.createSheet("Result");

            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            final CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            final Set<ExamDetail> examDetail = exam.getExamDetailSet();
            final int headerColumns = 13 + examDetail.size();
            final Row headerRow = sheet.createRow(0);
            final Row secondHeaderRow = sheet.createRow(1);
            sheet.createFreezePane(0, 2);

            final List<String> categoryList = new ArrayList<>();

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerColumns - 1));
            final Cell headerCell = headerRow.getCell(0);
            headerCell.setCellValue(exam.getExamName());
            headerCell.setCellStyle(headerCellStyle);

            final String[] columns = {"Name", "Email", "Mobile Number", "Age", "City", "State", "College", "Skill Set", "Degree", "Passing Year",
                    "Graduation Percentage", "Total Marks", "Obtained Marks"};

            int headerLength = columns.length - 1;

            for (int col = 0; col <= headerLength; col++) {
                final Cell cell = secondHeaderRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            for (ExamDetail exDetail : examDetail) {
                final int totalCategoryMarks = exDetail.getNumberOfQuestions() * exDetail.getSection().getMarksPerQuestion();
                final String name = exDetail.getQuestionCategory().getQuestionCategoryName() + "-"
                                    + exDetail.getSection().getMarksPerQuestion() + " Out of(" + totalCategoryMarks + ")";
                final Cell cell = secondHeaderRow.createCell(++headerLength);
                cell.setCellValue(name);
                cell.setCellStyle(headerCellStyle);
                categoryList.add(exDetail.getQuestionCategory().getQuestionCategoryName() + "-" + exDetail.getSection().getMarksPerQuestion());

            }

            int rowIdx = 2;
            for (Result res : result) {
                int headerNum;
                final Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(res.getUser().getFirstName() + " "
                                                 + res.getUser().getMiddleName() + " "
                                                 + res.getUser().getLastName());
                row.createCell(1).setCellValue(res.getUser().getEmailId());
                row.createCell(2).setCellValue(res.getUser().getMobileNumber());
                row.createCell(3).setCellValue(res.getUser().getAge());
                row.createCell(4).setCellValue(res.getUser().getCity());
                row.createCell(5).setCellValue(res.getUser().getState());
                row.createCell(6).setCellValue(res.getUser().getCollege());
                row.createCell(7).setCellValue(res.getUser().getSkills());
                row.createCell(8).setCellValue(res.getUser().getDegree());
                row.createCell(9).setCellValue(res.getUser().getGraduationYear());
                row.createCell(10).setCellValue(res.getUser().getGraduationPercentage());
                row.createCell(11).setCellValue(res.getTotalMarks());
                row.createCell(12).setCellValue(res.getObtainedMarks());
                final Set<ResultDetail> resultDetail = res.getResultDetailSet();

                for (ResultDetail resDetail : resultDetail) {
                    final String headerValue;
                    headerValue = resDetail.getExamDetail().getQuestionCategory().getQuestionCategoryName() + "-"
                            + resDetail.getExamDetail().getSection().getMarksPerQuestion();

                    headerNum = checkHeader(headerValue, categoryList);
                    row.createCell(headerNum + 13).setCellValue(resDetail.getObtainedMarks() + "/" + resDetail.getTotalMarks());
                }

                for (int i = 0; i < headerColumns; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private int checkHeader(String headerValue, List<String> categoryList) {
        int increment = 0;
        int cellNum = 0;
        while (increment < categoryList.size()) {
            if (categoryList.get(increment).equalsIgnoreCase(headerValue)) {
                cellNum = increment;
                break;
            }
            increment++;
        }
        return cellNum;
    }
}
