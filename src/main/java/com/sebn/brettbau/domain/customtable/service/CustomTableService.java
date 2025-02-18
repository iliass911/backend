package com.sebn.brettbau.domain.customtable.service;

import com.sebn.brettbau.domain.customtable.dto.CustomColumnDTO;
import com.sebn.brettbau.domain.customtable.dto.CustomTableDTO;
import com.sebn.brettbau.domain.customtable.dto.TableUpdateMessage;
import com.sebn.brettbau.domain.customtable.entity.CustomColumn;
import com.sebn.brettbau.domain.customtable.entity.CustomTable;
import com.sebn.brettbau.domain.customtable.entity.CustomTableData;
import com.sebn.brettbau.domain.customtable.entity.CustomTableSession;
import com.sebn.brettbau.domain.customtable.repository.CustomColumnRepository;
import com.sebn.brettbau.domain.customtable.repository.CustomTableDataRepository;
import com.sebn.brettbau.domain.customtable.repository.CustomTableRepository;
import com.sebn.brettbau.domain.customtable.repository.CustomTableSessionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomTableService {

    private static final Logger logger = LoggerFactory.getLogger(CustomTableService.class);

    private final CustomTableRepository tableRepository;
    private final CustomColumnRepository columnRepository;
    private final CustomTableDataRepository dataRepository;
    private final CustomTableSessionRepository sessionRepository;

    /**
     * Creates a new CustomTable along with any provided columns and initial data.
     */
    @Transactional
    public CustomTableDTO createTable(CustomTableDTO dto, String userId) {
        try {
            // Validate input
            if (dto.getName() == null || dto.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Table name cannot be empty");
            }

            CustomTable table = CustomTable.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .createdBy(userId)
                    .createdAt(LocalDateTime.now())
                    .lastModifiedBy(userId)
                    .lastModifiedAt(LocalDateTime.now())
                    .build();

            CustomTable savedTable = tableRepository.save(table);

            // Save columns and keep a list of the saved entities
            List<CustomColumn> savedColumns = new ArrayList<>();
            if (dto.getColumns() != null) {
                for (int i = 0; i < dto.getColumns().size(); i++) {
                    CustomColumnDTO colDto = dto.getColumns().get(i);
                    CustomColumn column = CustomColumn.builder()
                            .table(savedTable)
                            .name(colDto.getName())
                            .type(colDto.getType())
                            // Use the loop index as the order index if not provided
                            .orderIndex(colDto.getOrderIndex() != null ? colDto.getOrderIndex() : i)
                            .required(colDto.getRequired() != null ? colDto.getRequired() : false)
                            .defaultValue(colDto.getDefaultValue())
                            .precision(colDto.getPrecision())
                            .scale(colDto.getScale())
                            .maxLength(colDto.getMaxLength())
                            .dateFormat(colDto.getDateFormat())
                            .build();
                    savedColumns.add(columnRepository.save(column));
                }
            }

            // Initialize table data if provided
            if (dto.getData() != null && !dto.getData().isEmpty()) {
                for (int rowIndex = 0; rowIndex < dto.getData().size(); rowIndex++) {
                    List<Object> row = dto.getData().get(rowIndex);
                    for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                        // Ensure that a corresponding column exists
                        if (colIndex >= savedColumns.size()) {
                            throw new IllegalArgumentException("Data column index " + colIndex +
                                    " exceeds the number of table columns " + savedColumns.size());
                        }
                        CustomTableData cellData = CustomTableData.builder()
                                .table(savedTable)
                                .column(savedColumns.get(colIndex))
                                .rowIndex(rowIndex)
                                .cellValue(String.valueOf(row.get(colIndex)))
                                .lastModifiedAt(LocalDateTime.now())
                                .lastModifiedBy(userId)
                                .build();
                        dataRepository.save(cellData);
                    }
                }
            }

            return mapToDTO(savedTable);
        } catch (Exception e) {
            logger.error("Error creating table for user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error creating table: " + e.getMessage(), e);
        }
    }

    /**
     * Get all tables in the system.
     *
     * Note: Although the method signature accepts a userId, it now returns all tables.
     */
    public List<CustomTableDTO> getTablesByUser(String userId) {
        return tableRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific table by ID.
     */
    public CustomTableDTO getTableById(Long tableId) {
        CustomTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));
        return mapToDTO(table);
    }

    /**
     * Update an existing table.
     */
    @Transactional
    public CustomTableDTO updateTable(Long tableId, CustomTableDTO tableDTO) {
        try {
            logger.info("Updating table: {}", tableId);
            logger.info("Table Name: {}", tableDTO.getName());
            logger.info("Columns received: {}", (tableDTO.getColumns() != null ? tableDTO.getColumns().size() : "None"));
            logger.info("Data received: {}", (tableDTO.getData() != null ? tableDTO.getData().size() : "None"));

            // Fetch the table
            CustomTable table = tableRepository.findById(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

            // Update basic table fields
            table.setName(tableDTO.getName());
            table.setDescription(tableDTO.getDescription());
            table.setLastModifiedAt(LocalDateTime.now());
            if (tableDTO.getLastModifiedBy() != null) {
                table.setLastModifiedBy(tableDTO.getLastModifiedBy());
            }

            CustomTable updatedTable = tableRepository.save(table);

            // Update columns if provided: delete all existing columns and add new ones.
            if (tableDTO.getColumns() != null) {
                List<CustomColumn> existingColumns = columnRepository.findByTableIdOrderByOrderIndexAsc(tableId);
                columnRepository.deleteAll(existingColumns);

                for (CustomColumnDTO colDto : tableDTO.getColumns()) {
                    CustomColumn column = CustomColumn.builder()
                            .table(updatedTable)
                            .name(colDto.getName())
                            .type(colDto.getType())
                            .orderIndex(colDto.getOrderIndex())
                            .required(colDto.getRequired())
                            .defaultValue(colDto.getDefaultValue())
                            .precision(colDto.getPrecision())
                            .scale(colDto.getScale())
                            .maxLength(colDto.getMaxLength())
                            .dateFormat(colDto.getDateFormat())
                            .build();
                    columnRepository.save(column);
                }
            }

            // Update cell data if provided in the DTO.
            if (tableDTO.getData() != null) {
                // Clear existing data for this table.
                dataRepository.deleteByTableId(tableId);

                // Retrieve the updated columns (assumed to be in the correct order).
                List<CustomColumn> columns = columnRepository.findByTableIdOrderByOrderIndexAsc(tableId);

                // Save new cell data.
                for (int rowIndex = 0; rowIndex < tableDTO.getData().size(); rowIndex++) {
                    List<Object> rowData = tableDTO.getData().get(rowIndex);
                    for (int colIndex = 0; colIndex < rowData.size(); colIndex++) {
                        if (colIndex >= columns.size()) {
                            throw new IllegalArgumentException("Data column index " + colIndex +
                                    " exceeds the number of table columns " + columns.size());
                        }
                        CustomColumn column = columns.get(colIndex);
                        CustomTableData cellData = CustomTableData.builder()
                                .table(updatedTable)
                                .column(column)
                                .rowIndex(rowIndex)
                                .cellValue(String.valueOf(rowData.get(colIndex)))
                                .lastModifiedAt(LocalDateTime.now())
                                .lastModifiedBy(tableDTO.getLastModifiedBy())
                                .build();
                        dataRepository.save(cellData);
                    }
                }
            }

            return mapToDTO(updatedTable);
        } catch (Exception e) {
            logger.error("Error updating table {}: {}", tableId, e.getMessage(), e);
            throw new RuntimeException("Error updating table: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a table and all its associated data.
     */
    @Transactional
    public void deleteTable(Long tableId) {
        sessionRepository.deleteAll(
                sessionRepository.findByTableIdAndIsActiveTrue(tableId)
        );

        dataRepository.deleteAll(
                dataRepository.findByTableIdOrdered(tableId)
        );

        columnRepository.deleteAll(
                columnRepository.findByTableIdOrderByOrderIndexAsc(tableId)
        );

        tableRepository.deleteById(tableId);
    }

    /**
     * Add a new column to an existing table.
     */
    @Transactional
    public CustomColumnDTO addColumn(Long tableId, CustomColumnDTO columnDTO) {
        CustomTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

        List<CustomColumn> existingColumns = columnRepository.findByTableIdOrderByOrderIndexAsc(tableId);
        int nextOrderIndex = existingColumns.isEmpty() ? 0 :
                existingColumns.get(existingColumns.size() - 1).getOrderIndex() + 1;

        CustomColumn column = CustomColumn.builder()
                .table(table)
                .name(columnDTO.getName())
                .type(columnDTO.getType())
                .orderIndex(nextOrderIndex)
                .required(columnDTO.getRequired())
                .defaultValue(columnDTO.getDefaultValue())
                .precision(columnDTO.getPrecision())
                .scale(columnDTO.getScale())
                .maxLength(columnDTO.getMaxLength())
                .dateFormat(columnDTO.getDateFormat())
                .build();

        CustomColumn savedColumn = columnRepository.save(column);
        return mapColumnToDTO(savedColumn);
    }

    /**
     * Insert a new row at the specified index.
     */
    @Transactional
    public void insertRow(Long tableId, TableUpdateMessage update) {
        List<CustomTableData> existingData = dataRepository.findByTableIdOrdered(tableId);

        existingData.stream()
                .filter(data -> data.getRowIndex() >= update.getRowIndex())
                .forEach(data -> {
                    data.setRowIndex(data.getRowIndex() + 1);
                    dataRepository.save(data);
                });
    }

    /**
     * Delete a row at the specified index.
     */
    @Transactional
    public void deleteRow(Long tableId, TableUpdateMessage update) {
        dataRepository.deleteByTableIdAndRowIndex(tableId, update.getRowIndex());

        List<CustomTableData> higherRows = dataRepository.findByTableIdOrdered(tableId).stream()
                .filter(data -> data.getRowIndex() > update.getRowIndex())
                .collect(Collectors.toList());

        higherRows.forEach(data -> {
            data.setRowIndex(data.getRowIndex() - 1);
            dataRepository.save(data);
        });
    }

    /**
     * Update a cell value.
     */
    @Transactional
    public void updateCell(Long tableId, TableUpdateMessage update) {
        Optional<CustomTableData> optionalData = dataRepository.findByTableIdAndColumnIdAndRowIndex(
                tableId,
                update.getColumnId(),
                update.getRowIndex()
        );

        CustomTableData data = optionalData.orElseGet(() -> {
            CustomTableData newData = new CustomTableData();
            newData.setTable(tableRepository.findById(tableId).orElseThrow(
                    () -> new IllegalArgumentException("Table not found: " + tableId)
            ));
            newData.setColumn(columnRepository.findById(update.getColumnId()).orElseThrow(
                    () -> new IllegalArgumentException("Column not found: " + update.getColumnId())
            ));
            newData.setRowIndex(update.getRowIndex());
            return newData;
        });

        data.setCellValue(update.getNewValue());
        data.setLastModifiedAt(LocalDateTime.now());
        data.setLastModifiedBy(update.getUpdatedBy());

        dataRepository.save(data);
    }

    /**
     * Start a new session for a user joining a table.
     */
    @Transactional
    public void joinSession(Long tableId, String userId) {
        CustomTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));

        CustomTableSession session = CustomTableSession.builder()
                .table(table)
                .userId(userId)
                .joinedAt(LocalDateTime.now())
                .lastActiveAt(LocalDateTime.now())
                .isActive(true)
                .build();

        sessionRepository.save(session);
    }
    
    /**
     * Update column properties.
     */
    @Transactional
    public void updateColumn(Long tableId, TableUpdateMessage update) {
        // Implement any necessary column update logic here.
    }

    /**
     * End a session for a user leaving a table.
     */
    @Transactional
    public void leaveSession(Long tableId, String userId) {
        sessionRepository.deleteByUserIdAndTableId(userId, tableId);
    }

    /**
     * Get a list of active users for a table.
     */
    public List<String> getActiveUsers(Long tableId) {
        return sessionRepository.findByTableIdAndIsActiveTrue(tableId)
                .stream()
                .map(CustomTableSession::getUserId)
                .collect(Collectors.toList());
    }

    // Helper method to map a CustomTable entity to its DTO.
    private CustomTableDTO mapToDTO(CustomTable table) {
        CustomTableDTO dto = new CustomTableDTO();
        dto.setId(table.getId());
        dto.setName(table.getName());
        dto.setDescription(table.getDescription());
        dto.setCreatedBy(table.getCreatedBy());
        dto.setCreatedAt(table.getCreatedAt());
        dto.setLastModifiedAt(table.getLastModifiedAt());
        dto.setLastModifiedBy(table.getLastModifiedBy());

        // Fetch columns for the table and map them to DTOs.
        List<CustomColumn> columns = columnRepository.findByTableIdOrderByOrderIndexAsc(table.getId());
        dto.setColumns(
            columns.stream().map(this::mapColumnToDTO).collect(Collectors.toList())
        );

        // Fetch table data (cell values) for the table.
        List<CustomTableData> tableData = dataRepository.findAllByTableId(table.getId());

        // Organize the data into a 2D list.
        if (!tableData.isEmpty()) {
            // Determine the maximum row index.
            int maxRowIndex = tableData.stream()
                .mapToInt(CustomTableData::getRowIndex)
                .max()
                .orElse(-1);

            // Create a 2D list with an entry for each row.
            List<List<Object>> data = new ArrayList<>();
            for (int i = 0; i <= maxRowIndex; i++) {
                data.add(new ArrayList<>(columns.size()));
            }

            // Populate the 2D list with cell values.
            for (CustomTableData cellData : tableData) {
                int rowIndex = cellData.getRowIndex();
                // Find the column's index in the fetched columns list.
                int colIndex = columns.indexOf(cellData.getColumn());
                
                // Ensure the row has enough cells.
                while (data.get(rowIndex).size() < columns.size()) {
                    data.get(rowIndex).add(null);
                }
                
                // Set the cell value at the correct row and column index.
                data.get(rowIndex).set(colIndex, cellData.getCellValue());
            }

            dto.setData(data);
        } else {
            dto.setData(new ArrayList<>());
        }

        return dto;
    }

    // Helper method to map a CustomColumn entity to its DTO.
    private CustomColumnDTO mapColumnToDTO(CustomColumn column) {
        CustomColumnDTO dto = new CustomColumnDTO();
        dto.setId(column.getId());
        dto.setName(column.getName());
        dto.setType(column.getType());
        dto.setOrderIndex(column.getOrderIndex());
        dto.setRequired(column.getRequired());
        dto.setDefaultValue(column.getDefaultValue());
        dto.setPrecision(column.getPrecision());
        dto.setScale(column.getScale());
        dto.setMaxLength(column.getMaxLength());
        dto.setDateFormat(column.getDateFormat());
        return dto;
    }
}
