package com.mohitprasad.codeeditor.repository;

import com.mohitprasad.codeeditor.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Optional<Document> findByRoomId(UUID roomId);
}
