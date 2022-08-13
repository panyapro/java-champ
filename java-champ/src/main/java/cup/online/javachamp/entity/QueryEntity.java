package cup.online.javachamp.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Getter
@Setter
@Entity
@Table(name = "query")
@NoArgsConstructor
@AllArgsConstructor
public class QueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_id")
    private Integer queryId;

    @Column(name = "query")
    private String query;

    @Column(name = "table_name")
    private String tableName;

}
