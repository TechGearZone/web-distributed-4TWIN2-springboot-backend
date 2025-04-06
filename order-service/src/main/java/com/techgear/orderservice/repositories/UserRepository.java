package com.techgear.orderservice.repositories;

import com.techgear.orderservice.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
