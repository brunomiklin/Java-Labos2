package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Predsatvlja objekt sportke dvorane koja se može rezervirati i zakazati.
 *
 *
 * Klasa {@code Hall} implementira sučelja {@link Reservable} i {@link Schedulable}.
 * Svaka dvorana ima ogranićeni broj mogućih bookinga, kapacitet i naziv, broj vrata
 * i podrižani sport.
 * 
 */
non-sealed public class Hall implements Reservable, Schedulable {
    private static final Integer BrojBookinga = 5;
    private String name,doorNumber;
    private Integer capacity;
    private String supportedSport;
    private Booking[] bookings = new Booking[BrojBookinga];


    /**
     * Inicjalizira novi objekt klase {@link Hall} sa određenim parametrima
     *
     * @param name naziv dvorane
     * @param doorNumber oznaka na vratima dvorame
     * @param capacity kapacaitet dvorane
     * @param supportedSport tip sporta za koji je namijenjena dvorana
     */
    public Hall(String name, String doorNumber, Integer capacity, String supportedSport) {
        this.name = name;
        this.doorNumber = doorNumber;
        this.capacity = capacity;
        this.supportedSport = supportedSport;

    }

    /**
     * Vraca string verziju {@link Hall} objekta, ukljucujuci njegovo ime,
     * broj na vratima, kapacitet i sport koji podržava.
     *
     *
     *
     * @return vraća string u obliku name,broj na vratima: doorNumber kapacitet: capacity, sport: + supportedSport.
     */
    @Override
    public String toString() {
        return  name + ",broj na vratima:" + doorNumber + ", kapacitet:" + capacity +
                ", sport:" + supportedSport;
    }


    /**
     * Provjerava dostupnost dvorane za određeno vrijeme i vrijeme trajanja.
     *
     *
     * Metoda određuje postoji li mogućnost rezerviranja nekog bookinga sprjecavajući
     * preklapanja više različitih bookinga.
     *
     * @param time vrijeme traženog booking.
     * @param duration vrijeme trajanja bookinga.
     * @return true ako je dvorana dostupna, false ako nije odnosno vrijeme se preklapa.
     */
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


    /**
     * Dohvaća i ispisuje sve bookinge za određeni datum
     *
     * Metoda iterira kroz sve bookinge te ispisuje one koji
     * se preklapaju sa unesenom vrijednost u parametru metode.
     *
     * @param date datum bookinga koji želimo pronaći
     */
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

    /**
     * Dodaje novi booking u niz bookinga, ako je vrijeme i trajanje dostupno
     *
     * Metoda provjerava dostupnost termina za booking koristeći
     * {@code isAvailable} metodu. Ako je vrijeme zauzeto ispisuje određenu
     * poruku, u suprotnom traći prazno mjesto u nizu bookinga i sprema novi booking.
     *
     * @param newBooking booking koji će biti dodan u niz.
     */
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

    /**
     * Dohavaća ime dvorane.
     *
     * @return vraća ime dvorane u obliku String-a.
     */
    public String getName() {
        return name;
    }

    /**
     * Dohvaća kapacitet dvorane.
     *
     * @return vraća kapacitet dovrane u obliku Integer-a
     */
    public Integer getCapacity() {
        return capacity;
    }
}
