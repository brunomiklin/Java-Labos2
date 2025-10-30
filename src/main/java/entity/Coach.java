package entity;

import java.time.LocalDateTime;

public class Coach extends Person {
    private static final Integer BrojBookinga = 5;
    private String specializtion;
    private Booking[] myBookings = new Booking[BrojBookinga];


    private Coach(CoachBuilder builder)
    {
        super(builder);
        this.specializtion = builder.specialization;
    }

    public static class CoachBuilder extends Person.PersonBuilder
    {
        private String specialization = "";


        public CoachBuilder(String OIB, String firstName, String lastName) {
            super(OIB, firstName, lastName);
        }

        public CoachBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public Coach build()
        {
            return new Coach(this);
        }
    }



    public void createBooking(Hall hall, LocalDateTime dateTime, Integer trainingTime)
    {
        if(!hall.isAvailable(dateTime,trainingTime))
        {
            System.out.println("Dvorana " + hall.getName() + " je zauzeta " + dateTime.toLocalDate() + " u" + dateTime.toLocalTime());
        }
        else
        {
            Booking booking = new Booking(this,hall,dateTime,trainingTime);
            hall.addBooking(booking);
            for (Integer i= 0; i < myBookings.length; i++) {
                if (myBookings[i] == null) {
                    myBookings[i] = booking;
                    break;
                }
              if(i==myBookings.length-1)
              {
                  System.out.println(getFirstName() + " ima popunjen raspored!");
              }
        }

    }

    }


    public Booking[] getMyBookings() {
        return myBookings;
    }

    public void printMyBookings()
    {
        System.out.println("Svi rezervirani treninzi:");
        for(Integer i =0;i<myBookings.length;i++)
        {
            if (myBookings[i] == null) {

                break;
            }
            System.out.println(myBookings[i]);
        }
    }

    @Override
    public void printPersonalData()
    {
        System.out.println("Trener: OIB: " + getOIB() + " " + getFirstName() + " " + getLastName());
    }
}
