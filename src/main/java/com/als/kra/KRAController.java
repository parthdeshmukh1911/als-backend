package com.als.kra;

import com.als.dto.KRADTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kra")
@CrossOrigin
public class KRAController {

    private final KRAService kraService;

    @Autowired
    public KRAController(KRAService kraService) {
        this.kraService = kraService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<KRADTO>> getKRAsByEmployeeId(@PathVariable Long employeeId) {
        List<KRADTO> krs = kraService.getKRAsByEmployeeId(employeeId);
        return ResponseEntity.ok(krs);
    }

    @GetMapping("/{kraId}")
    public ResponseEntity<KRADTO> getKRAById(@PathVariable Long kraId) {
        KRADTO kra = kraService.getKRAById(kraId);
        return ResponseEntity.ok(kra);
    }

    @PostMapping
    public ResponseEntity<KRADTO> saveKRA(@RequestBody KRADTO kraDTO) {
        KRADTO savedKra = kraService.saveKRA(kraDTO);
        return ResponseEntity.ok(savedKra);
    }

    @PutMapping
    public ResponseEntity<KRADTO> updateKRA(@RequestBody KRADTO kraDTO) {
        KRADTO updatedKra = kraService.updateKRA(kraDTO);
        return ResponseEntity.ok(updatedKra);
    }

    @DeleteMapping("/{kraId}")
    public ResponseEntity<Void> deleteKRA(@PathVariable Long kraId) {
        kraService.deleteKRA(kraId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<KRADTO>> getAllKRAs() {
        return ResponseEntity.ok(kraService.getAllKRAs());
    }

    @GetMapping("/exists/{employeeId}/{financialYear}")
    public ResponseEntity<Boolean> existsByEmployeeAndFinancialYear(
            @PathVariable Long employeeId,
            @PathVariable String financialYear) {
        return ResponseEntity.ok(kraService.existsByEmployeeAndFinancialYear(employeeId, financialYear));
    }
}
