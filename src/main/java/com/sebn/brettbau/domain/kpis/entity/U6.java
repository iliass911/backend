package com.sebn.brettbau.domain.kpis.entity;

import javax.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Objects;

@Entity
@Table(name = "u6")
public class U6 {

    // Primary key field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date field with a JSON format
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    // Fields without underscores (Hibernate maps these automatically)
    private String mois;
    private String semaine;
    private String equipment;

    // Field with underscore in database name; use @Column to map
    @Column(name = "equipment_name")
    private String equipmentName;

    private String site;
    private String projet;
    private String shift;

    // Field with underscore in database name
    @Column(name = "duree_arret")
    private Integer dureeArret;

    private String accepte;

    // Field with underscore in database name
    @Column(name = "nbr_operateurs")
    private Integer nbrOperateurs;

    // Field with underscore in database name
    @Column(name = "description_defaillance")
    private String descriptionDefaillance;

    // Field with underscore in database name
    @Column(name = "description_action_corrective")
    private String descriptionActionCorrective;

    private String intervenant;

    // Field with underscore in database name
    @Column(name = "type_equipement")
    private String typeEquipement;

    // Field with underscore in database name
    @Column(name = "nature_intervention")
    private String natureIntervention;

    // IMPORTANT: Updated field to match the exact column name in the database.
    @Column(name = "lien_scan_fichebde")
    private String lienScanFicheBDE;

    // Date field with a JSON format and mapped to an underscored database column name
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "installation_date")
    private LocalDate installationDate;

    // Field with underscore in database name
    @Column(name = "equipment_status")
    private String equipmentStatus;

    // Field with underscore in database name; default value is "downtime"
    @Column(name = "record_type")
    private String recordType = "downtime";

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getSemaine() {
        return semaine;
    }

    public void setSemaine(String semaine) {
        this.semaine = semaine;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getProjet() {
        return projet;
    }

    public void setProjet(String projet) {
        this.projet = projet;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Integer getDureeArret() {
        return dureeArret;
    }

    public void setDureeArret(Integer dureeArret) {
        this.dureeArret = dureeArret;
    }

    public String getAccepte() {
        return accepte;
    }

    public void setAccepte(String accepte) {
        this.accepte = accepte;
    }

    public Integer getNbrOperateurs() {
        return nbrOperateurs;
    }

    public void setNbrOperateurs(Integer nbrOperateurs) {
        this.nbrOperateurs = nbrOperateurs;
    }

    public String getDescriptionDefaillance() {
        return descriptionDefaillance;
    }

    public void setDescriptionDefaillance(String descriptionDefaillance) {
        this.descriptionDefaillance = descriptionDefaillance;
    }

    public String getDescriptionActionCorrective() {
        return descriptionActionCorrective;
    }

    public void setDescriptionActionCorrective(String descriptionActionCorrective) {
        this.descriptionActionCorrective = descriptionActionCorrective;
    }

    public String getIntervenant() {
        return intervenant;
    }

    public void setIntervenant(String intervenant) {
        this.intervenant = intervenant;
    }

    public String getTypeEquipement() {
        return typeEquipement;
    }

    public void setTypeEquipement(String typeEquipement) {
        this.typeEquipement = typeEquipement;
    }

    public String getNatureIntervention() {
        return natureIntervention;
    }

    public void setNatureIntervention(String natureIntervention) {
        this.natureIntervention = natureIntervention;
    }

    public String getLienScanFicheBDE() {
        return lienScanFicheBDE;
    }

    public void setLienScanFicheBDE(String lienScanFicheBDE) {
        this.lienScanFicheBDE = lienScanFicheBDE;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    // Ensure recordType is set to "downtime" if null before persisting
    @PrePersist
    public void prePersist() {
        if (this.recordType == null) {
            this.recordType = "downtime";
        }
    }

    // equals and hashCode including all fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof U6)) return false;
        U6 u6 = (U6) o;
        return Objects.equals(date, u6.date) &&
               Objects.equals(mois, u6.mois) &&
               Objects.equals(semaine, u6.semaine) &&
               Objects.equals(equipment, u6.equipment) &&
               Objects.equals(equipmentName, u6.equipmentName) &&
               Objects.equals(site, u6.site) &&
               Objects.equals(projet, u6.projet) &&
               Objects.equals(shift, u6.shift) &&
               Objects.equals(dureeArret, u6.dureeArret) &&
               Objects.equals(accepte, u6.accepte) &&
               Objects.equals(nbrOperateurs, u6.nbrOperateurs) &&
               Objects.equals(descriptionDefaillance, u6.descriptionDefaillance) &&
               Objects.equals(descriptionActionCorrective, u6.descriptionActionCorrective) &&
               Objects.equals(intervenant, u6.intervenant) &&
               Objects.equals(typeEquipement, u6.typeEquipement) &&
               Objects.equals(natureIntervention, u6.natureIntervention) &&
               Objects.equals(lienScanFicheBDE, u6.lienScanFicheBDE) &&
               Objects.equals(installationDate, u6.installationDate) &&
               Objects.equals(equipmentStatus, u6.equipmentStatus) &&
               Objects.equals(recordType, u6.recordType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, mois, semaine, equipment, equipmentName, site, projet, shift, 
                            dureeArret, accepte, nbrOperateurs, descriptionDefaillance, 
                            descriptionActionCorrective, intervenant, typeEquipement,
                            natureIntervention, lienScanFicheBDE, installationDate, 
                            equipmentStatus, recordType);
    }
}
