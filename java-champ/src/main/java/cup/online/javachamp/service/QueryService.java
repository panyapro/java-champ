package cup.online.javachamp.service;


import cup.online.javachamp.constant.ResultStatus;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.repository.QueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;

    public ResultStatus create(QueryEntity queryEntity) {
        try {
            // TODO add validate for existing table by table_name
            queryRepository.save(queryEntity);
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred for {} ", queryEntity, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ResultStatus modify(QueryEntity queryEntity) {
        try {
            // TODO add validate for existing table by table_name

            queryRepository.findByQueryId(queryEntity.getQueryId())
                    .ifPresentOrElse(
                            entity -> {
                                entity.setQuery(queryEntity.getQuery());
                                queryRepository.save(entity);
                            },
                            () -> {
                                throw new IllegalArgumentException("Entity not found");
                            }
                    );
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred for {} ", queryEntity, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }
}
