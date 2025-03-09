package com.internship.portal.repository;

import com.internship.portal.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("select u from User u where " +
            "(:roleName is null or lower(u.role.roleName) like concat('%', :roleName, '%'))")
    Page<User> findAllFilterByRole(@Param("roleName") String roleName, Pageable pageable);

}
