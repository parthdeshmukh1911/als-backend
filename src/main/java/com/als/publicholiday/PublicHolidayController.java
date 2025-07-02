package com.als.publicholiday;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.als.converters.ExcelHelper;
import com.als.dto.PublicHolidayDTO;

@RestController
@RequestMapping("/publicholidays")
@CrossOrigin
public class PublicHolidayController {
    private final PublicHolidayService publicHolidayService;

    @Autowired
    public PublicHolidayController(PublicHolidayService publicHolidayService) {
        this.publicHolidayService = publicHolidayService;
    }
    
    @PostMapping ("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";

	    if (ExcelHelper.hasExcelFormat(file)) {
	      try {
	    	  publicHolidayService.save(file);

	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
	        return ResponseEntity.status(HttpStatus.OK).body(message);
	      } catch (Exception e) {
	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	        System.out.println(e);
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
	      }
	    }

	    message = "Please upload an excel file!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	  }
    
    @GetMapping("/download")
	  public ResponseEntity<Resource> getFile() throws IOException {
	    String filename = "PublicHoliday.xlsx";
	    InputStreamResource file = new InputStreamResource(publicHolidayService.load());

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }

    @GetMapping
    public ResponseEntity<List<PublicHoliday>> getAllPublicHolidays() {
        try {
            List<PublicHoliday> publicHolidays = publicHolidayService.getAllPublicHolidays();
            return new ResponseEntity<>(publicHolidays, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception or return an appropriate error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{publicHolidayId}")
    public ResponseEntity<PublicHoliday> getPublicHolidayById(@PathVariable Long publicHolidayId) {
        try {
            PublicHoliday publicHoliday = publicHolidayService.getPublicHolidayById(publicHolidayId);
            if (publicHoliday != null) {
                return new ResponseEntity<>(publicHoliday, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle the exception or return an appropriate error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<PublicHoliday> createPublicHoliday(@RequestBody PublicHolidayDTO publicHolidayDto) {
        try {
            PublicHoliday createdPublicHoliday = publicHolidayService.createPublicHoliday(publicHolidayDto);
            return new ResponseEntity<>(createdPublicHoliday, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle the exception or return an appropriate error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{publicHolidayId}")
    public ResponseEntity<PublicHoliday> updatePublicHoliday(
            @PathVariable Long publicHolidayId,
            @RequestBody PublicHolidayDTO publicHolidayDto
    ) {
        try {
            PublicHoliday updatedPublicHoliday = publicHolidayService.updatePublicHoliday(publicHolidayId, publicHolidayDto);
            if (updatedPublicHoliday != null) {
                return new ResponseEntity<>(updatedPublicHoliday, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle the exception or return an appropriate error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{publicHolidayId}")
    public ResponseEntity<Void> deletePublicHoliday(@PathVariable Long publicHolidayId) {
        try {
            publicHolidayService.deletePublicHoliday(publicHolidayId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Handle the exception or return an appropriate error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
