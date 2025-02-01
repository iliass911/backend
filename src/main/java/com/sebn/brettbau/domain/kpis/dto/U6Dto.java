package com.sebn.brettbau.domain.kpis.dto;

import java.time.LocalDate;

public class U6Dto {
    private Long id;
    private LocalDate date;
    private String mois;
    private String semaine;
    private String equipment;
    private String equipmentName;
    private String site;
    private String projet;
    private String shift;
    private Integer dureeArret;
    private String accepte;
    private Integer nbrOperateurs;
    private String descriptionDefaillance;
    private String descriptionActionCorrective;
    private String intervenant;
    private String typeEquipement;
    private String natureIntervention;
    private String lienScanFicheBDE;
    private LocalDate installationDate;
    private String equipmentStatus;
    private Double mtbf;
    private Double mttr;
    private String recordType;

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
}
