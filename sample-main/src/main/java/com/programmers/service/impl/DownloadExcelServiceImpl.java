package com.programmers.service.impl;

import com.programmers.model.db.Options;
import com.programmers.model.db.Question;
import com.programmers.service.DownloadExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class DownloadExcelServiceImpl implements DownloadExcelService {

    public ByteArrayInputStream questionsToExcel(List<Question> question) throws IOException {

        final String[] columns = {"Id", "QuestionText", "Marks", "Option1", "Option2", "Option3", "Option4"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final Sheet sheet = workbook.createSheet("Questions");

            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            final CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            final Row headerRow = sheet.createRow(0);
            sheet.createFreezePane(0, 1);

            for (int col = 0; col < columns.length; col++) {
                final Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            final CellStyle style = workbook.createCellStyle();
            final Font font = workbook.createFont();
            font.setColor(IndexedColors.BLUE.getIndex());
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowIdx = 1;
            for (Question ques : question) {
                final Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(ques.getId());
                row.createCell(1).setCellValue(ques.getQuestionText());
                row.createCell(2).setCellValue(ques.getSection().getMarksPerQuestion());
                final Set<Options> options = ques.getOptions();
                int cellIncrement = 1;
                for (Options opt : options) {
                    final boolean answer = opt.getAnswer();
                    if (answer) {
                        final Cell answerCell = row.createCell(cellIncrement + 2);
                        answerCell.setCellValue(opt.getOptionText());
                        answerCell.setCellStyle(style);
                    } else {
                        row.createCell(cellIncrement + 2).setCellValue(opt.getOptionText());
                    }
                    cellIncrement += 1;
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
