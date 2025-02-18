package com.sebn.brettbau.domain.maintenance.service;

import com.sebn.brettbau.domain.audit.service.AuditLogService;
import com.sebn.brettbau.domain.maintenance.dto.MaintenanceInterventionDTO;
import com.sebn.brettbau.domain.maintenance.entity.MaintenanceIntervention;
import com.sebn.brettbau.domain.maintenance.mapper.MaintenanceInterventionMapper;
import com.sebn.brettbau.domain.maintenance.repository.MaintenanceInterventionRepository;
import com.sebn.brettbau.domain.user.entity.User;
import com.sebn.brettbau.domain.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceInterventionService {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceInterventionService.class);

    private final MaintenanceInterventionRepository repository;
    private final AuditLogService auditLogService;
    private final UserService userService;

    public MaintenanceInterventionService(MaintenanceInterventionRepository repository,
                                          AuditLogService auditLogService,
                                          UserService userService) {
        this.repository = repository;
        this.auditLogService = auditLogService;
        this.userService = userService;
    }

    public MaintenanceInterventionDTO createIntervention(MaintenanceInterventionDTO dto) {
        try {
            MaintenanceIntervention entity = MaintenanceInterventionMapper.toEntity(dto);
            MaintenanceIntervention saved = repository.save(entity);

            User currentUser = userService.getCurrentUser();
            auditLogService.logEvent(
                    currentUser,
                    "MAINTENANCE_INTERVENTION_CREATED",
                    String.format("Maintenance intervention created: Type: %s, Value: %s",
                            saved.getTypeIntervention(), saved.getValeurEuro()),
                    "MAINTENANCE",
                    saved.getId()
            );

            return MaintenanceInterventionMapper.toDTO(saved);
        } catch (Exception e) {
            logger.error("Error creating maintenance intervention", e);
            throw new RuntimeException("Failed to create maintenance intervention: " + e.getMessage());
        }
    }

    public List<MaintenanceInterventionDTO> getAllInterventions() {
        try {
            return repository.findAll().stream()
                    .map(MaintenanceInterventionMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all maintenance interventions", e);
            throw new RuntimeException("Failed to retrieve maintenance interventions: " + e.getMessage());
        }
    }

    public MaintenanceInterventionDTO getInterventionById(Long id) {
        try {
            return repository.findById(id)
                    .map(MaintenanceInterventionMapper::toDTO)
                    .orElse(null);
        } catch (Exception e) {
            logger.error("Error retrieving maintenance intervention with id " + id, e);
            throw new RuntimeException("Failed to retrieve maintenance intervention: " + e.getMessage());
        }
    }

    public MaintenanceInterventionDTO updateIntervention(Long id, MaintenanceInterventionDTO dto) {
        try {
            MaintenanceIntervention entity = repository.findById(id).orElse(null);
            if (entity == null) {
                logger.warn("Maintenance intervention not found with id: {}", id);
                return null; // Alternatively, throw a NotFoundException
            }

            String oldDetails = String.format("Type: %s, Value: %s", 
                    entity.getTypeIntervention(), entity.getValeurEuro());

            // Update fields
            entity.setTypeIntervention(dto.getTypeIntervention());
            entity.setValeurEuro(dto.getValeurEuro());
            entity.setPointExaminer(dto.getPointExaminer());
            entity.setNatureIntervention(dto.getNatureIntervention());
            entity.setPosteTouche(dto.getPosteTouche());
            entity.setZone(dto.getZone());
            entity.setShift(dto.getShift());
            entity.setNumeroPanneau(dto.getNumeroPanneau());
            entity.setNomPrenomBB(dto.getNomPrenomBB());
            entity.setTempsIntervention(dto.getTempsIntervention());
            entity.setDate(dto.getDate());
            entity.setCommentaire(dto.getCommentaire());
            entity.setSite(dto.getSite());

            MaintenanceIntervention updated = repository.save(entity);

            User currentUser = userService.getCurrentUser();
            auditLogService.logEvent(
                    currentUser,
                    "MAINTENANCE_INTERVENTION_UPDATED",
                    String.format("Maintenance intervention updated from [%s] to [Type: %s, Value: %s]",
                            oldDetails, updated.getTypeIntervention(), updated.getValeurEuro()),
                    "MAINTENANCE",
                    updated.getId()
            );

            return MaintenanceInterventionMapper.toDTO(updated);
        } catch (Exception e) {
            logger.error("Error updating maintenance intervention with id " + id, e);
            throw new RuntimeException("Failed to update maintenance intervention: " + e.getMessage());
        }
    }

    public void deleteIntervention(Long id) {
        try {
            MaintenanceIntervention entity = repository.findById(id).orElse(null);
            if (entity == null) {
                logger.warn("Maintenance intervention not found with id: {}", id);
                return; // Alternatively, throw a NotFoundException
            }
            repository.deleteById(id);

            User currentUser = userService.getCurrentUser();
            auditLogService.logEvent(
                    currentUser,
                    "MAINTENANCE_INTERVENTION_DELETED",
                    String.format("Maintenance intervention deleted: Type: %s, Value: %s",
                            entity.getTypeIntervention(), entity.getValeurEuro()),
                    "MAINTENANCE",
                    id
            );
        } catch (Exception e) {
            logger.error("Error deleting maintenance intervention with id " + id, e);
            throw new RuntimeException("Failed to delete maintenance intervention: " + e.getMessage());
        }
    }
}
