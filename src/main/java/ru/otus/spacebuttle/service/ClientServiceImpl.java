package ru.otus.spacebuttle.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.otus.spacebuttle.entity.Game;
import ru.otus.spacebuttle.entity.Player;
import ru.otus.spacebuttle.entity.User;
import ru.otus.spacebuttle.repository.GameRepository;
import ru.otus.spacebuttle.repository.UserRepository;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Override
    public void registerUser(String nickname, String clientSecret) {
        String hash = BCrypt.hashpw(clientSecret, BCrypt.gensalt());

        userRepository.save(User.builder()
                        .nickname(nickname)
                        .hash(hash)
                .build());
    }

    @Override
    public Long registerGame(List<String> nicknames) {
        List<Player> players = nicknames.stream()
                .map(nickname -> Player.builder()
                        .nickname(nickname)
                        .build())
                .toList();

        Game game = new Game();
        game.addPlayers(players);
        game = gameRepository.save(game);

        return game.getId();
    }

    @Override
    public void checkCredentials(String nickname, String clientSecret) throws LoginException, NoSuchElementException {
        Optional<User> oUser = userRepository.findByNickname(nickname);

        if (oUser.isPresent()) {
            User user = oUser.get();
            String hash = user.getHash();
            if (hash == null)
                throw new LoginException("Client with nickname: " + user.getNickname() + " not found");

            if (!BCrypt.checkpw(clientSecret, hash)) {
                throw new LoginException("Secret is incorrect");
            }
        } else {
            throw new NoSuchElementException(nickname);
        }
    }

    @Override
    public void checkGameParticipant(String nickname, Long gameId) {
        gameRepository.findById(gameId).ifPresentOrElse(game -> {
            List<Player> players = game.getPlayers();
            players.stream().filter(player -> Objects.equals(player.getNickname(), nickname)).findFirst().orElseThrow(NoSuchElementException::new);
        }, () -> {throw new NoSuchElementException(nickname);});
    }
}
