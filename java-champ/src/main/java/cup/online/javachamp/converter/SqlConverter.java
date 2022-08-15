package cup.online.javachamp.converter;

import cup.online.javachamp.dto.TableDTO;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.isNull;

@Component
public class SqlConverter {


    //language=Sql
    private static final String GET_TABLE_INFO_BY_TABLE_NAME = "select COL.TABLE_NAME, COL.COLUMN_NAME as col_name, COL.DATA_TYPE, C.COLUMN_NAME as primary_col FROM " +
                                                                "INFORMATION_SCHEMA.TABLE_CONSTRAINTS T " +
                                                                "JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE C " +
                                                                "ON C.CONSTRAINT_NAME=T.CONSTRAINT_NAME " +
                                                                "JOIN INFORMATION_SCHEMA.COLUMNS COL " +
                                                                "ON COL.TABLE_NAME = T.TABLE_NAME " +
                                                                "WHERE T.CONSTRAINT_TYPE='PRIMARY KEY' " +
                                                                "and C.TABLE_NAME =  ";
    //language=Sql
    private static final String DROP_TABLE_BY_TABLE_NAME = "DROP TABLE ";
    //language=Sql
    private static final String CHECK_TABLE_EXIST = "" +
            "SELECT * " +
            " FROM INFORMATION_SCHEMA.TABLES " +
            " where TABLE_NAME = ";
    //language=Sql
    private static final String CHECK_COLUMN_EXIST_INSIDE_TABLE = "" +
            "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '%s' and COLUMN_NAME = '%s'";


    public String tableExistSql(String tableName) {
        if (isNull(tableName)) {
            throw new IllegalArgumentException("tableName is empty");
        }
        return CHECK_TABLE_EXIST + "'" + tableName + "'";
    }

    public String columnExistInsideTableSql(String tableName, String columnName) {
        if (isNull(tableName)) {
            throw new IllegalArgumentException("tableName is empty");
        }
        if (isNull(columnName)) {
            throw new IllegalArgumentException("columnName is empty");
        }
        return String.format(CHECK_COLUMN_EXIST_INSIDE_TABLE, tableName.toUpperCase(), columnName.toUpperCase());
    }

    public String findByNameSql(String tableName) {
        if (isNull(tableName)) {
            throw new IllegalArgumentException("tableName is empty");
        }
        return GET_TABLE_INFO_BY_TABLE_NAME + "'" + tableName.toUpperCase() + "'";
    }

    public String dropByNameSql(String tableName) {
        if (isNull(tableName)) {
            throw new IllegalArgumentException("tableName is empty");
        }
        return DROP_TABLE_BY_TABLE_NAME + tableName.toUpperCase();
    }

    public String countForEachColumn(String tableName, List<String> columnNames) {
        String sql = " SELECT ";
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i).toUpperCase();
            sql += " count(" + columnName + ") as " + columnName;

            if (i != columnNames.size() - 1) {
                sql += ",";
            }
        }
        sql += " FROM " + tableName.toUpperCase();
        return sql;
    }

    public String tableDTOtoSql(TableDTO tableDTO) {
        // TODO probably rework method
        if (isNull(tableDTO.getTableName())) {
            throw new IllegalArgumentException("tableName is empty");
        }

        // TODO probably not required
        if (isNull(tableDTO.getPrimaryKey())) {
            throw new IllegalArgumentException("primary key is empty");
        }

        if (tableDTO.getColumnInfos().stream()
                .noneMatch(col ->
                        col.getTitle().equals(tableDTO.getPrimaryKey()))) {
            throw new IllegalArgumentException("primary key doesn't contain in column info");
        }

        if (tableDTO.getColumnsAmount() != tableDTO.getColumnInfos().size()) {
            throw new IllegalArgumentException("columns amount doesn't equal with column info");
        }


        String sql = "create table " + tableDTO.getTableName() + "(";
        for (int i = 0; i < tableDTO.getColumnsAmount(); i++) {
            TableDTO.ColumnInfo columns = tableDTO.getColumnInfos().get(i);
            sql += columns.getTitle() + " " + columns.getType();
            if (tableDTO.getPrimaryKey().equals(columns.getTitle())) {
                sql += " primary key ";
            }
            if (i != tableDTO.getColumnInfos().size() - 1) {
                sql += ",";
            }
        }
        sql += ")";
        return sql;
    }
}
