package cup.online.javachamp.service;

import cup.online.javachamp.constant.ResultStatus;
import cup.online.javachamp.dto.QueryDTO;
import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.entity.SingleQueryEntity;
import cup.online.javachamp.exception.ObjectAlreadyExistException;
import cup.online.javachamp.repository.QueryRepository;
import cup.online.javachamp.repository.SingleQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class SingleQueryService {

    @Autowired
    private SingleQueryRepository singleQueryRepository;

    @Autowired
    private DynamicTableService dynamicTableService;

    public ResultStatus create(SingleQueryEntity singleQueryEntity) {
        try {
            if (singleQueryRepository.findByQueryId(singleQueryEntity.getQueryId())
                    .isPresent()) {
                throw new IllegalArgumentException("Single query with " + singleQueryEntity.getQueryId() + " already exist");
            }
            singleQueryRepository.save(singleQueryEntity);
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred for {} ", singleQueryEntity, e);
            return ResultStatus.BAD_REQUEST;
        }
    }

    public ResultStatus modify(SingleQueryEntity singleQueryEntity) {
        try {
            Optional<SingleQueryEntity> singleQueryOpt =
                    singleQueryRepository.findByQueryId(singleQueryEntity.getQueryId());
            if (singleQueryOpt.isEmpty()) {
                throw new IllegalArgumentException("Single query with " + singleQueryEntity.getQueryId() + " doesn't exist");
            }
            SingleQueryEntity singleQuery = singleQueryOpt.get();
            singleQuery.setQuery(singleQuery.getQuery());
            singleQueryRepository.save(singleQuery);
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error while trying modify single query entity occurred for {} ", singleQueryEntity, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ResultStatus delete(Integer id) {
        try {
            Optional<SingleQueryEntity> singleQueryOpt =
                    singleQueryRepository.findByQueryId(id);
            if (singleQueryOpt.isEmpty()) {
                throw new IllegalArgumentException("Single query with " + id + " doesn't exist");
            }
            SingleQueryEntity singleQuery = singleQueryOpt.get();
            singleQuery.setQuery(singleQuery.getQuery());
            singleQueryRepository.save(singleQuery);
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error while trying delete single query entity occurred for {} ", id, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ResultStatus execute(Integer id) {
        try {
            Optional<SingleQueryEntity> singleQueryOpt =
                    singleQueryRepository.findByQueryId(id);
            if (singleQueryOpt.isEmpty()) {
                throw new IllegalArgumentException("Single query with " + id + " doesn't exist");
            }
            if (!dynamicTableService.execute(singleQueryOpt.get().getQuery())) {
                log.error("Unsuccessfully execute query {} ", singleQueryOpt.get().getQuery());
                return ResultStatus.NOT_ACCEPTABLE;
            }

            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error while trying execute single query entity occurred for {} ", id, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public QueryDTO get(Integer id) {
        Optional<SingleQueryEntity> singleQueryOpt =
                singleQueryRepository.findByQueryId(id);
        if (singleQueryOpt.isEmpty()) {
            throw new IllegalArgumentException("Single query with " + id + " doesn't exist");
        }
        SingleQueryEntity singleQuery = singleQueryOpt.get();

        return QueryDTO.builder()
                .query(singleQuery.getQuery())
                .queryId(singleQuery.getQueryId())
                .build();
    }

    public List<QueryDTO> getAll() {
        return StreamSupport.stream(singleQueryRepository.findAll().spliterator(), false)
                .map(singleQuery ->
                        QueryDTO.builder()
                                .query(singleQuery.getQuery())
                                .queryId(singleQuery.getQueryId())
                                .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
