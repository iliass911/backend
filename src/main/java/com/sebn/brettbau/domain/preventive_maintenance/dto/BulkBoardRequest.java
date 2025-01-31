package com.sebn.brettbau.domain.preventive_maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for bulk board creation requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkBoardRequest {
    private List<BoardDTO> boards;
}
