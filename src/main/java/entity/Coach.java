package entity;

import java.time.LocalDateTime;

/**
 * Predstavlja trenera koji nasljeđuje klasu Person i ima mogućnost
 * kreiranja rezervacija termina za treninge u dvoranama.
 *
 * Trener ima specijalizaciju i može držati ograničen broj rezervacija.
 *
 * Konstrukcija objekta koristi Builder obrazac za lakše i sigurnije kreiranje trenera.
 *
 * Metoda {@link #createBooking(Hall, LocalDateTime, Integer)} omogućuje kreiranje nove rezervacije,
 * dok {@link #printMyBookings} ispisuje sve rezervacije trenera.
 *
 */
public class Coach extends Person {
    private static final Integer BrojBookinga = 5;
    private String specializtion;
    private Booking[] myBookings = new Booking[BrojBookinga];

    /**
     * Privatni konstruktor koji se koristi unutar CoachBuildera.
     *
     * @param builder objekt buildera sa svim potrebnim podacima
     */
    private Coach(CoachBuilder builder) {
        super(builder);
        this.specializtion = builder.specialization;
    }

    /**
     * Builder klase za kreiranje instanci trenera.
     */
    public static class CoachBuilder extends Person.PersonBuilder {
        private String specialization = "";

        /**
         * Konstruktor buildera s obaveznim podacima.
         *
         * @param OIB identifikacijski broj trenera
         * @param firstName ime trenera
         * @param lastName prezime trenera
         */
        public CoachBuilder(String OIB, String firstName, String lastName) {
            super(OIB, firstName, lastName);
        }

        /**
         * Postavlja specijalizaciju trenera.
         *
         * @param specialization specijalizacija
         * @return trenutni builder za moguću daljnju konfiguraciju
         */
        public CoachBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        /**
         * Kreira instancu trenera s prethodno postavljenim podacima.
         *
         * @return novi trener
         */
        public Coach build() {
            return new Coach(this);
        }
    }

    /**
     * Kreira novu rezervaciju termina u dvorani ako je termin slobodan.
     *
     * @param hall dvorana za rezervaciju
     * @param dateTime početno vrijeme termina
     * @param trainingTime trajanje treninga u minutama
     */
    public void createBooking(Hall hall, LocalDateTime dateTime, Integer trainingTime) {
        if (!hall.isAvailable(dateTime, trainingTime)) {
            System.out.println("Dvorana " + hall.getName() + " je zauzeta " + dateTime.toLocalDate() + " u " + dateTime.toLocalTime());
        } else {
            Booking booking = new Booking(this, hall, dateTime, trainingTime);
            hall.addBooking(booking);
            for (Integer i = 0; i < myBookings.length; i++) {
                if (myBookings[i] == null) {
                    myBookings[i] = booking;
                    break;
                }
                if (i == myBookings.length - 1) {
                    System.out.println(getFirstName() + " ima popunjen raspored!");
                }
            }
        }
    }

    /**
     * Dohvaća sve rezervacije trenera.
     *
     * @return niz rezervacija
     */
    public Booking[] getMyBookings() {
        return myBookings;
    }

    /**
     * Ispisuje sve rezervirane treninge trenera.
     */
    public void printMyBookings() {
        System.out.println("Svi rezervirani treninzi:");
        for (Integer i = 0; i < myBookings.length; i++) {
            if (myBookings[i] == null) {
                break;
            }
            System.out.println(myBookings[i]);
        }
    }

    /**
     * Ispisuje osobne podatke trenera.
     */
    @Override
    public void printPersonalData() {
        System.out.println("Trener: OIB: " + getOIB() + " " + getFirstName() + " " + getLastName());
    }
}
