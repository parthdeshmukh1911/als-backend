package com.als.publicholiday;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.als.dto.PublicHolidayDTO;

public interface PublicHolidayService {
    List<PublicHoliday> getAllPublicHolidays();

    PublicHoliday getPublicHolidayById(Long publicHolidayId);

    PublicHoliday createPublicHoliday(PublicHolidayDTO publicHoliday);

    PublicHoliday updatePublicHoliday(Long publicHolidayId, PublicHolidayDTO publicHoliday);

    void deletePublicHoliday(Long publicHolidayId);
    
    public void save(MultipartFile file);
    
    public ByteArrayInputStream load() throws IOException;
    
    PublicHoliday getByDate(LocalDate date);
}

