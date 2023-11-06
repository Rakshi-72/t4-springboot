package com.rakshi.t4.repos;

import com.rakshi.t4.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
