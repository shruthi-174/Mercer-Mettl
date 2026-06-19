package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}
