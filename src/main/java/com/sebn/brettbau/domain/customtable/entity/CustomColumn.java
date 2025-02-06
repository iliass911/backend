package com.sebn.brettbau.domain.customtable.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "custom_table_columns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private CustomTable table;

    private String name;
    private String type;
    private Integer orderIndex;
    private Boolean required;
    private String defaultValue;

    @Column(name = "numeric_precision")
    private Integer precision;
    
    @Column(name = "numeric_scale")
    private Integer scale;

    private Integer maxLength;
    private String dateFormat;
}