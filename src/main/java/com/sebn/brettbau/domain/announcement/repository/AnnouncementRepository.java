// src/main/java/com/sebn/brettbau/domain/announcement/repository/AnnouncementRepository.java
package com.sebn.brettbau.domain.announcement.repository;

import com.sebn.brettbau.domain.announcement.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}