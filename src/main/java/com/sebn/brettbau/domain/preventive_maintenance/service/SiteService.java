package com.sebn.brettbau.domain.preventive_maintenance.service;

import com.sebn.brettbau.domain.preventive_maintenance.dto.SiteDTO;
import com.sebn.brettbau.domain.preventive_maintenance.entity.Site;
import com.sebn.brettbau.domain.preventive_maintenance.mapper.SiteMapper;
import com.sebn.brettbau.domain.preventive_maintenance.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiteService {
    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    public List<SiteDTO> getAllSites() {
        return siteRepository.findAll().stream()
                .map(SiteMapper::toDTO)
                .collect(Collectors.toList());
    }	

    public SiteDTO getSiteById(Long id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found"));
        return SiteMapper.toDTO(site);
    }

    public SiteDTO createSite(SiteDTO dto) {
        Site site = SiteMapper.toEntity(dto);
        Site saved = siteRepository.save(site);
        return SiteMapper.toDTO(saved);
    }

    public SiteDTO updateSite(Long id, SiteDTO dto) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found"));
        site.setName(dto.getName());
        site.setLocation(dto.getLocation());
        Site updated = siteRepository.save(site);
        return SiteMapper.toDTO(updated);
    }

    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}

