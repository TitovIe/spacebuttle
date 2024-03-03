package ru.otus.spacebuttle.service;

public interface AuthTokenService {
    String generateToken(Long clientId);
}
