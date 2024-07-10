package com.management.authentication.repository;


import com.management.authentication.model.ERole;
import com.management.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(ERole name);
}
