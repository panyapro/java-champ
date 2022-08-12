package cup.online.javachamp.repository;

import cup.online.javachamp.entity.QueryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface QueryRepository extends CrudRepository<QueryEntity, Integer> {

    Optional<QueryEntity> findById(Integer id);

    @Transactional
    void deleteById(Integer id);
}