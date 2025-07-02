package com.als.kra;

import com.als.dto.KRADTO;
import com.als.employee.Employee;
import com.als.employee.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KRAServiceImpl implements KRAService {

    @Autowired
    private KRARepository kraRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<KRADTO> getKRAsByEmployeeId(Long employeeId) {
        return kraRepository.findByEmployeeId(employeeId)
                .stream()
                .map(kra -> {
                    KRADTO dto = modelMapper.map(kra, KRADTO.class);
                    if (kra.getEmployee() != null) {
                        dto.setFirstName(kra.getEmployee().getFirstName());
                        dto.setLastName(kra.getEmployee().getLastName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public KRADTO getKRAById(Long kraId) {
        KRA kra = kraRepository.findById(kraId)
                .orElseThrow(() -> new KRAException("KRA not found for ID: " + kraId));
        KRADTO dto = modelMapper.map(kra, KRADTO.class);
        if (kra.getEmployee() != null) {
            dto.setFirstName(kra.getEmployee().getFirstName());
            dto.setLastName(kra.getEmployee().getLastName());
        }
        return dto;
    }


    @Override
    public KRADTO saveKRA(KRADTO kraDTO) {
        Employee employee = employeeRepository.findById(kraDTO.getEmployeeId())
                .orElseThrow(() -> new KRAException("Employee not found for ID: " + kraDTO.getEmployeeId()));

        KRA kra = modelMapper.map(kraDTO, KRA.class);

        // 🔐 Override unsafe client input
        kra.setEmployee(employee);
        kra.setStatus("Pending");                // ✅ Set manually
        kra.setCreatedDate(LocalDate.now());    // ✅ Set manually

        KRA savedKRA = kraRepository.save(kra);
        return modelMapper.map(savedKRA, KRADTO.class);
    }


    @Override
    public KRADTO updateKRA(KRADTO kraDTO) {
        KRA existingKRA = kraRepository.findById(kraDTO.getId())
                .orElseThrow(() -> new KRAException("KRA not found for ID: " + kraDTO.getId()));

        LocalDate existingCreatedDate = existingKRA.getCreatedDate();

        modelMapper.map(kraDTO, existingKRA);

        existingKRA.setCreatedDate(existingCreatedDate);

        // 🔓 Allow status update if provided
        if (kraDTO.getStatus() != null) {
            existingKRA.setStatus(kraDTO.getStatus());
        }

        KRA updatedKRA = kraRepository.save(existingKRA);
        return modelMapper.map(updatedKRA, KRADTO.class);
    }



    @Override
    public void deleteKRA(Long kraId) {
        if (!kraRepository.existsById(kraId)) {
            throw new KRAException("KRA not found for ID: " + kraId);
        }
        kraRepository.deleteById(kraId);
    }

    @Override
    public boolean existsByEmployeeAndFinancialYear(Long employeeId, String financialYear) {
        return kraRepository.existsByEmployeeIdAndFinancialYear(employeeId, financialYear);
    }

    @Override
    public List<KRADTO> getAllKRAs() {
        return kraRepository.findAll()
                .stream()
                .map(kra -> {
                    KRADTO dto = modelMapper.map(kra, KRADTO.class);
                    if (kra.getEmployee() != null) {
                        dto.setFirstName(kra.getEmployee().getFirstName());
                        dto.setLastName(kra.getEmployee().getLastName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
