package com.als.kra;

import com.als.dto.KRADTO;
import java.util.List;

public interface KRAService {

    List<KRADTO> getKRAsByEmployeeId(Long employeeId);

    KRADTO getKRAById(Long kraId);

    KRADTO saveKRA(KRADTO kraDTO);

    KRADTO updateKRA(KRADTO kraDTO);

    void deleteKRA(Long kraId);

    boolean existsByEmployeeAndFinancialYear(Long employeeId, String financialYear);

    List<KRADTO> getAllKRAs();
}
