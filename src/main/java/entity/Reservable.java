package entity;

import java.time.LocalDateTime;

public interface Reservable {

     boolean isAvailable(LocalDateTime time,Integer duration);

}
