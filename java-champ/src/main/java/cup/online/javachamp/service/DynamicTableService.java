package cup.online.javachamp.service;


import cup.online.javachamp.constant.ResultStatus;
import cup.online.javachamp.converter.SqlConverter;
import cup.online.javachamp.dto.TableDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class DynamicTableService {

    @Autowired
    private DataSource hikariDataSource;

    @Autowired
    private SqlConverter converter;

    public ResultStatus create(TableDTO tableDTO) {
        try {
            execute(converter.tableDTOtoSql(tableDTO));
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying create table for {} ", tableDTO.getTableName(), e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public ResultStatus remove(String tableName) {
        try {
            execute(converter.dropByNameSql(tableName));
            return ResultStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying remove table for {} ", tableName, e);
            return ResultStatus.NOT_ACCEPTABLE;
        }
    }

    public boolean execute(String sql) throws SQLException {
        try (Connection connection =
                     hikariDataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {
            return ps.execute();
        }
    }

    public boolean checkExistExecute(String sql) throws SQLException {
        try (Connection connection =
                     hikariDataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {
            return ps.executeQuery().isBeforeFirst();
        }
    }

    public TableDTO getStructureByName(String tableName) {

        try (Connection connection =
                     hikariDataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(converter.findByNameSql(tableName))) {

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                log.debug("No data is available for {} ", tableName);
                return null;
            } else {
                TableDTO tableDTO = new TableDTO();
                List<TableDTO.ColumnInfo> columnInfos = new ArrayList<>();
                tableDTO.setPrimaryKey(rs.getString("primary_col").toLowerCase());
                tableDTO.setTableName(rs.getString("TABLE_NAME"));
                do {
                    columnInfos.add(
                            TableDTO.ColumnInfo.builder()
                                    .title(rs.getString("col_name"))
                                    .type(rs.getString("DATA_TYPE"))
                                    .build());
                } while (rs.next());
                tableDTO.setColumnInfos(columnInfos);
                tableDTO.setColumnsAmount(columnInfos.size());
                return tableDTO;
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying get by table name {} ", tableName, e);
        }
        return null;
    }

    public Map<String, Integer> countByColumnName(String tableName, List<String> columnNames) {
        try (Connection connection =
                     hikariDataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(converter.countForEachColumn(tableName, columnNames))) {

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                log.debug("No data is available for {} ", tableName);
                return Collections.emptyMap();
            } else {
                Map<String, Integer> map = new HashMap<>();
                do {
                    for (String columnName : columnNames) {
                        if (rs.getString(columnName) != null) {
                            map.put(columnName, rs.getInt(columnName));
                        }
                    }
                } while (rs.next());
                return map;
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred while trying get by table name {} ", tableName, e);
        }
        return Collections.emptyMap();
    }

    public boolean isTableExist(String tableName) throws SQLException {
        return checkExistExecute(converter.tableExistSql(tableName.toUpperCase()));
    }

    public boolean isColumnExist(String tableName, String columnName) throws SQLException {
        return checkExistExecute(converter.columnExistInsideTableSql(tableName.toUpperCase(), columnName.toUpperCase()));
    }
}
