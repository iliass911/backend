package com.sebn.brettbau.domain.tool.service;

import com.sebn.brettbau.domain.tool.dto.*;
import com.sebn.brettbau.domain.tool.entity.*;
import com.sebn.brettbau.domain.tool.repository.*;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToolService {
    private final ToolRepository toolRepository;
    private final ToolRequestRepository requestRepository;
    private final ToolTransferRepository transferRepository;
    private final ToolMaintenanceRepository maintenanceRepository;

    // Tool related methods
    public List<ToolDTO> getAll() {
        return toolRepository.findAll()
                .stream()
                .map(this::mapToToolDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ToolDTO createTool(ToolDTO dto) {
        Tool tool = Tool.builder()
                .toolId(dto.getToolId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .type(dto.getType())
                .location(dto.getLocation())
                .condition(dto.getCondition())
                .status(dto.getStatus())
                .currentHolder(dto.getCurrentHolder())
                .lastMaintained(dto.getLastMaintained())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        tool = toolRepository.save(tool);
        return mapToToolDTO(tool);
    }

    // Added update method for updating a tool
    @Transactional
    public ToolDTO updateTool(Long id, ToolDTO dto) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found"));
        
        // Update the tool fields with new values
        tool.setToolId(dto.getToolId());
        tool.setName(dto.getName());
        tool.setBrand(dto.getBrand());
        tool.setType(dto.getType());
        tool.setLocation(dto.getLocation());
        tool.setCondition(dto.getCondition());
        tool.setStatus(dto.getStatus());
        tool.setCurrentHolder(dto.getCurrentHolder());
        tool.setLastMaintained(dto.getLastMaintained());
        tool.setUpdatedAt(LocalDateTime.now());
        
        // Save and return the updated tool
        tool = toolRepository.save(tool);
        return mapToToolDTO(tool);
    }

    @Transactional
    public void deleteTool(Long id) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found"));
        toolRepository.delete(tool);
    }

    // Request related methods
    public List<ToolRequestDTO> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                .map(this::mapToToolRequestDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ToolRequestDTO createToolRequest(ToolRequestDTO dto) {
        Tool tool = toolRepository.findById(dto.getToolId())
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found"));
        ToolRequest request = ToolRequest.builder()
                .tool(tool)
                .requestedBy(dto.getRequestedBy())
                .approvedBy(dto.getApprovedBy())
                .status(dto.getStatus() != null ? dto.getStatus() : "PENDING")
                .requestedAt(LocalDateTime.now())
                .expectedReturnDate(dto.getExpectedReturnDate())
                .purpose(dto.getPurpose())
                .notes(dto.getNotes())
                .build();
        request = requestRepository.save(request);
        return mapToToolRequestDTO(request);
    }

    @Transactional
    public void deleteRequest(Long id) {
        ToolRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool request not found"));
        requestRepository.delete(request);
    }

    // Transfer related methods
    public List<ToolTransferDTO> getAllTransfers() {
        return transferRepository.findAll()
                .stream()
                .map(this::mapToToolTransferDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ToolTransferDTO createToolTransfer(ToolTransferDTO dto) {
        Tool tool = toolRepository.findById(dto.getToolId())
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found"));
        ToolTransfer transfer = ToolTransfer.builder()
                .tool(tool)
                .fromUser(dto.getFromUser())
                .toUser(dto.getToUser())
                .conditionBefore(dto.getConditionBefore())
                .conditionAfter(dto.getConditionAfter())
                .transferredAt(LocalDateTime.now())
                .notes(dto.getNotes())
                .photoUrls(dto.getPhotoUrls())
                .status(dto.getStatus() != null ? dto.getStatus() : "PENDING")
                .build();
        transfer = transferRepository.save(transfer);
        return mapToToolTransferDTO(transfer);
    }

    @Transactional
    public void deleteTransfer(Long id) {
        ToolTransfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool transfer not found"));
        transferRepository.delete(transfer);
    }

    // Maintenance related methods
    public List<ToolMaintenanceDTO> getAllMaintenance() {
        return maintenanceRepository.findAll()
                .stream()
                .map(this::mapToToolMaintenanceDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ToolMaintenanceDTO createMaintenanceRequest(ToolMaintenanceDTO dto) {
        Tool tool = toolRepository.findById(dto.getToolId())
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found"));
        ToolMaintenance maintenance = ToolMaintenance.builder()
                .tool(tool)
                .type(dto.getType())
                .requestedBy(dto.getRequestedBy())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : "PENDING")
                .requestedAt(LocalDateTime.now())
                .notes(dto.getNotes())
                .cost(dto.getCost())
                .build();
        maintenance = maintenanceRepository.save(maintenance);
        return mapToToolMaintenanceDTO(maintenance);
    }

    @Transactional
    public void deleteMaintenance(Long id) {
        ToolMaintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool maintenance record not found"));
        maintenanceRepository.delete(maintenance);
    }

    // Mapping methods
    private ToolDTO mapToToolDTO(Tool tool) {
        if (tool == null) return null;
        ToolDTO dto = new ToolDTO();
        dto.setId(tool.getId());
        dto.setToolId(tool.getToolId());
        dto.setName(tool.getName());
        dto.setBrand(tool.getBrand());
        dto.setType(tool.getType());
        dto.setLocation(tool.getLocation());
        dto.setCondition(tool.getCondition());
        dto.setStatus(tool.getStatus());
        dto.setCurrentHolder(tool.getCurrentHolder());
        dto.setLastMaintained(tool.getLastMaintained());
        dto.setCreatedAt(tool.getCreatedAt());
        dto.setUpdatedAt(tool.getUpdatedAt());
        return dto;
    }

    private ToolRequestDTO mapToToolRequestDTO(ToolRequest request) {
        ToolRequestDTO dto = new ToolRequestDTO();
        dto.setId(request.getId());
        dto.setToolId(request.getTool().getId());
        dto.setRequestedBy(request.getRequestedBy());
        dto.setApprovedBy(request.getApprovedBy());
        dto.setStatus(request.getStatus());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setExpectedReturnDate(request.getExpectedReturnDate());
        dto.setPurpose(request.getPurpose());
        dto.setNotes(request.getNotes());
        return dto;
    }

    private ToolTransferDTO mapToToolTransferDTO(ToolTransfer transfer) {
        ToolTransferDTO dto = new ToolTransferDTO();
        dto.setId(transfer.getId());
        dto.setToolId(transfer.getTool().getId());
        dto.setFromUser(transfer.getFromUser());
        dto.setToUser(transfer.getToUser());
        dto.setConditionBefore(transfer.getConditionBefore());
        dto.setConditionAfter(transfer.getConditionAfter());
        dto.setTransferredAt(transfer.getTransferredAt());
        dto.setNotes(transfer.getNotes());
        dto.setPhotoUrls(transfer.getPhotoUrls());
        dto.setStatus(transfer.getStatus());
        return dto;
    }

    private ToolMaintenanceDTO mapToToolMaintenanceDTO(ToolMaintenance maintenance) {
        ToolMaintenanceDTO dto = new ToolMaintenanceDTO();
        dto.setId(maintenance.getId());
        dto.setToolId(maintenance.getTool().getId());
        dto.setType(maintenance.getType());
        dto.setRequestedBy(maintenance.getRequestedBy());
        dto.setDescription(maintenance.getDescription());
        dto.setStatus(maintenance.getStatus());
        dto.setRequestedAt(maintenance.getRequestedAt());
        dto.setCompletedAt(maintenance.getCompletedAt());
        dto.setNotes(maintenance.getNotes());
        dto.setCost(maintenance.getCost());
        return dto;
    }
}
