package entity;

/**
 * Predstavlja korisnika sustava koji nasljeđuje osobu (Person) i ima mogućnost
 * upravljanja rezervacijama dvorana.
 *
 * Korisnik ima korisničko ime, lozinku i ograničen broj rezervacija koje može držati.
 *
 * Konstrukcija objekta koristi Builder obrazac za lakše i sigurnije kreiranje korisnika.
 *
 * Metoda {@link #joinBooking(Booking)} omogućuje dodavanje nove rezervacije u korisnikov popis,
 * dok {@link #allMyBookings} ispisuje sve rezervacije korisnika.
 *
 */
public class User extends Person {
    private static final Integer BrojBookinga = 5;
    private String username;
    private String password;
    private Booking[] myBookings = new Booking[BrojBookinga];

    /**
     * Ispisuje osnovne podatke o korisniku.
     */
    @Override
    public void printPersonalData() {
        System.out.println("Korisnik: OIB: " + getOIB() + " " + getFirstName() + " " + getLastName());
    }

    /**
     * Privatni konstruktor koji se koristi unutar UserBuildera.
     *
     * @param builder objekt buildera sa svim potrebnim podacima
     */
    private User(UserBuilder builder) {
        super(builder);
        this.username = builder.username;
        this.password = builder.password;
    }

    /**
     * Builder klase za kreiranje instanci korisnika.
     */
    public static class UserBuilder extends Person.PersonBuilder {
        private String username = "";
        private String password = "";

        /**
         * Konstruktor buildera s obaveznim podacima.
         *
         * @param OIB identifikacijski broj korisnika
         * @param firstName ime korisnika
         * @param lastName prezime korisnika
         */
        public UserBuilder(String OIB, String firstName, String lastName) {
            super(OIB, firstName, lastName);
        }

        /**
         * Postavlja korisničko ime.
         *
         * @param username korisničko ime
         * @return trenutni builder za moguću daljnju konfiguraciju
         */
        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Postavlja lozinku.
         *
         * @param password lozinka
         * @return trenutni builder za moguću daljnju konfiguraciju
         */
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Kreira instancu korisnika s prethodno postavljenim podacima.
         *
         * @return novi korisnik
         */
        public User build() {
            return new User(this);
        }
    }

    /**
     * Dodaje rezervaciju korisniku ako još nije dosegnut maksimalni broj rezervacija.
     *
     * @param booking rezervacija koju korisnik želi dodati
     */
    public void joinBooking(Booking booking) {
        try {
            if (booking == null) {
                throw new IllegalArgumentException("Booking ne može biti null!");
            }

            for (int i = 0; i < myBookings.length; i++) {
                if (myBookings[i] == null) {
                    myBookings[i] = booking;
                    System.out.println("Korisnik " + getFirstName() + " uspješno je rezervirao termin u dvorani "
                            + booking.hall().getName() + " -> " + booking.dateTime());
                    return;
                }
            }

            System.out.println("Dosegli ste maksimalan broj rezervacija (" + BrojBookinga + ").");

        } catch (IllegalArgumentException e) {
            System.out.println("Greška: " + e.getMessage());
        }
    }

    /**
     * Ispisuje sve rezervacije koje korisnik ima.
     */
    public void allMyBookings() {
        if (myBookings[0] == null) {
            System.out.println("Nemate rezerviranih termina!");
        } else {
            System.out.println("Treninzi korisnika " + this.getFirstName() + ":");
            for (int i = 0; i < myBookings.length; i++) {
                if (myBookings[i] != null) {
                    System.out.println(myBookings[i]);
                }
            }
        }
    }
}
