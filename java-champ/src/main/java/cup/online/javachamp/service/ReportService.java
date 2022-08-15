package cup.online.javachamp.service;

import cup.online.javachamp.constant.ResultStatus;
import cup.online.javachamp.dto.ReportDTO;
import cup.online.javachamp.dto.TableDTO;
import cup.online.javachamp.entity.ReportEntity;
import cup.online.javachamp.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private DynamicTableService dynamicTableService;

    public ResultStatus create(ReportDTO reportDTO) {

        try {
            if (reportDTO.getTableAmount() != reportDTO.getTables().size()) {
                throw new IllegalArgumentException("Amount of tables doesn't equals with table size");
            }

            if (reportRepository.findByReportId(reportDTO.getReportId()).isPresent()) {
                throw new IllegalArgumentException("Report with " + reportDTO.getReportId() + " already exist");
            }

            for (ReportDTO.TableInfo tableInfo : reportDTO.getTables()) {
                if (!dynamicTableService.isTableExist(tableInfo.getTableName())) {
                    throw new IllegalArgumentException("Table with name " + tableInfo.getTableName() + " doesn't exist");
                }

                for (ReportDTO.ColumnInfo columnInfo : tableInfo.getColumns()) {
                    if (!dynamicTableService.isColumnExist(tableInfo.getTableName(), columnInfo.getTitle())) {
                        throw new IllegalArgumentException("Column with name " + columnInfo.getTitle() + " doesn't exist inside " + tableInfo.getTableName());
                    }
                }
            }

            reportRepository.save(ReportEntity.builder()
                    .reportId(reportDTO.getReportId())
                    .tableNames(reportDTO.getTables().stream().map(ReportDTO.TableInfo::getTableName).collect(Collectors.joining(",")))
                    .build());
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying create report for {} ", reportDTO.getReportId(), e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ReportDTO getById(Integer id) {
        try {
            Optional<ReportEntity> reportEntityOpt =
                    reportRepository.findByReportId(id);

            if (reportEntityOpt.isEmpty()){
                throw new IllegalArgumentException("Report with id " + id + " doesn't exist");
            }
            ReportEntity reportEntity = reportEntityOpt.get();

            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setReportId(reportEntity.getReportId());

            String[] tableNames = reportEntity.getTableNames().split(",");
            reportDTO.setTableAmount(tableNames.length);

            List<ReportDTO.TableInfo> tableInfos = new ArrayList<>();
            for (String tableName : tableNames) {
                ReportDTO.TableInfo tableInfo = new ReportDTO.TableInfo();
                tableInfo.setTableName(tableName);

                TableDTO tableDTO = dynamicTableService.getStructureByName(tableName);
                Map<String, Integer> countByColumnName =
                        dynamicTableService.countByColumnName(
                                tableName,
                                tableDTO.getColumnInfos()
                                        .stream()
                                        .map(TableDTO.ColumnInfo::getTitle)
                                        .collect(Collectors.toUnmodifiableList()));

                List<ReportDTO.ColumnInfo> listReportColumnInfos = new ArrayList<>();
                for (TableDTO.ColumnInfo tableColumnInfo : tableDTO.getColumnInfos()) {
                    ReportDTO.ColumnInfo reportColumnInfo = new ReportDTO.ColumnInfo();
                    reportColumnInfo.setTitle(tableColumnInfo.getTitle());
                    reportColumnInfo.setType(tableColumnInfo.getType());
                    reportColumnInfo.setSize(countByColumnName.get(tableColumnInfo.getTitle()));
                    listReportColumnInfos.add(reportColumnInfo);
                }
                tableInfo.setColumns(listReportColumnInfos);
                tableInfos.add(tableInfo);
            }

            reportDTO.setTables(tableInfos);
            return reportDTO;
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying obtain report by {} ", id, e);
            return null;
        }
    }
}
