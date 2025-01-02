// src/main/java/com/example/backend/domain/preventive_maintenance/service/PackService.java

package com.example.backend.domain.preventive_maintenance.service;

import com.example.backend.domain.preventive_maintenance.dto.PackDTO;
import com.example.backend.domain.preventive_maintenance.entity.Pack;
import com.example.backend.domain.preventive_maintenance.entity.Site;
import com.example.backend.domain.preventive_maintenance.mapper.PackMapper;
import com.example.backend.domain.preventive_maintenance.repository.PackRepository;
import com.example.backend.domain.preventive_maintenance.repository.SiteRepository;
import com.example.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackService {
    private final PackRepository packRepository;
    private final SiteRepository siteRepository;
    private final PackMapper packMapper;

    public PackService(PackRepository packRepository, SiteRepository siteRepository, PackMapper packMapper) {
        this.packRepository = packRepository;
        this.siteRepository = siteRepository;
        this.packMapper = packMapper;
    }

    public List<PackDTO> getAllPacks() {
        return packRepository.findAll().stream()
                .map(packMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PackDTO getPackById(Long id) {
        Pack pack = packRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id " + id));
        return packMapper.toDTO(pack);
    }

    public PackDTO createPack(PackDTO dto) {
        Pack pack = packMapper.toEntity(dto);
        Pack saved = packRepository.save(pack);
        return packMapper.toDTO(saved);
    }

    public PackDTO updatePack(Long id, PackDTO dto) {
        Pack existingPack = packRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found with id " + id));
        existingPack.setName(dto.getName());

        if (dto.getSiteId() != null) {
            Site site = siteRepository.findById(dto.getSiteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Site not found with id " + dto.getSiteId()));
            existingPack.setSite(site);
        }

        Pack updatedPack = packRepository.save(existingPack);
        return packMapper.toDTO(updatedPack);
    }

    public void deletePack(Long id) {
        if (!packRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pack not found with id " + id);
        }
        packRepository.deleteById(id);
    }

    public List<PackDTO> getPacksBySiteId(Long siteId) {
        return packRepository.findBySiteId(siteId).stream()
                .map(packMapper::toDTO)
                .collect(Collectors.toList());
    }
}
