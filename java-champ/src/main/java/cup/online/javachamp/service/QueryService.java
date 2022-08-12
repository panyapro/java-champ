package cup.online.javachamp.service;


import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class QueryService implements DBService<QueryEntity> {

    @Autowired
    private QueryRepository queryRepository;

    @Override
    public HttpStatus create(QueryEntity queryEntity) {
        queryRepository.save(queryEntity);
        return null;
    }
}
