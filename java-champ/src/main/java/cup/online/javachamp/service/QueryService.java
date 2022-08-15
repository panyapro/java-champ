package cup.online.javachamp.service;


import cup.online.javachamp.constant.ResultStatus;
import cup.online.javachamp.dto.QueryDTO;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.exception.ObjectNotFoundException;
import cup.online.javachamp.repository.QueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private DynamicTableService dynamicTableService;

    public ResultStatus create(QueryEntity queryEntity) {
        try {
            checkTableExist(queryEntity.getTableName());

            if (queryRepository.findByQueryId(queryEntity.getQueryId())
                    .isPresent()) {
                throw new IllegalArgumentException("Entity with " + queryEntity.getQueryId() + " already exist");
            }
            queryRepository.save(queryEntity);
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred for {} ", queryEntity, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ResultStatus modify(QueryEntity queryEntity) {
        try {
            checkTableExist(queryEntity.getTableName());

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

    public List<QueryDTO> getByTableName(String tableName){
        List<QueryEntity> entityList =
                queryRepository.findByTableName(tableName);

        return entityList.stream()
                .map(entity ->
                        QueryDTO.builder()
                                .queryId(entity.getQueryId())
                                .tableName(entity.getTableName())
                                .query(entity.getQuery())
                                .build())
                .collect(Collectors.toUnmodifiableList());
    }

    public List<QueryDTO> getAll() {
        return StreamSupport.stream(
                        queryRepository.findAll().spliterator(), false)
                .map(queryEntity ->
                        QueryDTO.builder()
                                .query(queryEntity.getQuery())
                                .queryId(queryEntity.getQueryId())
                                .build()
                )
                .collect(Collectors.toUnmodifiableList());

    }

    private void checkTableExist(String tableName) throws SQLException {
        if (!dynamicTableService.isTableExist(tableName)) {
            throw new IllegalArgumentException("Table with " + tableName + " doesn't exist");
        }
    }
}
