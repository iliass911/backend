package com.sebn.brettbau.domain.kpis.service;

import com.sebn.brettbau.domain.kpis.entity.U6;
import com.sebn.brettbau.domain.kpis.repository.U6Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class U6Service {

    @Autowired
    private U6Repository u6Repository;

    public List<U6> findAll() {
        return u6Repository.findAll();
    }

    public Optional<U6> findById(Long id) {
        return u6Repository.findById(id);
    }

    public U6 save(U6 u6) {
        // Check for an existing identical entry (based on core fields)
        List<U6> existingEntries = u6Repository.findAll().stream()
                .filter(existing -> existing.equals(u6))
                .collect(Collectors.toList());
        if (!existingEntries.isEmpty()) {
            U6 existing = existingEntries.get(0);
            existing.setNbrOperateurs(existing.getNbrOperateurs() != null ? existing.getNbrOperateurs() + 1 : 1);
            return u6Repository.save(existing);
        }
        if(u6.getNbrOperateurs() == null) {
            u6.setNbrOperateurs(1);
        }
        return u6Repository.save(u6);
    }

    public List<U6> saveAll(List<U6> u6List) {
        Map<U6, Integer> groupedEntries = new HashMap<>();
        for (U6 entry : u6List) {
            groupedEntries.merge(entry, 1, Integer::sum);
        }
        List<U6> consolidatedList = new ArrayList<>();
        groupedEntries.forEach((entry, count) -> {
            entry.setNbrOperateurs(count);
            consolidatedList.add(entry);
        });
        return u6Repository.saveAll(consolidatedList);
    }

    public void deleteById(Long id) {
        u6Repository.deleteById(id);
    }
}
