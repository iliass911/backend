package com.sebn.brettbau.domain.kpis.entity;

import javax.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Objects;

@Entity
@Table(name = "equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Equipment ID from file (e.g. "EQ-001")
    @Column(name = "equipment_id")
    private String equipmentId;

    @Column(name = "equipment_name")
    private String equipmentName;

    private String type;
    private String projet;
    private String site;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "installation_date")
    private LocalDate installationDate;

    private String status;

    public Equipment() {
    }

    public Equipment(String equipmentId, String equipmentName, String type, String projet, String site,
                     LocalDate installationDate, String status) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.type = type;
        this.projet = projet;
        this.site = site;
        this.installationDate = installationDate;
        this.status = status;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEquipmentId() { return equipmentId; }
    public void setEquipmentId(String equipmentId) { this.equipmentId = equipmentId; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getProjet() { return projet; }
    public void setProjet(String projet) { this.projet = projet; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipment)) return false;
        Equipment that = (Equipment) o;
        return Objects.equals(equipmentId, that.equipmentId) &&
               Objects.equals(equipmentName, that.equipmentName) &&
               Objects.equals(type, that.type) &&
               Objects.equals(projet, that.projet) &&
               Objects.equals(site, that.site) &&
               Objects.equals(installationDate, that.installationDate) &&
               Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentId, equipmentName, type, projet, site, installationDate, status);
    }
}
