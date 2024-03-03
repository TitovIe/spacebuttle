package ru.otus.spacebuttle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spacebuttle.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}