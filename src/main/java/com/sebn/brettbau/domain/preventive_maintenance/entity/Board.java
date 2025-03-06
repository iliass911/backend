package com.sebn.brettbau.domain.preventive_maintenance.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;
import com.sebn.brettbau.domain.user.entity.User;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_number", nullable = false)
    @NotBlank(message = "Board Number is required")
    private String boardNumber;

    @Column(name = "fb_id")
    private String fbId;

    @Column(name = "fb_name")
    @NotBlank(message = "FB Name is required")
    private String fbName;

    @Column(name = "fb_size")
    @NotBlank(message = "FB Size is required")
    private String fbSize;

    @Column(name = "first_tech_level")
    private String firstTechLevel;

    @Column(name = "projet")
    private String projet;

    @Column(name = "plant")
    private String plant;

    @Column(name = "in_use")
    private String inUse;

    @Column(name = "test_clip")
    private Boolean testClip;

    @Column(name = "area")
    private String area;

    @Column(name = "fb_type1")
    private String fbType1;

    @Column(name = "fb_type2")
    private String fbType2;

    @Column(name = "fb_type3")
    private String fbType3;

    @Column(name = "side")
    private String side;

    @Column(name = "derivate")
    private String derivate;

    @Column(name = "creation_reason")
    private String creationReason;

    @Column(name = "first_yellow_release_date")
    private LocalDate firstYellowReleaseDate;

    @Column(name = "first_orange_release_date")
    private LocalDate firstOrangeReleaseDate;

    @Column(name = "first_green_release_date")
    private LocalDate firstGreenReleaseDate;

    @Column(name = "first_use_by_prod_date")
    private LocalDate firstUseByProdDate;

    @Column(name = "current_tech_level")
    private String currentTechLevel;

    @Column(name = "nextTechLevel")
    private String nextTechLevel;

    @Column(name = "last_tech_change_implemented")
    private String lastTechChangeImplemented;

    @Column(name = "last_tech_change_imple_date")
    private LocalDate lastTechChangeImpleDate;

    @Column(name = "last_tech_change_release_date")
    private LocalDate lastTechChangeReleaseDate;

    @Column(name = "comment_1")
    private String comment1;

    @Column(name = "comment_2")
    private String comment2;

    @Column(name = "comment_3")
    private String comment3;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "storage_place")
    private String storagePlace;

    @Column(name = "status")
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Checklist> checklists;
}
