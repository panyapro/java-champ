package cup.online.javachamp.repository;

import cup.online.javachamp.entity.ReportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {

    Optional<ReportEntity> findByReportId(Integer reportId);
}
