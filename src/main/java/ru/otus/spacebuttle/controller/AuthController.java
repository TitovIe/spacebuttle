package ru.otus.spacebuttle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spacebuttle.dto.*;
import ru.otus.spacebuttle.service.ClientService;
import ru.otus.spacebuttle.service.AuthTokenService;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientService clientService;
    private final AuthTokenService authTokenService;

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        clientService.registerUser(userDto.getNickname(), userDto.getClientSecret());
        return ResponseEntity.ok("Registered user");
    }

    @PostMapping("/register/game")
    public ResponseEntity<Long> registerGame(@RequestBody GameDto gameDto) {
        Long gameId = clientService.registerGame(gameDto.getNicknames());
        return ResponseEntity.ok(gameId);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestBody TokenRequest tokenRequest) throws LoginException {
        String nickname = tokenRequest.getNickname();
        String clientSecret = tokenRequest.getClientSecret();
        Long gameId = tokenRequest.getGameId();

        clientService.checkCredentials(nickname, clientSecret);
        clientService.checkGameParticipant(nickname, gameId);

        return ResponseEntity.ok(new TokenResponse(authTokenService.generateToken(gameId)));
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }
}