package com.devserbyn.suncheck.repository;


import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.model.UserConfig;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {

}
