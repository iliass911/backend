package com.example.backend.domain.kpis.dto;

import java.time.LocalDate;

public class U6Dto {
    private Long id;
    private LocalDate date;
    private String projet;
    private String code;
    private String tempsDarret;
    private Integer totalMinute;
    private String commentaire;
    private String zone;
    private String workplace;

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
}
