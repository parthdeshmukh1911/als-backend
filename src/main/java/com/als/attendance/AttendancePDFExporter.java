package com.als.attendance;

import com.als.employee.Employee;
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
public class AttendancePDFExporter {
    public ByteArrayInputStream exportToPDF(List<Attendance> attendanceList) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            MyFooter footer = new MyFooter();
            writer.setPageEvent(footer);

            document.open();

            // Add Content to PDF file
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Paragraph para = new Paragraph("Employee Attendance", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            // Add PDF Table Header
            addTableHeader(table);

            // Add attendance data to the table
            addAttendanceData(table, attendanceList);

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Sr No.", "Name", "Status", "Date", "Time", "Work Mode").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBorderWidth(1);
            header.setBackgroundColor(Color.CYAN);
            header.setPhrase(new Phrase(headerTitle, headFont));
            table.addCell(header);
        });
    }

    private void addAttendanceData(PdfPTable table, List<Attendance> attendanceList) {
    	int serialNumber = 1;
        for (Attendance attendance : attendanceList) {
            PdfPCell srNoCell = new PdfPCell(new Phrase(String.valueOf(serialNumber)));
            srNoCell.setPaddingLeft(4);
            srNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            srNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(srNoCell);

            Employee employee = attendance.getEmployee();
            PdfPCell nameCell = new PdfPCell(new Phrase(employee.getFirstName() + " " + employee.getLastName()));
            nameCell.setPaddingLeft(4);
            nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(nameCell);

            PdfPCell statusCell = new PdfPCell(new Phrase(String.valueOf(attendance.getAttendanceStatus())));
            statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(statusCell);

            PdfPCell dateCell = new PdfPCell(new Phrase(String.valueOf(attendance.getAttendanceDate())));
            dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(dateCell);

            PdfPCell timeCell = new PdfPCell(new Phrase(String.valueOf(attendance.getAttendanceTime())));
            timeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            timeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(timeCell);

            PdfPCell workModeCell = new PdfPCell(new Phrase(attendance.getWorkMode()));
            workModeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            workModeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(workModeCell);
            
            serialNumber ++;
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