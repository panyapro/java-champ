package cup.online.javachamp.dto;

import lombok.*;



@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QueryDTO {

    private Integer queryId;
    private String query;
    private String tableName;
}
