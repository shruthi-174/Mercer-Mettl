package com.mercer.mettl.user.org.service.repository;

import com.mercer.mettl.user.org.service.constants.Status;
import com.mercer.mettl.user.org.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByOrgIdAndStatus(Integer orgId, Status status);
}
