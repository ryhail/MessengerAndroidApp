package com.example.chatservice.repository;

import com.example.chatservice.model.Chatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatterRepository extends JpaRepository<Chatter, Long> {
}
