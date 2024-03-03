package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.otus.spacebuttle.dto.*;
import ru.otus.spacebuttle.generator.GameGenerator;
import ru.otus.spacebuttle.generator.ObjectGenerator;
import ru.otus.spacebuttle.generator.OperationGenerator;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {
    private static final String WS_PATH = "ws://localhost:%d/ws";
    private static final String SUBSCRIBE_PATH = "/user/0/queue/messages";
    private static final String SEND_PATH = "/spacebuttle/command";
    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private WebSocketStompClient webSocketStompClient;

    @SneakyThrows
    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @SneakyThrows
    @Test
    void execute_should_change_position_to_17x_5y_because_move_command_called_by_client() {
        //Given
        BlockingQueue<String> result = new ArrayBlockingQueue<>(1);

        GameGenerator.init();
        GameGenerator.createGame();
        ObjectGenerator.init();
        OperationGenerator.init();
        ObjectGenerator.createObject(SpaceShip.class, new HashMap<>() {{
            put("Position", new Vector(12.0, 5.0));
            put("Velocity", 5.0);
            put("OwnerId", 0);
            put("Direction", 0);
            put("DirectionsNumber", 6);
        }}, MovableAdapter.class);

        testRestTemplate.postForEntity(
                String.format("http://localhost:%d/auth/register/user", port),
                UserDto.builder()
                        .nickname("ubivator666")
                        .clientSecret("666")
                        .build(),
                String.class
        );

        testRestTemplate.postForEntity(
                String.format("http://localhost:%d/auth/register/user", port),
                UserDto.builder()
                        .nickname("manipulator777")
                        .clientSecret("777")
                        .build(),
                String.class
        );

        testRestTemplate.postForEntity(
                String.format("http://localhost:%d/auth/register/game", port),
                GameDto.builder()
                        .nicknames(List.of(
                                "ubivator666",
                                "manipulator777"
                        ))
                        .build(),
                Long.class
        );

        ResponseEntity<TokenResponse> token = testRestTemplate.postForEntity(
                String.format("http://localhost:%d/auth/token", port),
                TokenRequest.builder()
                        .gameId(1L)
                        .nickname("manipulator777")
                        .clientSecret("777")
                        .build(),
                TokenResponse.class
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getBody().getToken());
        StompSession session = webSocketStompClient.connectAsync(
                String.format(WS_PATH, port),
                new WebSocketHttpHeaders(headers),
                new StompSessionHandlerAdapter() {}).get(50, SECONDS);
        session.subscribe(SUBSCRIBE_PATH, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                result.add((String) payload);
            }
        });

        SpaceButtleRequest spaceButtleRequest = SpaceButtleRequest.builder()
                .ownerId(0)
                .gameId(0)
                .objectId(0)
                .operationId(2)
                .args(new Object[]{2,3})
                .build();

        //When
        session.send(SEND_PATH, spaceButtleRequest);

        //Then
        await().atMost(2, SECONDS).untilAsserted(() -> assertEquals("MovableAdapter(o=SpaceShip(properties={DirectionsNumber=6, OwnerId=0, Position=Vector[x=17.0, y=5.0], Velocity=5.0, Direction=0}))", result.take()));
    }
}
