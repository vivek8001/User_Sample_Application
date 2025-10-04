package com.userSample.Repository;


import com.userSample.entity.User;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
