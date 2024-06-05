package com.example.librarymanagement.repository;

import com.example.librarymanagement.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
