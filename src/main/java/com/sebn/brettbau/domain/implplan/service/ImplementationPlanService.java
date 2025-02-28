package com.sebn.brettbau.domain.implplan.service;

import com.sebn.brettbau.domain.implplan.dto.ImplementationPlanDTO;
import com.sebn.brettbau.domain.implplan.entity.ImplementationPlan;
import com.sebn.brettbau.domain.implplan.repository.ImplementationPlanRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat; // if you plan to handle CSV in the future
import org.apache.poi.ss.usermodel.*;  // <-- imports for Apache POI: Cell, Row, Sheet, Workbook, DateUtil, CellType, etc.
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImplementationPlanService {

    private final ImplementationPlanRepository repository;

    // ------------------------------------------------------------------------
    // CRUD Methods
    // ------------------------------------------------------------------------
    public List<ImplementationPlanDTO> getAll() {
        List<ImplementationPlan> list = repository.findAll();
        List<ImplementationPlanDTO> dtos = new ArrayList<>();
        for (ImplementationPlan plan : list) {
            dtos.add(entityToDto(plan));
        }
        return dtos;
    }

    public ImplementationPlanDTO getById(Long id) {
        ImplementationPlan plan = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        return entityToDto(plan);
    }

    public ImplementationPlanDTO createOrUpdate(ImplementationPlanDTO dto) {
        ImplementationPlan entity = (dto.getId() != null)
                ? repository.findById(dto.getId()).orElse(new ImplementationPlan())
                : new ImplementationPlan();

        // map fields from DTO
        mapDtoToEntity(dto, entity);
        ImplementationPlan saved = repository.save(entity);
        return entityToDto(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Plan not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // ------------------------------------------------------------------------
    // Excel Import
    // ------------------------------------------------------------------------
    public List<ImplementationPlanDTO> importFromExcel(MultipartFile file) throws Exception {
        List<ImplementationPlanDTO> results = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // assume single sheet; change as needed
            boolean headerRow = true;
            for (Row row : sheet) {
                if (headerRow) {
                    headerRow = false; // skip the header row
                    continue;
                }
                // Parse row -> ImplementationPlanDTO
                ImplementationPlanDTO dto = parseRowToDto(row);
                // Save each row into DB
                ImplementationPlanDTO saved = createOrUpdate(dto);
                results.add(saved);
            }
        } catch (Exception e) {
            throw new Exception("Failed to parse Excel file: " + e.getMessage(), e);
        }
        return results;
    }

    private ImplementationPlanDTO parseRowToDto(Row row) {
        ImplementationPlanDTO dto = new ImplementationPlanDTO();

        //  0: Side
        dto.setSide(readString(row.getCell(0)));

        //  1: FB Type
        dto.setFbType(readString(row.getCell(1)));

        //  2: Phase
        dto.setPhase(readString(row.getCell(2)));

        //  3: Comment
        dto.setComment(readString(row.getCell(3)));

        //  4: FB Title / Name
        dto.setFbTitle(readString(row.getCell(4)));

        //  5: Statut Release (PQM)
        dto.setStatutReleasePqm(readString(row.getCell(5)));

        //  6: CAD Designer Name
        dto.setCadDesignerName(readString(row.getCell(6)));

        //  7: Visualisation Status
        dto.setVisualisationStatus(readString(row.getCell(7)));

        //  8: Visualisation Sent to PQM (Date)
        dto.setVisualisationSentToPqmDate(readDate(row.getCell(8)));

        //  9: PQM Release Visualisation
        dto.setPqmReleaseVisualisation(readString(row.getCell(9)));

        // 10: Implementation on FB
        dto.setImplementationOnFb(readString(row.getCell(10)));

        // 11: Implementation Start Date
        dto.setImplementationStartDate(readDate(row.getCell(11)));

        // 12: Implementation End Date
        dto.setImplementationEndDate(readDate(row.getCell(12)));

        // 13: Implementation Statut
        dto.setImplementationStatut(readString(row.getCell(13)));

        // 14: Delivered to PQM (Date)
        dto.setDeliveredToPqmDate(readDate(row.getCell(14)));

        // 15: Release Start Date
        dto.setReleaseStartDate(readDate(row.getCell(15)));

        // 16: Release End Date
        dto.setReleaseEndDate(readDate(row.getCell(16)));

        // 17: 1st Check PQM
        dto.setFirstCheckPqm(readString(row.getCell(17)));

        // 18: Reparation BB
        dto.setReparationBb(readString(row.getCell(18)));

        // 19: 2nd Check PQM
        dto.setSecondCheckPqm(readString(row.getCell(19)));

        // 20: Yellow Release Date
        dto.setYellowReleaseDate(readDate(row.getCell(20)));

        // 21: WH Sample Status
        dto.setWhSampleStatus(readString(row.getCell(21)));

        // 22: Orange Release Date
        dto.setOrangeReleaseDate(readDate(row.getCell(22)));

        // 23: Green Release Date
        dto.setGreenReleaseDate(readDate(row.getCell(23)));

        // 24: Câblage & Simulation TC
        dto.setCablageSimulationTc(readString(row.getCell(24)));

        return dto;
    }


    // ------------------------------------------------------------------------
    // Utility Readers
    // ------------------------------------------------------------------------
    private String readString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else {
            // fallback: convert numeric, boolean, or other cell types to string
            return cell.toString().trim();
        }
    }

    private LocalDate readDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        // If your Excel file stores dates differently (text or custom format),
        // you would parse it with your own logic here.
        return null;
    }

    // ------------------------------------------------------------------------
    // Mappers
    // ------------------------------------------------------------------------
    private ImplementationPlanDTO entityToDto(ImplementationPlan e) {
        ImplementationPlanDTO dto = new ImplementationPlanDTO();
        dto.setId(e.getId());
        dto.setSide(e.getSide());
        dto.setFbType(e.getFbType());
        dto.setPhase(e.getPhase());
        dto.setComment(e.getComment());
        dto.setFbTitle(e.getFbTitle());
        dto.setStatutReleasePqm(e.getStatutReleasePqm());
        dto.setCadDesignerName(e.getCadDesignerName());
        dto.setVisualisationStatus(e.getVisualisationStatus());
        dto.setVisualisationSentToPqmDate(e.getVisualisationSentToPqmDate());
        dto.setPqmReleaseVisualisation(e.getPqmReleaseVisualisation());
        dto.setImplementationOnFb(e.getImplementationOnFb());
        dto.setImplementationStartDate(e.getImplementationStartDate());
        dto.setImplementationEndDate(e.getImplementationEndDate());
        dto.setImplementationStatut(e.getImplementationStatut());
        dto.setDeliveredToPqmDate(e.getDeliveredToPqmDate());
        dto.setReleaseStartDate(e.getReleaseStartDate());
        dto.setReleaseEndDate(e.getReleaseEndDate());
        dto.setFirstCheckPqm(e.getFirstCheckPqm());
        dto.setReparationBb(e.getReparationBb());
        dto.setSecondCheckPqm(e.getSecondCheckPqm());
        dto.setYellowReleaseDate(e.getYellowReleaseDate());
        dto.setWhSampleStatus(e.getWhSampleStatus());
        dto.setOrangeReleaseDate(e.getOrangeReleaseDate());
        dto.setGreenReleaseDate(e.getGreenReleaseDate());
        dto.setCablageSimulationTc(e.getCablageSimulationTc());
        return dto;
    }

    private void mapDtoToEntity(ImplementationPlanDTO dto, ImplementationPlan e) {
        e.setSide(dto.getSide());
        e.setFbType(dto.getFbType());
        e.setPhase(dto.getPhase());
        e.setComment(dto.getComment());
        e.setFbTitle(dto.getFbTitle());
        e.setStatutReleasePqm(dto.getStatutReleasePqm());
        e.setCadDesignerName(dto.getCadDesignerName());
        e.setVisualisationStatus(dto.getVisualisationStatus());
        e.setVisualisationSentToPqmDate(dto.getVisualisationSentToPqmDate());
        e.setPqmReleaseVisualisation(dto.getPqmReleaseVisualisation());
        e.setImplementationOnFb(dto.getImplementationOnFb());
        e.setImplementationStartDate(dto.getImplementationStartDate());
        e.setImplementationEndDate(dto.getImplementationEndDate());
        e.setImplementationStatut(dto.getImplementationStatut());
        e.setDeliveredToPqmDate(dto.getDeliveredToPqmDate());
        e.setReleaseStartDate(dto.getReleaseStartDate());
        e.setReleaseEndDate(dto.getReleaseEndDate());
        e.setFirstCheckPqm(dto.getFirstCheckPqm());
        e.setReparationBb(dto.getReparationBb());
        e.setSecondCheckPqm(dto.getSecondCheckPqm());
        e.setYellowReleaseDate(dto.getYellowReleaseDate());
        e.setWhSampleStatus(dto.getWhSampleStatus());
        e.setOrangeReleaseDate(dto.getOrangeReleaseDate());
        e.setGreenReleaseDate(dto.getGreenReleaseDate());
        e.setCablageSimulationTc(dto.getCablageSimulationTc());
    }
}
