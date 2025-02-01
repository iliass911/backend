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

    // For downtime records, this field is required.
    // For equipment records, leave this null.
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    // Common fields for downtime tracking
    private String mois;
    private String semaine;
    private String equipment;           // Equipment ID
    private String equipmentName;       // Equipment Name
    private String site;
    private String projet;
    private String shift;
    
    // Downtime fields (for downtime tracking records)
    @Column(name = "duree_arret")
    private Integer dureeArret;
    private String accepte;
    @Column(name = "nbr_operateurs")
    private Integer nbrOperateurs;
    private String descriptionDefaillance;
    private String descriptionActionCorrective;
    private String intervenant;
    
    // Equipment-specific fields (for equipment records)
    @Column(name = "type_equipement")
    private String typeEquipement;
    private String natureIntervention;
    @Column(name = "lien_scan_fiche_bde")
    private String lienScanFicheBDE;
    @Column(name = "installation_date")
    private LocalDate installationDate;
    @Column(name = "equipment_status")
    private String equipmentStatus;
    
    // KPI calculation fields (optional)
    private Double mtbf;
    private Double mttr;
    
    // New field to distinguish record types: "downtime" or "equipment"
    @Column(name = "record_type")
    private String recordType = "downtime";  // default to downtime

    // Constructors
    public U6() {
    }

    // Full constructor (you can overload as needed)
    public U6(LocalDate date, String mois, String semaine, String equipment, String equipmentName, String site,
              String projet, String shift, Integer dureeArret, String accepte, Integer nbrOperateurs,
              String descriptionDefaillance, String descriptionActionCorrective, String intervenant,
              String typeEquipement, String natureIntervention, String lienScanFicheBDE, LocalDate installationDate,
              String equipmentStatus, Double mtbf, Double mttr, String recordType) {
        this.date = date;
        this.mois = mois;
        this.semaine = semaine;
        this.equipment = equipment;
        this.equipmentName = equipmentName;
        this.site = site;
        this.projet = projet;
        this.shift = shift;
        this.dureeArret = dureeArret;
        this.accepte = accepte;
        this.nbrOperateurs = nbrOperateurs;
        this.descriptionDefaillance = descriptionDefaillance;
        this.descriptionActionCorrective = descriptionActionCorrective;
        this.intervenant = intervenant;
        this.typeEquipement = typeEquipement;
        this.natureIntervention = natureIntervention;
        this.lienScanFicheBDE = lienScanFicheBDE;
        this.installationDate = installationDate;
        this.equipmentStatus = equipmentStatus;
        this.mtbf = mtbf;
        this.mttr = mttr;
        this.recordType = recordType;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getMois() { return mois; }
    public void setMois(String mois) { this.mois = mois; }

    public String getSemaine() { return semaine; }
    public void setSemaine(String semaine) { this.semaine = semaine; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getProjet() { return projet; }
    public void setProjet(String projet) { this.projet = projet; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public Integer getDureeArret() { return dureeArret; }
    public void setDureeArret(Integer dureeArret) { this.dureeArret = dureeArret; }

    public String getAccepte() { return accepte; }
    public void setAccepte(String accepte) { this.accepte = accepte; }

    public Integer getNbrOperateurs() { return nbrOperateurs; }
    public void setNbrOperateurs(Integer nbrOperateurs) { this.nbrOperateurs = nbrOperateurs; }

    public String getDescriptionDefaillance() { return descriptionDefaillance; }
    public void setDescriptionDefaillance(String descriptionDefaillance) { this.descriptionDefaillance = descriptionDefaillance; }

    public String getDescriptionActionCorrective() { return descriptionActionCorrective; }
    public void setDescriptionActionCorrective(String descriptionActionCorrective) { this.descriptionActionCorrective = descriptionActionCorrective; }

    public String getIntervenant() { return intervenant; }
    public void setIntervenant(String intervenant) { this.intervenant = intervenant; }

    public String getTypeEquipement() { return typeEquipement; }
    public void setTypeEquipement(String typeEquipement) { this.typeEquipement = typeEquipement; }

    public String getNatureIntervention() { return natureIntervention; }
    public void setNatureIntervention(String natureIntervention) { this.natureIntervention = natureIntervention; }

    public String getLienScanFicheBDE() { return lienScanFicheBDE; }
    public void setLienScanFicheBDE(String lienScanFicheBDE) { this.lienScanFicheBDE = lienScanFicheBDE; }

    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }

    public String getEquipmentStatus() { return equipmentStatus; }
    public void setEquipmentStatus(String equipmentStatus) { this.equipmentStatus = equipmentStatus; }

    public Double getMtbf() { return mtbf; }
    public void setMtbf(Double mtbf) { this.mtbf = mtbf; }

    public Double getMttr() { return mttr; }
    public void setMttr(Double mttr) { this.mttr = mttr; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    // equals and hashCode now include recordType so equipment records won’t be merged with downtime records.
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
        return Objects.hash(date, mois, semaine, equipment, equipmentName, site, projet, shift, dureeArret, accepte,
                            nbrOperateurs, descriptionDefaillance, descriptionActionCorrective, intervenant, typeEquipement,
                            natureIntervention, lienScanFicheBDE, installationDate, equipmentStatus, recordType);
    }
}
