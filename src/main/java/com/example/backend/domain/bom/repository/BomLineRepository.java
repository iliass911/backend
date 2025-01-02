// BomLineRepository.java
package com.example.backend.domain.bom.repository;

import com.example.backend.domain.bom.entity.BomLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomLineRepository extends JpaRepository<BomLine, Long> {
}