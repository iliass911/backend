package com.sebn.brettbau.domain.kpis.service;

import com.sebn.brettbau.domain.kpis.entity.Equipment;
import com.sebn.brettbau.domain.kpis.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public List<Equipment> findAll() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> findById(Long id) {
        return equipmentRepository.findById(id);
    }

    public Equipment save(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public void deleteById(Long id) {
        equipmentRepository.deleteById(id);
    }
}
