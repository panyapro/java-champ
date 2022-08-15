package cup.online.javachamp.repository;

import cup.online.javachamp.entity.QueryEntity;
import cup.online.javachamp.entity.SingleQueryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SingleQueryRepository extends CrudRepository<SingleQueryEntity, Long> {

    Optional<SingleQueryEntity> findById(Long id);

    Optional<SingleQueryEntity> findByQueryId(Integer id);
}