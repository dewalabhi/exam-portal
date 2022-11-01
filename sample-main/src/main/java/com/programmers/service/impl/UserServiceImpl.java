package com.programmers.service.impl;

import com.programmers.common.Constant;
import com.programmers.globalexception.EntityNotFoundException;
import com.programmers.globalexception.InvalidEmailException;
import com.programmers.globalexception.InvalidEntityException;
import com.programmers.model.db.User;
import com.programmers.model.response.StatusResponse;
import com.programmers.repository.UserRepository;
import com.programmers.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        final Optional<User> findUser = Optional.ofNullable(userRepository.findByEmailId(user.getEmailId()));
        final boolean userActive = findUser.get().getIsActive();
        if (!userActive) {
            validateUser(user);
            user.setIsActive(true);
            user.setId(findUser.get().getId());
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found with this email - " + user.getEmailId());
        }
    }

    @Override
    public void validateUser(User user) {

        final String emailId = (user.getEmailId() == null) ? "" : user.getEmailId().trim();
        final String firstName = (user.getFirstName() == null) ? "" : user.getFirstName().trim();
        final String lastName = (user.getLastName() == null) ? "" : user.getLastName().trim();
        final String gender = (user.getGender() == null) ? "" : user.getGender().trim();
        final int age = (user.getAge() < 0 || user.getAge() > 50) ? -1 : user.getAge();
        final String college = (user.getCollege() == null) ? "" : user.getCollege().trim();
        final String skills = (user.getSkills() == null) ? "" : user.getSkills().trim();
        final String degree = (user.getDegree() == null) ? "" : user.getDegree().trim();
        final int graduationYear = user.getGraduationYear();
        final String mobileNumber = (user.getMobileNumber() == null) ? "" : user.getMobileNumber().trim();
        final String country = (user.getCountry() == null) ? "" : user.getCountry().trim();
        final String state = (user.getState() == null) ? "" : user.getState().trim();
        final String city = (user.getCity() == null) ? "" : user.getCity().trim();
        final double graduationPercentage = user.getGraduationPercentage();

        final List<String> errorMessage = new ArrayList<>();

        if (firstName.equalsIgnoreCase("") || !firstName.matches("^[a-zA-Z ]*$")) {
            errorMessage.add("First name required");
        }
        if (lastName.equalsIgnoreCase("") || !lastName.matches("^[a-zA-Z ]*$")) {
            errorMessage.add("Last name required");
        }
        if (gender.equalsIgnoreCase("") || !gender.matches("^[a-zA-Z]*$")) {
            errorMessage.add("Gender required");
        }
        if (age > 60 || age < 18) {
            errorMessage.add("Age must be between 18 to 60");
        }
        if (college.equalsIgnoreCase("")) {
            errorMessage.add("College required");
        }
        if (skills.equalsIgnoreCase("")) {
            errorMessage.add("At least one skill required");
        }
        if (degree.equalsIgnoreCase("")) {
            errorMessage.add("Degree required");
        }

        final int gradYear = LocalDateTime.now().getYear() + 3;

        if (!(1975 < graduationYear && graduationYear <= gradYear)) {
            errorMessage.add("Graduation year can't be greater than " + gradYear);
        }
        if (!mobileNumber.matches("\\d{3}-\\d{3}-\\d{4}")) {
            errorMessage.add("Mobile number must be 10 digit [XXX-XXX-XXXX]");
        }
        if (country.equalsIgnoreCase("")) {
            errorMessage.add("Country required");
        }
        if (state.equalsIgnoreCase("")) {
            errorMessage.add("State required");
        }
        if (city.equalsIgnoreCase("")) {
            errorMessage.add("City required");
        }
        if (Double.compare(graduationPercentage, 40) < 0) {
            errorMessage.add("Graduation Percentage can't be less than 40%");
        }
        final boolean validEmail = validateEmail(emailId);
        if (!validEmail) {
            errorMessage.add("Email required or invalid");
        }

        final Set<String> skillSet = new HashSet<>();
        final String[] arrSplit = skills.split(",");
        for (String s : arrSplit) {
            if (!skillSet.contains(s)) {
                skillSet.add(s);
            } else {
                throw new InvalidEntityException("Skill-set must be unique");
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new InvalidEntityException("Invalid User detail - " + errorMessage);
        }
    }

    @Override
    public Boolean getTokenStatus(String email) {
        final Optional<User> findUser = Optional.ofNullable(userRepository.findByEmailId(email));
        return findUser.isPresent() && !findUser.get().getIsActive();
    }

    public StatusResponse createUser(User user) {
        StatusResponse statusResponse = new StatusResponse();
        statusResponse = validate(user);

        if (statusResponse.getCode() == Constant.CODE_SUCCESS) {
            userRepository.save(user);
        }
        return statusResponse;
    }

    public User getUser(long id) {
        final Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new EntityNotFoundException("User Not Found with - " + id);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Boolean checkUserDetailSaveOrNot(String email) {
        final Optional<User> findUser = Optional.ofNullable(userRepository.findByEmailId(email));
        return findUser.isPresent() && findUser.get().getIsActive();
    }

    @Override
    public Boolean validateEmail(String email) {
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(emailPattern);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public ByteArrayInputStream downloadCandidate(List<User> userList) throws IOException {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final Sheet sheet = workbook.createSheet("Candidate Details");

            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            final CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            final Row headerRow = sheet.createRow(0);
            sheet.createFreezePane(0, 1);

            final String[] columns = {"First Name", "Middle Name", "Last Name", "Email", "Mobile Number", "Gender", "Age",
                    "Country", "City", "State", "College", "Skill Set", "Degree", "Passing Year",
                    "Graduation Percentage", "Status"};

            int rowIdx = 0;
            // Header Row
            for (int col = 0; col < columns.length; col++) {
                final Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerCellStyle);
            }

            for (User user : userList) {
                final Row row = sheet.createRow(++rowIdx);
                row.createCell(0).setCellValue(user.getFirstName());
                row.createCell(1).setCellValue(user.getMiddleName());
                row.createCell(2).setCellValue(user.getLastName());
                row.createCell(3).setCellValue(user.getEmailId());
                row.createCell(4).setCellValue(user.getMobileNumber());
                row.createCell(5).setCellValue(user.getGender());
                row.createCell(6).setCellValue(user.getAge());
                row.createCell(7).setCellValue(user.getCountry());
                row.createCell(8).setCellValue(user.getCity());
                row.createCell(9).setCellValue(user.getState());
                row.createCell(10).setCellValue(user.getCollege());
                row.createCell(11).setCellValue(user.getSkills());
                row.createCell(12).setCellValue(user.getDegree());
                row.createCell(13).setCellValue(user.getGraduationYear());
                row.createCell(14).setCellValue(user.getGraduationPercentage());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public StatusResponse validate(User user) {
        final StatusResponse statusResponse = new StatusResponse();
        statusResponse.setCode(Constant.CODE_SUCCESS);
        statusResponse.setMessage("User Table updated");
        statusResponse.setStatus(HttpStatus.OK);
        return statusResponse;
    }

}
