package com.sebn.brettbau.domain.kpis.entity;

import javax.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Objects;

@Entity
@Table(name = "u6")
public class U6 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String projet;
    private String code;

    @Column(name = "temps_darret")
    private String tempsDarret;

    @Column(name = "total_minute")
    private Integer totalMinute;

    private String commentaire;
    private String zone;
    private String workplace;

    @Column(name = "operator_count")
    private Integer operatorCount = 1;  // Default to 1

    // Constructors
    public U6() {}

    public U6(LocalDate date, String projet, String code, String tempsDarret, Integer totalMinute, 
              String commentaire, String zone, String workplace) {
        this.date = date;
        this.projet = projet;
        this.code = code;
        this.tempsDarret = tempsDarret;
        this.totalMinute = totalMinute;
        this.commentaire = commentaire;
        this.zone = zone;
        this.workplace = workplace;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getProjet() { return projet; }
    public void setProjet(String projet) { this.projet = projet; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTempsDarret() { return tempsDarret; }
    public void setTempsDarret(String tempsDarret) { this.tempsDarret = tempsDarret; }

    public Integer getTotalMinute() { return totalMinute; }
    public void setTotalMinute(Integer totalMinute) { this.totalMinute = totalMinute; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getWorkplace() { return workplace; }
    public void setWorkplace(String workplace) { this.workplace = workplace; }

    public Integer getOperatorCount() { return operatorCount; }
    public void setOperatorCount(Integer operatorCount) { this.operatorCount = operatorCount; }

    // Add equals method to check for duplicates
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        U6 u6 = (U6) o;
        return Objects.equals(date, u6.date) &&
               Objects.equals(projet, u6.projet) &&
               Objects.equals(code, u6.code) &&
               Objects.equals(tempsDarret, u6.tempsDarret) &&
               Objects.equals(totalMinute, u6.totalMinute) &&
               Objects.equals(commentaire, u6.commentaire) &&
               Objects.equals(zone, u6.zone) &&
               Objects.equals(workplace, u6.workplace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, projet, code, tempsDarret, totalMinute, commentaire, zone, workplace);
    }
}

