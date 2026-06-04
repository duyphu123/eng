package com.smartviet.base.salary.repositories;

import com.smartviet.base.salary.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @NonNull
    @Query(value = "SELECT * FROM users WHERE id = :id AND status <> 0", nativeQuery = true)
    Optional<User> findById(@NonNull @Param("id") UUID id);

//    @Query(value = "SELECT " +
//            "u.id, u.name, u.account, u.status, " +
//            "u.allow_modify, " +
//            "u.created_at, u.password, u.updated_at, u.created_by, u.updated_by " +
//            "FROM users u " +
//            "WHERE lower(u.account) = lower(:account) " +
//            "AND u.status <> 0", nativeQuery = true)
//    Optional<Object> findByAccount(@NotBlank @Param("account") String account);

    @Query(value = "SELECT * FROM users u " +
            "WHERE lower(u.account) = lower(:account) " +
            "AND u.status <> 0", nativeQuery = true)
    Optional<User> findByAccount(@NotBlank @Param("account") String account);

    @Query(value = "SELECT " +
            "u.id, u.name, u.account, u.status, " +
            "u.allow_modify, u.department_id, " +
            "d.name as d_name, d.code as d_code, " +
            "u.created_at, u.password, u.updated_at, u.created_by, u.updated_by " +
            "FROM users u " +
            "JOIN departments d ON u.department_id = d.id " +
            "WHERE d.id = :departmentId AND (:searchKey IS NULL OR :searchKey = '' OR (LOWER(u.name) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR LOWER(u.account) LIKE LOWER(CONCAT('%', :searchKey, '%')))) " +
            "AND u.status <> 0 AND d.status = 1", nativeQuery = true)
    Page<User> findByDepartmentId(
            @NotNull UUID departmentId,
            String searchKey,
            Pageable pageable);


}