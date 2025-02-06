package com.sebn.brettbau.domain.customtable.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "custom_table_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomTableData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private CustomTable table;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private CustomColumn column;

    private Integer rowIndex;
    private String cellValue;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}
