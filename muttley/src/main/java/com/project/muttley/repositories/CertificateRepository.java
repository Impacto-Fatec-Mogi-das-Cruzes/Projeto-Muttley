package com.project.muttley.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.certificate.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

}
