package cup.online.javachamp.controller;

import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/table-query")
public class QueryManagerController {


    @Autowired
    private DBService<QueryEntity> queryService;

    @PostMapping
    public ResponseEntity<Void> createQuery(QueryEntity query){
        queryService.create(query);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
