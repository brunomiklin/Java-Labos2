package entity;

public class User extends Person {
    private static final Integer BrojBookinga = 5;
    private String username;
    private String password;
    private Booking[] myBookings = new Booking[BrojBookinga];

    @Override
    public void printPersonalData()
    {
        System.out.println("Korisnik: OIB: " + getOIB() + " " + getFirstName() + " " + getLastName());
    }

    private User(UserBuilder builder)
    {
        super(builder);
        this.username = builder.username;
        this.password = builder.password;

    }

    public static class UserBuilder extends Person.PersonBuilder
    {
        private String username="";
        private String password="";


        public UserBuilder(String OIB,String firstName, String lastName)
        {
            super(OIB,firstName,lastName);
        }

        public UserBuilder username(String username)
        {
            this.username = username;
            return this;
        }
        public UserBuilder password(String password)
        {
            this.password = password;
            return this;
        }
        public User build()
        {
                return new User(this);
        }
    }

    public void joinBooking(Booking booking)
    {
        for(Integer i = 0;i<myBookings.length;i++)
        {
            if(myBookings[i]==null)
            {
                myBookings[i] = booking;
                System.out.println("Korisnik " + getFirstName() +" uspješno je rezvirao termin u dvorani !" + booking.hall().name + "! -> " + booking.dateTime());
                return;
            }
        }

    }

    public void allMyBookings()
    {
        if(myBookings[0]==null)
        {
            System.out.println("Nemate rezerviranih termina!");
        }
        else
        {
            System.out.println("Treninzi korisnika " + this.getFirstName() +":");
            for(int i=0;i<myBookings.length;i++)
            {

                if(myBookings[i]!=null)
                {
                    System.out.println(myBookings[i]);
                }
            }
        }

    }

}
