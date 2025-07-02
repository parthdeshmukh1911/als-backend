package com.als.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.als.attendance.AttendanceException;
import com.als.dto.EmployeeDTO;
import com.als.dto.RosterDTO;
import com.als.employee.Employee;
import com.als.publicholiday.PublicHoliday;
import com.als.roster.Roster;


public class ExcelHelper {


	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	//Related to PublicHoliday class
	static String[] publicHolidayHeaders = {"Id", "Date","Reason"};
	static String SHEET = "PublicHoliday";

	//Related to Employee Class
	static String[] EmployeeHeaders = {"Id", "FirstName", "LastName", "Email", "DateOfJoining", "Designation", "JobRole", "Department", "Status", "DOB", "Country", "MaritialStatus", "Gender", "ContactNumber", "EmergencyContact", "Address", "EmployeeType", "ManagerId"};
	static String employeeSheetName = "Employee";
	static Workbook workBook;
	
	//Related to Roster class
	static String[] RosterHeaders = {"RosterId","EmployeeName","RosterDate","RosterType"};
	static String rosterSheetName = "Roster";


	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<PublicHoliday> excelToPublicHoliday(InputStream is) throws IOException{
		try {
			workBook = new XSSFWorkbook(is);

			Sheet sheet = (Sheet) workBook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<PublicHoliday> holidaysList = new ArrayList<PublicHoliday>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellInRow = currentRow.iterator();

				PublicHoliday holiday = new PublicHoliday();

				int cellIdx = 0;
				while(cellInRow.hasNext()) {
					Cell currentCell = cellInRow.next();

					switch(cellIdx) {
					case 1:
						holiday.setHolidayDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
						break;
					case 2:
						holiday.setHolidayName(currentCell.getStringCellValue());
						break;
					default :
						break;
					}
					cellIdx++;
				}
				holidaysList.add(holiday);
			}

			return holidaysList;	
		}catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}finally {
			workBook.close();
		}
	}


	public static ByteArrayInputStream publicHolidayToExcel(List<PublicHoliday> holidays) throws IOException {

		try (Workbook workbook = new XSSFWorkbook(); 
				ByteArrayOutputStream out = new ByteArrayOutputStream();) 
		{
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < publicHolidayHeaders.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(publicHolidayHeaders[col]);
			}

			int rowIdx = 1;
			for (PublicHoliday holiday : holidays) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(holiday.getPublicHolidayId());
				row.createCell(1).setCellValue(holiday.getHolidayDate());
				row.createCell(2).setCellValue(holiday.getHolidayName());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}

	}


	public static ByteArrayInputStream employeeToExcelConvertor(List<Employee> employees) {

		try (Workbook workbook = new XSSFWorkbook(); 
				ByteArrayOutputStream out = new ByteArrayOutputStream();) 
		{
			Sheet sheet = workbook.createSheet(employeeSheetName);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < EmployeeHeaders.length-1; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(EmployeeHeaders[col]);
			}

			int rowIdx = 1;
			for (Employee employee : employees) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(employee.getId());
				row.createCell(1).setCellValue(employee.getFirstName());
				row.createCell(2).setCellValue(employee.getLastName());
				row.createCell(3).setCellValue(employee.getEmailId());

				Cell dateCell=row.createCell(4);
				CellStyle dateCellStyle = workbook.createCellStyle();
				CreationHelper creationHelper = workbook.getCreationHelper();
				dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				dateCell.setCellValue(employee.getDateOfJoining());
				dateCell.setCellStyle(dateCellStyle);

				row.createCell(5).setCellValue(employee.getDesignation());
				row.createCell(6).setCellValue(employee.getJobTitle());
				row.createCell(7).setCellValue(employee.getDepartment().getDepartmentName());
				row.createCell(8).setCellValue(employee.getStatus());

				Cell dataCell9 = row.createCell(9);
				dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				dataCell9.setCellValue(employee.getDateOfBirth());
				dataCell9.setCellStyle(dateCellStyle);

				row.createCell(10).setCellValue(employee.getCountry());
				row.createCell(11).setCellValue(employee.getMaritalStatus());
				row.createCell(12).setCellValue(employee.getGender());
				row.createCell(13).setCellValue(employee.getContactNumber());
				row.createCell(14).setCellValue(employee.getEmergencyContactNumber());
				row.createCell(15).setCellValue(employee.getPermanentAddress());
				row.createCell(16).setCellValue(employee.getEmployeeType());
				
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	//Convert all the excel data into a list format and send back 
	public static List<EmployeeDTO> excelToEmployeeConvertor(InputStream is ) {

		try {
			workBook =new XSSFWorkbook(is);

			Sheet sheet = workBook.getSheet(employeeSheetName);
			Iterator<Row> rows = sheet.iterator();

			List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				//Skip headers
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cellsInRow = currentRow.iterator();

				EmployeeDTO employee = new EmployeeDTO();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {
					case 0:
						break;
					case 1:
						String firstName = currentCell.getStringCellValue();
						if (firstName.matches("[a-zA-Z]+")) {
							employee.setFirstName(firstName);
							System.out.println("This is case 1");
						} else {
							throw new AttendanceException("Invalid string. The first name should only contain letters.");
						}

						System.out.println("this is case 1");
						break;
					case 2:
						String lastName = currentCell.getStringCellValue();
						if(lastName.matches("[a-zA-Z]+")) {
							employee.setLastName(lastName);
						}else {
							throw new AttendanceException("Invalid string. The Last name should only contain letters.");
						}
						employee.setLastName(currentCell.getStringCellValue());
						System.out.println("this is case 2");
						break;
					case 3:
						String email = currentCell.getStringCellValue();
						
						if(email.matches("^[a-zA-Z0-9%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
							employee.setEmailId(email);
						}else {
							throw new AttendanceException("Invalid string. The Email should be valid.");
						}
						System.out.println("this is case 3");
						break;
					case 4:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							employee.setDateOfJoining(currentCell.getLocalDateTimeCellValue().toLocalDate());
						} else {
							System.out.println("this is a string cell");
							throw new AttendanceException("Invalid Date Of joining. The DOJ should be valid.");
						}
						break;
					case 5:
						String designation = currentCell.getStringCellValue();
						if (designation.matches("\\b(?:[a-zA-Z]+ ?)+\\b")) {
							employee.setDesignation(designation);
							System.out.println("This is case 5");
						} else {
							throw new AttendanceException("Invalid string. The designation should only contain letters.");
						}
						break;

					case 6:
						String jobTitle = currentCell.getStringCellValue();
						if (jobTitle.matches("\\b(?:[a-zA-Z]+ ?)+\\b")) {
							employee.setJobTitle(jobTitle);
							System.out.println("This is case 6");
						} else {
							throw new AttendanceException("Invalid string. The job title should only contain letters.");
						}
						break;
					case 7:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							employee.setDepartmentId((long) currentCell.getNumericCellValue());
							System.out.println("this is case 7");
						} else {
							// Handle the case when the cell contains a string instead of a number
							System.out.println("department Id is not feteched : " + currentCell.getNumericCellValue() );
							throw new AttendanceException("Invalid Date Of joining. The DOJ should be valid.");
						}
						break;
					case 8:
						String status = currentCell.getStringCellValue();
						if (status.equalsIgnoreCase("Active") || status.equalsIgnoreCase("Inactive")) {
							employee.setStatus(status);
							System.out.println("This is case 8");
						} else {
							throw new AttendanceException("Invalid status. The status should be 'Active' or 'Inactive'.");
						}
						break;
					case 9:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							LocalDate dob = currentCell.getLocalDateTimeCellValue().toLocalDate();
							LocalDate currentDate = LocalDate.now();
							int age = Period.between(dob, currentDate).getYears();
							if(age>18) {
								employee.setDateOfBirth(dob);
							}else {
								throw new AttendanceException("Invalid Date Of Birth. The DOB should be > 18.");
							}
							System.out.println("this is case 9");
						} else {
							throw new AttendanceException("Invalid Date Of Birth. The DOB should be valid & >18.");
						}
						break;
					case 10:
						employee.setCountry(currentCell.getStringCellValue());
						System.out.println("this is case 10");
						break;
					case 11:
						employee.setMaritalStatus(currentCell.getStringCellValue());
						System.out.println("this is case 11");
						break;
					case 12:
						String gender = currentCell.getStringCellValue();
						if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Others")) {
							employee.setGender(gender);
							System.out.println("This is case 11");
						} else {
							throw new AttendanceException("Invalid gender. The gender should be 'Male', 'Female', or 'Others'.");
						}
						System.out.println("this is case 12");
						break;
					case 13:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							long contactNumber = (long) currentCell.getNumericCellValue();
							if (String.valueOf(contactNumber).length() == 10) {
								employee.setContactNumber(contactNumber);
								System.out.println("This is case 9");
							} else {
								throw new AttendanceException("Invalid contact number. The contact number should be 10 digits.");
							}
						} else {
							throw new AttendanceException("Invalid contact number. The contact number should be a numeric value.");
						}
						System.out.println("this is case 13");
						break;
					case 14:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							long contactNumber = (long) currentCell.getNumericCellValue();
							if (String.valueOf(contactNumber).length() == 10) {
								employee.setEmergencyContactNumber(contactNumber);
								System.out.println("This is case 9");
							} else {
								throw new AttendanceException("Invalid contact number. The contact number should be 10 digits.");
							}
						} else {
							throw new AttendanceException("Invalid contact number. The contact number should be a numeric value.");
						}
						System.out.println("this is case 14");
						break;
					case 15:
						employee.setPermanentAddress(currentCell.getStringCellValue());
						System.out.println("this is case 15");
						break;
					case 16:
						String employeeType = currentCell.getStringCellValue();
						if (employeeType.equalsIgnoreCase("Hybrid") || employeeType.equalsIgnoreCase("Virtual")) {
							employee.setEmployeeType(employeeType);
							System.out.println("This is case 10");
						} else {
							throw new AttendanceException("Invalid employee type. The employee type should be 'Hybrid' or 'Virtual'.");
						}
						System.out.println("this is case 16");
						break;
					case 17:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							employee.setManagerId((long) currentCell.getNumericCellValue());
							System.out.println("this is case 17");
						} else {
							// Handle the case when the cell contains a string instead of a number
							System.out.println(" manager id : " + currentCell.getNumericCellValue());
						}
						break;
					default:
					}
					cellIdx++;
				}
				employees.add(employee);
			}
			return employees;
		}catch(IOException o) {
			throw new RuntimeException("fail to parse Excel file: " + o.getMessage());
		}
	}
	
	public static List<RosterDTO> excelToRoster(InputStream inputStream) throws IOException{
		
		try {
			workBook = new XSSFWorkbook(inputStream);

			Sheet sheet = (Sheet) workBook.getSheet(rosterSheetName);
			Iterator<Row> rows = sheet.iterator();

			List<RosterDTO> rosterList = new ArrayList<RosterDTO>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellInRow = currentRow.iterator();

				RosterDTO roster = new RosterDTO();

				int cellIdx = 0;
				while(cellInRow.hasNext()) {
					Cell currentCell = cellInRow.next();

					switch(cellIdx) {
					case 1:
						roster.setEmployeeId((long)currentCell.getNumericCellValue());
						break;
					case 2:
						roster.setRosterDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
						break;
					case 3:
						roster.setRosterType(currentCell.getStringCellValue());
						break;
					default :
						break;
					}
					cellIdx++;
				}
				rosterList.add(roster);
			}

			return rosterList;	
		}catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}finally {
			workBook.close();
		}
	}
	
	public static ByteArrayInputStream rosterToExcel(List<Roster> rosterList) {
		
		try (Workbook workbook = new XSSFWorkbook(); 
				ByteArrayOutputStream out = new ByteArrayOutputStream();) 
		{
			Sheet sheet = workbook.createSheet(rosterSheetName);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < RosterHeaders.length-1; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(RosterHeaders[col]);
			}

			int rowIdx = 1;
			for (Roster roster : rosterList) {
				Row row = sheet.createRow(rowIdx++);

				row.createCell(0).setCellValue(roster.getRosterId());
				row.createCell(1).setCellValue(roster.getEmployee().getId()+"("+roster.getEmployee().getFirstName()+")");
				row.createCell(2).setCellValue(roster.getRosterType());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

}
