package ru.otus.spacebuttle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.spacebuttle.dto.SpaceButtleRequest;
import ru.otus.spacebuttle.service.SpaceButtleService;

@Controller
@RequiredArgsConstructor
public class SpaceButtleController {
    private final SpaceButtleService spaceButtleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/command")
    public void sendCommand(@Payload SpaceButtleRequest spaceButtleRequest) {
        Object response = spaceButtleService.makeAction(spaceButtleRequest);
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(spaceButtleRequest.getOwnerId()),
                "/queue/messages",
                response.toString());
    }
}
