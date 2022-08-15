package cup.online.javachamp.controller;

import cup.online.javachamp.dto.QueryDTO;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/table-query")
public class QueryManagerController {


    @Autowired
    private QueryService queryService;

    @PostMapping("/add-new-query-to-table")
    public ResponseEntity<Void> createQuery(@RequestBody QueryDTO query){
        log.debug("createQuery: " + query);
        return new ResponseEntity<>(
                queryService.create(
                        QueryEntity.builder().query(query.getQuery())
                                .tableName(query.getTableName())
                                .queryId(query.getQueryId())
                                .build())
                        .getHttpStatus()
        );
    }

    @PutMapping("/modify-query-in-table")
    public ResponseEntity<Void> modifyQuery(@RequestBody QueryDTO query){
        log.debug("modifyQuery: " + query);
        return new ResponseEntity<>(
                queryService.modify(
                                QueryEntity.builder().query(query.getQuery())
                                        .tableName(query.getTableName())
                                        .queryId(query.getQueryId())
                                        .build())
                        .getHttpStatus()
        );
    }

    @DeleteMapping("/delete-table-query-by-id/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer queryId){
        log.debug("delete: " + queryId);
        return new ResponseEntity<>(queryService.remove(queryId).getHttpStatus());
    }

    @GetMapping("/execute-table-query-by-id/{id}")
    public ResponseEntity<Void> execute(@PathVariable("id") Integer queryId){
        log.debug("execute: " + queryId);
        return new ResponseEntity<>(queryService.execute(queryId).getHttpStatus());
    }

    @GetMapping("/get-table-query-by-id/{id}")
    public ResponseEntity<QueryDTO> get(@PathVariable("id") Integer queryId){
        log.debug("get: " + queryId);
        return ResponseEntity.ok(queryService.get(queryId));
    }

    @GetMapping("/get-all-queries-by-table-name/{name}")
    public ResponseEntity<List<QueryDTO>> getByTableName(@PathVariable("name") String tableName){
        log.debug("getByTableName: " + tableName);
        return ResponseEntity.ok(queryService.getByTableName(tableName));
    }

    @GetMapping("/get-all-table-queries")
    public ResponseEntity<List<QueryDTO>> getAll(){
        log.debug("getAll:");
        return ResponseEntity.ok(queryService.getAll());
    }

}
