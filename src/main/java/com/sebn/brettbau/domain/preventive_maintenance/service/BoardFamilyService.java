package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.BoardFamilyDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Board;
import com.sebn.brettbau.domain.preventive_maintenance.entity.BoardFamily;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.BoardFamilyMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardFamilyRepository;
import com.sebn.brettbau.domain.preventive_maintenance.repository.BoardRepository;
import com.sebn.brettbau.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardFamilyService {
    private final BoardFamilyRepository familyRepository;
    private final BoardRepository boardRepository;
    private final BoardFamilyMapper familyMapper;

    public BoardFamilyService(
            BoardFamilyRepository familyRepository,
            BoardRepository boardRepository,
            BoardFamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.boardRepository = boardRepository;
        this.familyMapper = familyMapper;
    }

    public List<BoardFamilyDTO> getAllFamilies() {
        return familyRepository.findAll()
            .stream()
            .map(familyMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public BoardFamily getOrCreateFamily(Board board) {
        if (!hasValidFamilyAttributes(board)) {
            List<String> missingAttributes = getMissingAttributes(board);
            throw new IllegalArgumentException("Board is missing required family attributes: " + 
                String.join(", ", missingAttributes));
        }

        return familyRepository
            .findByProjetAndSideAndFbType2AndFbType3AndFbSizeAndDerivate(
                board.getProjet(),
                board.getSide(),
                board.getFbType2(),
                board.getFbType3(),
                board.getFbSize(),
                board.getDerivate()
            )
            .orElseGet(() -> createNewFamily(createFamilyKey(board), 1));
    }

    public BoardFamily findExistingFamily(
            String projet, String side, String fbType2, 
            String fbType3, String fbSize, String derivate) {
        return familyRepository.findByProjetAndSideAndFbType2AndFbType3AndFbSizeAndDerivate(
                projet, side, fbType2, fbType3, fbSize, derivate)
                .orElseThrow(() -> new ResourceNotFoundException("No matching family found"));
    }

    public BoardFamily createFamily(
            String projet, String side, String fbType2, 
            String fbType3, String fbSize, String derivate) {
        
        FamilyKey key = new FamilyKey(projet, side, fbType2, fbType3, fbSize, derivate);
        return createNewFamily(key, 0);
    }

    @Transactional
    public int generateFamiliesForExistingBoards() {
        System.out.println("Starting family generation process...");
        
        // Get all boards
        List<Board> allBoards = boardRepository.findAll();
        System.out.println("Found " + allBoards.size() + " total boards");
        
        // Group boards by common attributes
        Map<String, List<Board>> boardGroups = new HashMap<>();
        
        for (Board board : allBoards) {
            if (board.getProjet() != null && board.getSide() != null && 
                board.getFbType2() != null && board.getFbType3() != null && 
                board.getFbSize() != null && board.getDerivate() != null) {
                
                String groupKey = String.format("%s_%s_%s_%s_%s_%s",
                    board.getProjet(),
                    board.getSide(),
                    board.getFbType2(),
                    board.getFbType3(),
                    board.getFbSize(),
                    board.getDerivate());
                
                boardGroups.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(board);
            }
        }
        
        System.out.println("Created " + boardGroups.size() + " potential family groups");

        int familiesCreated = 0;
        
        // Create families for each group
        for (Map.Entry<String, List<Board>> entry : boardGroups.entrySet()) {
            List<Board> boardGroup = entry.getValue();
            if (boardGroup.isEmpty()) continue;
            
            // Use first board as template for family
            Board templateBoard = boardGroup.get(0);
            
            // Check if family already exists
            Optional<BoardFamily> existingFamily = familyRepository
                .findByProjetAndSideAndFbType2AndFbType3AndFbSizeAndDerivate(
                    templateBoard.getProjet(),
                    templateBoard.getSide(),
                    templateBoard.getFbType2(),
                    templateBoard.getFbType3(),
                    templateBoard.getFbSize(),
                    templateBoard.getDerivate()
                );

            BoardFamily family;
            if (existingFamily.isPresent()) {
                family = existingFamily.get();
                System.out.println("Found existing family: " + family.getFamilyName());
            } else {
                // Create new family
                family = BoardFamily.builder()
                    .familyName("FAM_" + entry.getKey())
                    .projet(templateBoard.getProjet())
                    .side(templateBoard.getSide())
                    .fbType2(templateBoard.getFbType2())
                    .fbType3(templateBoard.getFbType3())
                    .fbSize(templateBoard.getFbSize())
                    .derivate(templateBoard.getDerivate())
                    .boardCount(0)
                    .build();
                
                family = familyRepository.save(family);
                familiesCreated++;
                System.out.println("Created new family: " + family.getFamilyName());
            }

            // Associate all boards with this family
            for (Board board : boardGroup) {
                board.setFamily(family);
                boardRepository.save(board);
            }
            
            // Update board count
            family.setBoardCount(boardGroup.size());
            familyRepository.save(family);
            
            System.out.println("Added " + boardGroup.size() + " boards to family " + family.getFamilyName());
        }
        
        System.out.println("Family generation complete. Created " + familiesCreated + " new families.");
        return familiesCreated;
    }

    private List<String> getMissingAttributes(Board board) {
        List<String> missing = new ArrayList<>();
        if (board.getProjet() == null || board.getProjet().isEmpty()) missing.add("projet");
        if (board.getSide() == null || board.getSide().isEmpty()) missing.add("side");
        if (board.getFbType2() == null || board.getFbType2().isEmpty()) missing.add("fbType2");
        if (board.getFbType3() == null || board.getFbType3().isEmpty()) missing.add("fbType3");
        if (board.getFbSize() == null || board.getFbSize().isEmpty()) missing.add("fbSize");
        if (board.getDerivate() == null || board.getDerivate().isEmpty()) missing.add("derivate");
        return missing;
    }

    private boolean hasValidFamilyAttributes(Board board) {
        return getMissingAttributes(board).isEmpty();
    }

    private FamilyKey createFamilyKey(Board board) {
        return new FamilyKey(
                board.getProjet(),
                board.getSide(),
                board.getFbType2(),
                board.getFbType3(),
                board.getFbSize(),
                board.getDerivate()
        );
    }

    private BoardFamily createNewFamily(FamilyKey key, int boardCount) {
        BoardFamily family = BoardFamily.builder()
                .familyName(generateFamilyName(key))
                .projet(key.projet)
                .side(key.side)
                .fbType2(key.fbType2)
                .fbType3(key.fbType3)
                .fbSize(key.fbSize)
                .derivate(key.derivate)
                .boardCount(boardCount)
                .build();
        return familyRepository.save(family);
    }

    private String generateFamilyName(Board board) {
        return String.format("FAM_%s_%s_%s_%s_%s_%s",
                board.getProjet(),
                board.getSide(),
                board.getFbType2(),
                board.getFbType3(),
                board.getFbSize(),
                board.getDerivate()
        );
    }

    private String generateFamilyName(FamilyKey key) {
        return String.format("FAM_%s_%s_%s_%s_%s_%s",
                key.projet,
                key.side,
                key.fbType2,
                key.fbType3,
                key.fbSize,
                key.derivate
        );
    }

    private static class FamilyKey {
        final String projet;
        final String side;
        final String fbType2;
        final String fbType3;
        final String fbSize;
        final String derivate;

        FamilyKey(String projet, String side, String fbType2, String fbType3, String fbSize, String derivate) {
            this.projet = projet;
            this.side = side;
            this.fbType2 = fbType2;
            this.fbType3 = fbType3;
            this.fbSize = fbSize;
            this.derivate = derivate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FamilyKey that = (FamilyKey) o;
            return Objects.equals(projet, that.projet) &&
                   Objects.equals(side, that.side) &&
                   Objects.equals(fbType2, that.fbType2) &&
                   Objects.equals(fbType3, that.fbType3) &&
                   Objects.equals(fbSize, that.fbSize) &&
                   Objects.equals(derivate, that.derivate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(projet, side, fbType2, fbType3, fbSize, derivate);
        }
    }
}
