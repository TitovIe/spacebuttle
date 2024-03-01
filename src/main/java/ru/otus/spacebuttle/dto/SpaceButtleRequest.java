package ru.otus.spacebuttle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpaceButtleRequest {
    private Integer ownerId;
    private Integer gameId;
    private Integer objectId;
    private Integer operationId;
    private Object[] args;
}
