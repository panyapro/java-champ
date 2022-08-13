package cup.online.javachamp.controller;

import cup.online.javachamp.dto.QueryDTO;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table-query")
public class QueryManagerController {


    @Autowired
    private QueryService queryService;

    @PostMapping("/add-new-query-to-table")
    public ResponseEntity<Void> createQuery(@RequestBody QueryDTO query){
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
        return new ResponseEntity<>(
                queryService.modify(
                                QueryEntity.builder().query(query.getQuery())
                                        .tableName(query.getTableName())
                                        .queryId(query.getQueryId())
                                        .build())
                        .getHttpStatus()
        );
    }

    @PutMapping("/modify-query-in-table")
    public ResponseEntity<Void> delete(@RequestBody QueryDTO query){
        return null;
    }

}
