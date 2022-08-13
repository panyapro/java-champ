package cup.online.javachamp.converter;

import cup.online.javachamp.dto.TableDTO;
import org.springframework.stereotype.Component;

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

    private static final String DROP_TABLE_BY_TABLE_NAME = "DROP TABLE ";

    public String findByNameSql(String tableName) {
        if (isNull(tableName)) {
            throw new IllegalArgumentException("tableName is empty");
        }
        return GET_TABLE_INFO_BY_TABLE_NAME + "'" + tableName + "'";
    }

    public String dropByNameSql(String tableName) {
        if (isNull(tableName)) {
            throw new IllegalArgumentException("tableName is empty");
        }
        return DROP_TABLE_BY_TABLE_NAME + tableName;
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
