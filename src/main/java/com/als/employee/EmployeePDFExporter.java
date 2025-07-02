package com.als.employee;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class EmployeePDFExporter {
    public ByteArrayInputStream exportToPDF(List<Employee> employeeList) {
        Document document = new Document(PageSize.A4.rotate()); // Set the page size to A4 in landscape mode
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            MyFooter footer = new MyFooter();
            writer.setPageEvent(footer);

            document.open();

            // Add Content to PDF file
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Paragraph para = new Paragraph("Employee Data", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(8);
            // Add PDF Table Header
            addTableHeader(table);

            // Add employee data to the table
            addEmployeeData(table, employeeList);

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Sr No.", "Name", "Email ID", "Date of Joining", "Designation", "Status", "Department", "Employee Type").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorderWidth(1);
            header.setBackgroundColor(Color.CYAN);
            header.setPhrase(new Phrase(headerTitle, headFont));
            table.addCell(header);
        });
    }

    private void addEmployeeData(PdfPTable table, List<Employee> employeeList) {
    	 int serialNumber = 1;
    	 System.out.print(employeeList);
    	for (Employee employee : employeeList) {
    		PdfPCell srNoCell = new PdfPCell(new Phrase(String.valueOf(serialNumber)));
            srNoCell.setPaddingLeft(4);
            srNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            srNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(srNoCell);

            PdfPCell fullNameCell = new PdfPCell(new Phrase(employee.getFirstName() + " " + employee.getLastName()));
            fullNameCell.setPaddingLeft(4);
            fullNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fullNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(fullNameCell);

            PdfPCell emailIdCell = new PdfPCell(new Phrase(employee.getEmailId()));
            emailIdCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emailIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(emailIdCell);

            PdfPCell dateOfJoiningCell = new PdfPCell(new Phrase(String.valueOf(employee.getDateOfJoining())));
            dateOfJoiningCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dateOfJoiningCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(dateOfJoiningCell);

            PdfPCell designationCell = new PdfPCell(new Phrase(employee.getDesignation()));
            designationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            designationCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(designationCell);
            
            PdfPCell statusCell = new PdfPCell(new Phrase(employee.getStatus()));
            statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(statusCell);

            PdfPCell departmentCell = new PdfPCell(new Phrase(employee.getDepartment().getDepartmentName()));
            departmentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            departmentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(departmentCell);
//            
//            Long c = employee.getContactNumber();
//            if(c != null) {
//            PdfPCell contactNumberCell = new PdfPCell(new Phrase(employee.getEmergencyContactNumber()));
//            contactNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            contactNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(contactNumberCell);
//            }else {
//            	System.out.println(" no contact number : "+ c);
//            }

            PdfPCell employeeTypeCell = new PdfPCell(new Phrase(employee.getEmployeeType()));
            employeeTypeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            employeeTypeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(employeeTypeCell);
    
            serialNumber++;
            
        }
    }

    private static class MyFooter extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            footerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            footerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            footerTable.setSpacingBefore(10);
            document.add(new Paragraph("\n"));

            PdfPCell footerCell = new PdfPCell(new Phrase("Page " + writer.getPageNumber()));
            footerCell.setBorder(0);
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Center-align the footer cell content
            footerTable.addCell(footerCell);

            try {
                document.add(footerTable);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
}
