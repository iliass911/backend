// Create new file: src/main/java/com/example/backend/domain/preventive_maintenance/enums/BoardMaintenanceStatus.java
package com.example.backend.domain.preventive_maintenance.enums;

public enum BoardMaintenanceStatus {
    PENDING,    // Not yet due
    IN_PROGRESS,// Work started but not validated
    COMPLETED,  // Work done on time
    ADVANCED,   // Work done ahead of schedule
    RETARD      // Behind schedule
}