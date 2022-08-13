package cup.online.javachamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {

    private String tableName;
    private Integer columnsAmount;
    private String primaryKey;
    private List<ColumnInfo> columnInfos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnInfo {
        private String title;
        private String type;
    }
}
