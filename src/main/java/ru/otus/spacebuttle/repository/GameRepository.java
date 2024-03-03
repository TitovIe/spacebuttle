package ru.otus.spacebuttle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spacebuttle.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}