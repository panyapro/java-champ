package cup.online.javachamp.controller;

import cup.online.javachamp.dto.QueryDTO;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.entity.SingleQueryEntity;
import cup.online.javachamp.service.SingleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/single-query")
public class SingleQueryManagerController {

    @Autowired
    private SingleQueryService singleQueryService;

    @PostMapping("/add-new-query")
    public ResponseEntity<Void> createQuery(@RequestBody QueryDTO query) {
        log.debug("create single query: " + query);
        return new ResponseEntity<>(
                singleQueryService.create(
                                SingleQueryEntity.builder().query(query.getQuery())
                                        .queryId(query.getQueryId())
                                        .build())
                        .getHttpStatus()
        );
    }

    @PutMapping("/modify-single-query")
    public ResponseEntity<Void> modify(@RequestBody QueryDTO query) {
        log.debug("modify single query: " + query);
        return new ResponseEntity<>(
                singleQueryService.modify(
                                SingleQueryEntity.builder()
                                        .query(query.getQuery())
                                        .queryId(query.getQueryId())
                                        .build())
                        .getHttpStatus()
        );
    }

    @DeleteMapping("/delete-single-query-by-id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("delete single query: " + id);
        return new ResponseEntity<>(
                singleQueryService.delete(id)
                        .getHttpStatus()
        );
    }

    @GetMapping("/execute-single-query-by-id/{id}")
    public ResponseEntity<Void> execute(@PathVariable Integer id) {
        log.debug("delete single query: " + id);
        return new ResponseEntity<>(
                singleQueryService.execute(id)
                        .getHttpStatus()
        );
    }

    @GetMapping("/get-single-query-by-id/{id}")
    public QueryDTO get(@PathVariable Integer id) {
        log.debug("get single query: " + id);
        return singleQueryService.get(id);
    }


    @GetMapping("/get-all-single-queries")
    public List<QueryDTO> getAll() {
        log.debug("get all single queries ");
        return singleQueryService.getAll();
    }
}
