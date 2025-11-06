package entity;

/**
 * Predstavlja generički tip osobe sa osobnim podacima i ponašanjima.
 * Služi kao šablona za klase poput {@link Coach} i {@link User}.
 *
 */
public abstract class Person {
    protected String OIB, firstName,lastName,email,phoneNumber;

    /**
     * Konstuira {@link Person} objekt korsite ći PersonBuilder
     *
     *
     * @param builder tipa {@link PersonBuilder} sadži varijable
     *                potrebne kako bi se inicjalizirao {@link Person} objekt.
     *
     */
    protected Person(PersonBuilder builder)
    {
        this.OIB = builder.OIB;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
    }


    /**
     * Predstavlja apstraktn builder za kreiranje {@link Person} objekata.
     *
     * Pruža mnogo opcionalnih parametara i onih obveznih omogućavajući korisnik
     * fleksibilnost kod izarade samog objekta.
     *
     */
    public abstract static class PersonBuilder
    {
        private final String OIB;
        private final String firstName;
        private final String lastName;

        /**
         * Konstruira novu instancu tipa {@link PersonBuilder}.
         *
         * @param OIB jedinstveni identifikacijski broj u Republici Hrvatskoj
         * @param firstName ime osobe
         * @param lastName  prezime osobe
         */
        public PersonBuilder(String OIB,String firstName, String lastName)
        {
            this.OIB = OIB;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        private String email="";

        /**
         * Postavlja email adresu za builder objekt.
         *
         * Metoda omogućava specificiranje emaila koji će biti
         * upotrebljen kod istanciranja {@link Person} objekta.
         *
         * @param email email varijabla koji će se pridužiti klasnoj {@code email}  varijabli.
         * @return trnutna instance objekta {@code PersonBuilder} omogućava lanačnje metoda.
         */
        public PersonBuilder email(String email)
        {
            this.email = email;
            return this;
        }
        private String phoneNumber="";

        /**
         * Postavlja broj telefona za builder objekt.
         *
         * Metoda omogućava specificiranje telefonskog broja koji će biti
         * upotrebljen kod istanciranja {@link Person} objekta.
         *
         * @param phoneNumber phoneNumber varijabla koji će se pridužiti klasnoj {@code PhoneNumber} varijabli.
         * @return trnutna instance objekta {@code PersonBuilder} omogućava lanačnje metoda.
         */
        public PersonBuilder phoneNumber(String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
            return this;
        }

        /**
         * Kostruktor vraća objekt {@link Person} klase.
         *
         * Metoda incijalizira {@code Person} objekt koristeći
         * prethodno zadane parametre pomoću {@link PersonBuilder}.
         *
         * @return vraća potpuni objekt {@link Person}
         */
        public abstract Person build();


    }

    /**
     * Ispsuje osobne podatke klase tipa {@link Person}
     *
     * Metoda je apstraktna što znaći da sve klase koje nasljeđuju
     * klasu {@link Person} moraju nadrediti ovu metodu.
     */
     public abstract void printPersonalData();


    /**
     * Dohvaća OIB klase {@link Person}
     *
     * @return vraća OIB u obliku String-a
     */
    public String getOIB() {
        return OIB;
    }


    /**
     * Dohvaća ime klase {@link Person}
     *
     * @return vraća ime u obliku String-a
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Dohvaća ime prezime {@link Person}
     *
     * @return vraća prezime u obliku String-a
     */
    public String getLastName() {
        return lastName;
    }


}
