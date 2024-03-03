package ru.otus.spacebuttle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spacebuttle.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
}