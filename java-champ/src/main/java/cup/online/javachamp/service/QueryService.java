package cup.online.javachamp.service;


import cup.online.javachamp.constant.ResultStatus;
import cup.online.javachamp.controller.CustomerExceptionHandler;
import cup.online.javachamp.dto.QueryDTO;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.exception.ObjectNotFoundException;
import cup.online.javachamp.repository.QueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private DynamicTableService dynamicTableService;

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

    public ResultStatus remove(Integer queryId) {
        try {
            // TODO add validate for existing table by table_name
            queryRepository.findByQueryId(queryId)
                    .ifPresentOrElse(
                            entity -> queryRepository.delete(entity),
                            () -> {
                                throw new IllegalArgumentException("Entity not found");
                            }
                    );

            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying remove query for {} ", queryId, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ResultStatus execute(Integer queryId) {
        try {
            // TODO add validate for existing table by table_name
            Optional<QueryEntity> queryEntityOptional =
                    queryRepository.findByQueryId(queryId);
            if (queryEntityOptional.isPresent()) {
                QueryEntity entity = queryEntityOptional.get();
                dynamicTableService.execute(entity.getQuery());
                return ResultStatus.ACCEPTED;
            } else {
                log.debug("queryId not found {} ", queryId);
                return ResultStatus.NOT_ACCEPTABLE;
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying remove query for {} ", queryId, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public QueryDTO get(Integer queryId) {
        try {
            // TODO add validate for existing table by table_name
            Optional<QueryEntity> queryEntityOptional =
                    queryRepository.findByQueryId(queryId);
            if (queryEntityOptional.isPresent()) {
                QueryEntity queryEntity = queryEntityOptional.get();
                return QueryDTO.builder()
                        .query(queryEntity.getQuery())
                        .queryId(queryEntity.getQueryId())
                        .build();
            } else {
                throw new ObjectNotFoundException();
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying remove query for {} ", queryId, e);
            throw new ObjectNotFoundException();
        }
    }
}
