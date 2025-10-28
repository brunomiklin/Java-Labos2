package entity;

import java.time.LocalDate;

public sealed interface Schedulable permits Hall {
    void addBooking(Booking b);
    //void removeBooking(Booking b);
    void getBookingsForDate(LocalDate date);
}
