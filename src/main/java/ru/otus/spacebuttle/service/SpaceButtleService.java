package ru.otus.spacebuttle.service;

import ru.otus.spacebuttle.dto.SpaceButtleRequest;

public interface SpaceButtleService {
    Object makeAction(SpaceButtleRequest spaceButtleRequest);
}
