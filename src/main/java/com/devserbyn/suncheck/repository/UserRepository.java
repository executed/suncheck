package com.devserbyn.suncheck.repository;


import com.devserbyn.suncheck.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
