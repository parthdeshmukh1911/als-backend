package com.als.publicholiday;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.als.dto.PublicHolidayDTO;





@Service
public class PublicHolidayServiceImpl implements PublicHolidayService {
    private final PublicHolidayRepository publicHolidayRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PublicHolidayServiceImpl(PublicHolidayRepository publicHolidayRepository, ModelMapper modelMapper) {
        this.publicHolidayRepository = publicHolidayRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PublicHoliday> getAllPublicHolidays() {
        try {
            List<PublicHoliday> publicHolidays = publicHolidayRepository.findAll();
            return publicHolidays;
        } catch (Exception e) {
            // Handle the exception or rethrow if necessary
            throw new RuntimeException("Failed to retrieve public holidays", e);
        }
    }

    @Override
    public PublicHoliday getPublicHolidayById(Long publicHolidayId) {
        try {
            Optional<PublicHoliday> publicHoliday = publicHolidayRepository.findById(publicHolidayId);
            return publicHoliday.get();
        } catch (Exception e) {
            // Handle the exception or rethrow if necessary
            throw new RuntimeException("Failed to retrieve public holiday with ID: " + publicHolidayId, e);
        }
    }

    @Override
    public PublicHoliday createPublicHoliday(PublicHolidayDTO publicHolidayDto) {
        try {
            PublicHoliday publicHoliday = modelMapper.map(publicHolidayDto, PublicHoliday.class);
            PublicHoliday createdPublicHoliday = publicHolidayRepository.save(publicHoliday);
            return createdPublicHoliday;
        } catch (Exception e) {
            // Handle the exception or rethrow if necessary
            throw new RuntimeException("Failed to create public holiday", e);
        }
    }

    @Override
    public PublicHoliday updatePublicHoliday(Long publicHolidayId, PublicHolidayDTO publicHolidayDto) {
        try {
            PublicHoliday existingPublicHoliday = getPublicHolidayById(publicHolidayId);
            if (existingPublicHoliday != null) {
                PublicHoliday publicHoliday = modelMapper.map(publicHolidayDto, PublicHoliday.class);
                publicHoliday.setPublicHolidayId(publicHolidayId);
                PublicHoliday updatedPublicHoliday = publicHolidayRepository.save(publicHoliday);
                return updatedPublicHoliday;
            }
            return null;
        } catch (Exception e) {
            // Handle the exception or rethrow if necessary
            throw new RuntimeException("Failed to update public holiday with ID: " + publicHolidayId, e);
        }
    }

    @Override
    public void deletePublicHoliday(Long publicHolidayId) {
        try {
            publicHolidayRepository.deleteById(publicHolidayId);
        } catch (Exception e) {
            // Handle the exception or rethrow if necessary
            throw new RuntimeException("Failed to delete public holiday with ID: " + publicHolidayId, e);
        }
    }
    
    @Override
    public void save(MultipartFile file) {
		try {
			List<PublicHoliday> holidayList = com.als.converters.ExcelHelper.excelToPublicHoliday(file.getInputStream());
			publicHolidayRepository.saveAll(holidayList);
		}catch(Exception e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
    
    @Override
    public ByteArrayInputStream load() throws IOException {
	    List<PublicHoliday> holidays = publicHolidayRepository.findAll();

	    ByteArrayInputStream in = com.als.converters.ExcelHelper.publicHolidayToExcel(holidays);
	    return in;
	  }
    
    @Override
    public PublicHoliday getByDate(LocalDate date) {
      PublicHoliday holiday = publicHolidayRepository.findByHolidayDate(date);
      if(holiday!=null) {
        return holiday;
      }else {
        return null;
      }
    }
}
