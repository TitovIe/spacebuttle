package ru.otus.spacebuttle.service;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface ClientService {
    void registerUser(String nickname, String clientSecret);
    Long registerGame(List<String> nicknames);
    void checkCredentials(String nickname, String clientSecret) throws LoginException;
    void checkGameParticipant(String nickname, Long gameId) throws LoginException;
}
