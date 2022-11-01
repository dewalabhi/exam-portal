package com.programmers.service.impl;

import com.programmers.model.DifficultyLevel;
import com.programmers.model.db.AdminUser;
import com.programmers.model.db.Options;
import com.programmers.model.db.Question;
import com.programmers.model.db.QuestionCategory;
import com.programmers.model.db.QuestionHistory;
import com.programmers.model.db.Section;
import com.programmers.model.response.StatusResponse;
import com.programmers.repository.AdminUserRepository;
import com.programmers.repository.QuestionCategoryRepository;
import com.programmers.repository.QuestionHistoryRepository;
import com.programmers.repository.QuestionRepository;
import com.programmers.repository.SectionRepository;
import com.programmers.service.ReadExcelService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.programmers.common.Constant.CODE_SUCCESS;
import static com.programmers.common.Constant.CODE_VALIDATION_ERROR;

@Service
public class ReadExcelServiceImpl implements ReadExcelService {

    private final Logger logger = LoggerFactory.getLogger(ReadExcelServiceImpl.class);
    @Autowired
    private QuestionCategoryRepository categoryMasterRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private QuestionHistoryRepository questionHistoryRepository;
    @Autowired
    private AdminUserRepository adminUserRepository;

    public StatusResponse processExcel(String fileBlob) {
        final StatusResponse statusResponse = new StatusResponse();
        statusResponse.setMessage("Data uploaded successfully");
        statusResponse.setStatus(HttpStatus.OK);
        statusResponse.setCode(CODE_SUCCESS);

        try {

            QuestionCategory questionCategory = new QuestionCategory();

            final byte[] decodedByte = Base64.decodeBase64(fileBlob);
            final Blob blob = new SerialBlob(decodedByte);
            final InputStream in = blob.getBinaryStream();

            final XSSFWorkbook workbook = new XSSFWorkbook(in);

            final XSSFSheet worksheet = workbook.getSheetAt(0);

            final XSSFRow headerRow = worksheet.getRow(0);

            final CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillForegroundColor((short) 41);
            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            final CellStyle errorStyle = workbook.createCellStyle();
            final Font errorFont = workbook.createFont();
            errorFont.setColor(IndexedColors.RED.getIndex());
            errorStyle.setFont(errorFont);
            errorStyle.setWrapText(true);

            headerRow.setRowStyle(headerStyle);
            final Cell errorCell = headerRow.createCell(7);
            errorCell.setCellStyle(headerStyle);
            errorCell.setCellValue("Errors");

            headerRow.getCell(0).setCellStyle(headerStyle);

            String errorMessage = validateHeader(worksheet);

            if (errorMessage.equals("")) {
                final String categoryValue = worksheet.getRow(0).getCell(0).getStringCellValue();
                final Optional<QuestionCategory> questionCategoryName = categoryMasterRepository.findByQuestionCategoryName(categoryValue);
                if (questionCategoryName.isPresent()) {
                    questionCategory = questionCategoryName.get();
                } else {
                    errorMessage = "No such category present!!";
                }
            }

            if (!errorMessage.equals("")) {
                final XSSFRow row = worksheet.getRow(1);
                final Cell cell = row.createCell(7);
                cell.setCellStyle(errorStyle);
                cell.setCellValue(errorMessage);
            } else {
                int cellIncrement = 2;
                while (cellIncrement <= worksheet.getLastRowNum()) {
                    final XSSFRow row1 = worksheet.getRow(cellIncrement++);
                    errorMessage = validateEachRow(row1);
                    if (!errorMessage.equals(" ")) {
                        final Cell cell1 = row1.createCell(7);
                        cell1.setCellValue(errorMessage);
                        cell1.setCellStyle(errorStyle);
                    } else {
                        final int marks;
                        Section section = null;

                        final Set<Options> optionSet = new HashSet<>();
                        final Question question = new Question();
                        question.setQuestionCategory(questionCategory);

                        question.setQuestionText(row1.getCell(0).getStringCellValue());
                        if (row1.getCell(6) == null) {
                            question.setDifficultyLevel(DifficultyLevel.EASY.name());
                        } else {
                            question.setDifficultyLevel(row1.getCell(6).getStringCellValue());
                        }
                        for (int j = 1; j <= 4; j++) {
                            final Options options = new Options();
                            if (row1.getCell(j).getCellType() == 0) {
                                options.setOptionText(Integer.toString((int) row1.getCell(j).getNumericCellValue()));
                            } else {
                                options.setOptionText(row1.getCell(j).getStringCellValue());
                            }
                            options.setAnswer(row1.getCell(j).getCellStyle().getFillForegroundColor() != 64);
                            optionSet.add(options);
                        }
                        question.setOptions(optionSet);
                        marks = (int) row1.getCell(5).getNumericCellValue();

                        final Optional<Section> findSection = sectionRepository.findByMarksPerQuestion(marks);

                        if (findSection.isPresent()) {
                            section = findSection.get();
                        }
                        question.setSection(section);
                        questionRepository.save(question);
                    }
                }
            }
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            saveQuestionHistory(byteArrayOutputStream);
            workbook.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error in updating questions Table");
            statusResponse.setMessage("Error in file processing");
            statusResponse.setStatus(HttpStatus.FORBIDDEN);
            statusResponse.setCode(CODE_VALIDATION_ERROR);
        }
        return statusResponse;
    }

    private String validateHeader(XSSFSheet worksheet) {
        final int n = worksheet.getNumMergedRegions();

        final XSSFRow row = worksheet.getRow(1);
        String message = "";
        // Merged region in our excel will always be the first merged region
        final CellRangeAddress range = worksheet.getMergedRegion(0);
        if (range.getFirstRow() != 0 && range.getLastRow() != 0 && !range.isInRange(0, 6)) {
            message = "Improper Merged Region";
        } else if (n > 1) {
            message = "Merged region cannot be more than one";
        } else if (row.getCell(0) == null || !row.getCell(0).getStringCellValue().equalsIgnoreCase("Question")) {
            message = "First column of second row should be a question";
        } else if (row.getCell(1) == null || !row.getCell(1).getStringCellValue().equalsIgnoreCase("Option1")) {
            message = "Second column of second row should be a option";
        } else if (row.getCell(2) == null || !row.getCell(2).getStringCellValue().equalsIgnoreCase("Option2")) {
            message = "Third column of second row should be a option";
        } else if (row.getCell(3) == null || !row.getCell(3).getStringCellValue().equalsIgnoreCase("Option3")) {
            message = "Fourth column of second row should be a option";
        } else if (row.getCell(4) == null || !row.getCell(4).getStringCellValue().equalsIgnoreCase("Option4")) {
            message = "Fifth column of second row should be a option";
        }
        return message;
    }

    public String validateEachRow(XSSFRow row) {
        int flag = 0;
        Section section = new Section();
        final int marks;
        String errorMessage = " ";
        for (int k = 0; k < 6; k++) {
            final Cell cell = row.getCell(k);
            if (cell == null) {
                // throw new OTPException("No cell can be blank");
                errorMessage = "No cell can be blank";
                return errorMessage;
            }
        }

        for (int k = 1; k < 5; k++) {
            for (int j = k + 1; j < 5; j++) {
                if (row.getCell(j).getCellType() == 0 && row.getCell(k).getCellType() == 0) {
                    if (row.getCell(k).getNumericCellValue() == row.getCell(j).getNumericCellValue()) {
                        errorMessage = "Options can't repeat";
                        return errorMessage;
                    }
                } else if (row.getCell(j).getCellType() != 0 && row.getCell(k).getCellType() != 0) {
                    if (row.getCell(k).getStringCellValue().equals(row.getCell(j).getStringCellValue())) {
                        errorMessage = "Options can't repeat";
                        return errorMessage;
                    }
                }
            }
        }
        marks = (int) row.getCell(5).getNumericCellValue();
        final Optional<Section> questionByMarks = sectionRepository.findByMarksPerQuestion(marks);
        if (questionByMarks.isPresent()) {
            section = questionByMarks.get();
        } else {
            errorMessage = "No section found with given marks";
            return errorMessage;
        }

        for (int x = 1; x < 5; x++) {
            if (row.getCell(x).getCellStyle().getFillForegroundColor() != 64) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            errorMessage = "Answer not highlighted";
            return errorMessage;
        }
        return errorMessage;
    }

    public void saveQuestionHistory(ByteArrayOutputStream byt) {
        final byte[] spreadSheetBytes = byt.toByteArray();
        final QuestionHistory questionHistory = new QuestionHistory();
        final AdminUser adminUser = new AdminUser();
        final String username;
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        final Optional<AdminUser> findAdmin = adminUserRepository.findByUserName(username);

        findAdmin.ifPresent(questionHistory::setAdminUser);
        questionHistory.setExcelData(spreadSheetBytes);
        questionHistory.setCreatedBy(username);
        questionHistoryRepository.save(questionHistory);
    }
}
