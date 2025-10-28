package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


non-sealed public class Hall implements Reservable, Schedulable {
    private static final Integer BrojBookinga = 5;
    String name,doorNumber;
    Integer capacity;
    String supportedSport;
    Booking[] bookings = new Booking[BrojBookinga];
    public Hall(String name, String doorNumber, Integer capacity, String supportedSport) {
        this.name = name;
        this.doorNumber = doorNumber;
        this.capacity = capacity;
        this.supportedSport = supportedSport;

    }

    @Override
    public String toString() {
        return  name + ",broj na vratima:" + doorNumber + ", kapacitet:" + capacity +
                ", sport:'" + supportedSport;
    }

    @Override
   public boolean isAvailable(LocalDateTime time,Integer duration)
   {
       for(Integer i=0;i<bookings.length;i++)
       {
           if (bookings[i] == null) {
               continue;
           }
           LocalDateTime start = bookings[i].dateTime();
           LocalDateTime end = bookings[i].dateTime().plusMinutes(bookings[i].trainingTime()*60);


           if(!(time.isBefore(start) || time.isAfter(end)))
           {
               return false;
           }
       }
       return true;
   }


   @Override
   public void getBookingsForDate(LocalDate date)
   {
       Integer brojac =1;
        for(int i=0;i<bookings.length;i++)
        {
            if(bookings[i].dateTime().toLocalDate()==date)
            {

                System.out.println(((brojac++) + "Trener: " + bookings[i].coach().getFirstName() + " Dvorana:" + bookings[i].hall()));
            }
        }
   }

    @Override
    public void addBooking(Booking newBooking)
   {
       if(!isAvailable(newBooking.dateTime(),newBooking.trainingTime()))
       {
           System.out.println("Termin nije dostupan za dodavanje!");
       }
       for(Integer i = 0;i<bookings.length;i++)
       {
           if(bookings[i]==null)
           {
               bookings[i] = newBooking;
               System.out.println("Termin dodan za trenera " + newBooking.coach().getFirstName());
               return;
           }
       }
       System.out.println("Nema više prostora za dodavanje bookinga!");
   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
