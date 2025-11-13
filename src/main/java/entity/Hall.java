package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Predsatvlja objekt sportke dvorane koja se može rezervirati i zakazati.
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
    private SportType supportedSport;
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Defaultni konstruktor klase {@link Hall}
     */
    public Hall(){}

    /**
     * Inicjalizira novi objekt klase {@link Hall} sa određenim parametrima
     *
     * @param name naziv dvorane
     * @param doorNumber oznaka na vratima dvorame
     * @param capacity kapacaitet dvorane
     * @param supportedSport tip sporta za koji je namijenjena dvorana
     */
    public Hall(String name, String doorNumber, Integer capacity, SportType supportedSport) {
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
        return bookings.stream().noneMatch(b-> {

            LocalDateTime start = b.dateTime();
            LocalDateTime end = start.plusMinutes(b.trainingTime());
            return !(time.isBefore(start) || time.isAfter(end));

        });
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
       SequencedSet<Booking> result = new TreeSet<>(
               Comparator.comparing(Booking::dateTime)
       );
       result.addAll(
        bookings.stream().filter(b->b.dateTime().toLocalDate().equals(date)).toList()
       );

       System.out.print("Pronađena je rezervacije koja odgovara vašem datumu: ");
       System.out.println(result.getFirst().toString()); //getFirst() samo zbog sequenced
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
       if (!isAvailable(newBooking.dateTime(), newBooking.trainingTime())) {
           System.out.println("Termin nije dostupan za dodavanje!");
           return;
       }
       if(bookings.size()>BrojBookinga)
       {
           System.out.println("Nema više prostora za dodavanje bookinga!");
       return;
       }
        bookings.add(newBooking);
       System.out.println("Termin dodan za trenera: " + newBooking.coach().getFirstName() + " " + newBooking.coach().getLastName());

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

    /**
     * Dohvaća tip sporta kojeg dvorana podržava.
     *
     * @return vraća sport kao {@link SportType}.
     */
    public SportType getSport() {
        return supportedSport;
    }
}
