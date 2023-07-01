package io.handicrafty.sixteen.user.repository;

import io.handicrafty.sixteen.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long>
