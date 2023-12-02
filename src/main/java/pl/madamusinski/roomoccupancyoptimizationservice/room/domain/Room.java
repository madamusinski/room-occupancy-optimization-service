package pl.madamusinski.roomoccupancyoptimizationservice.room.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Room {
    Type type;
    State state;

    enum Type {
        ECONOMY,
        PREMIUM
    }

    enum State {
        FREE,
        BOOKED
    }
}
