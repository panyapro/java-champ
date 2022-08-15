package cup.online.javachamp.controller;

import cup.online.javachamp.dto.TableDTO;
import cup.online.javachamp.service.DynamicTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/table")
public class TableManagerController {

    @Autowired
    private DynamicTableService dynamicTableService;

    @PostMapping("/create-table")
    public ResponseEntity<Void> create(@RequestBody TableDTO tableDTO) {
        log.debug("create table:" + tableDTO);
        return new ResponseEntity<>(dynamicTableService.create(tableDTO).getHttpStatus());
    }

    @GetMapping("/get-table-by-name/{name}")
    public TableDTO getByName(@PathVariable String name) {
        log.debug("getByName table:" + name);
        return dynamicTableService.getStructureByName(name.toUpperCase());
    }

    @DeleteMapping("/drop-table/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name){
        log.debug("delete table:" + name);
        return new ResponseEntity<>(dynamicTableService.remove(name.toUpperCase()).getHttpStatus());
    }
}
