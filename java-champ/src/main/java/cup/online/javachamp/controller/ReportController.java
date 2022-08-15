package cup.online.javachamp.controller;

import cup.online.javachamp.dto.ReportDTO;
import cup.online.javachamp.dto.TableDTO;
import cup.online.javachamp.entity.ReportEntity;
import cup.online.javachamp.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/create-report")
    public ResponseEntity<Void> create(@RequestBody ReportDTO reportDTO) {
        log.debug("create report:" + reportDTO);
        return new ResponseEntity<>(reportService.create(reportDTO).getHttpStatus());
    }

    @GetMapping("/get-report-by-id/{id}")
    public ResponseEntity<ReportDTO> get(@PathVariable Integer id) {
        log.debug("get report:" + id);
        ReportDTO reportDTO = reportService.getById(id);
        return new ResponseEntity<>(
                reportDTO,
                isNull(reportDTO) ?
                        HttpStatus.NOT_ACCEPTABLE :
                        HttpStatus.CREATED
        );
    }


}
