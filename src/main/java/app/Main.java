package app;
import entity.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Klasa {@link Main} za upravljanje sportskim centrima i terminima.
 *
 * Sadrži metode za generiranje korisnika i trenera, kreiranje dvorana,
 * upravljanje bookingom te pretragu korisnika i dvorana.
 *
 * @author Bruno Miklin
 * @since JAVA 25
 */
public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class);
    public static final Integer NUMBER_OF_OBJECTS = 5;
    public static final Integer NUMBER_OF_USERS = 10;

    /**
     * Pozivanje različitih metoda te generianje i manipulacija korisnicima, bookingom i dvoranama
     *
     * Glavna metoda u aplikaciji omogućuje generiranje trenera i korisnika, koje kasnije koristi
     * kao izvor kod kreiranja bookinga, pretrage i u konačnici pružanje usluge istima
     */
    static void main() {
        log.trace("Ulaz u metodu main.");
        Scanner sc = new Scanner(System.in);
        Person[] osobe = new Person[NUMBER_OF_USERS];
        Hall[] dvorane = new Hall[NUMBER_OF_OBJECTS];

        for (Integer i = 0; i < NUMBER_OF_USERS; i++) {
            log.trace("Iteracija kroz for-petlju (NUMBER_OF_USERS)");
            while (true)
            {

                try {
                    System.out.print("Odaberi tip korisnika (1. User, 2. Trener): ");
                    Integer choice = Integer.parseInt(sc.nextLine());
                     switch (choice) {
                        case 1 -> osobe[i] =  generateUser(sc);
                        case 2 -> osobe[i] = generateCoach(sc);
                        default -> {
                            System.out.println("Nepoznat izbor, unosimo User po defaultu.");
                             osobe[i] = generateUser(sc);

                        }
                    };
                    log.info("Korisnik je uspješno kreiran!");
                     break;
                } catch (InvalidOibException e) {
                    System.out.println(e.getMessage());
                    log.error(e.getMessage());
                }
                catch (IllegalArgumentException iag)
                {
                    System.out.println(iag.getMessage());
                    log.error(iag.getMessage());
                }


            }
        }


        for (Integer i = 0; i < NUMBER_OF_OBJECTS; i++) {
            log.trace("Iteracija kroz for-petlju (NUMBER_OF_OBJECTS)");
            boolean validInput = false;
            while (!validInput) {
                try {
                    log.debug("Pozivam metodu generateHall za dvoranu broj " + (i+1) + ".");
                    dvorane[i] = generateHall(sc);
                    validInput = true;
                } catch (InvalidHallCapacity ihe) {
                    System.out.println(ihe.getMessage());
                    log.error(ihe.getMessage());
                }
            }
        }

        log.debug("Pozivam metodu createBooking pomocu ovih parametara [Person[] " + osobe.getClass() + " [Scanner " + sc.getClass() + "] [Halls[] " + dvorane.getClass()+"]" );
        createBooking(osobe, sc, dvorane);

        log.debug("Pozivam metodu createBooking pomocu ovih parametara [Person[] " + osobe.getClass() + " [Scanner " + sc.getClass() + "]" );
        joinBooking(osobe, sc);

        log.debug("Pozivam metodu searchUsers pomocu ovih parametara [Person[] " + osobe.getClass() + " [Scanner " + sc.getClass() + "]");
        searchUsers(osobe, sc);

        log.debug("Pozivam metodu searchUsers pomocu ovih parametara [Halls[] " + osobe.getClass() + " [Scanner " + sc.getClass() + "]");
        searchHalls(dvorane, sc);
        log.trace("Izlaz iz metode main.");
    }

    /**
     * Kreiranje bookinga pomoću niza osoba (trenera) i dvorana.
     *
     * Metoda uzima niz osoba iz kojih instancira trenere te traži od korisnika
     * odabir određenog trenera. Odabrani trener se kasnije povezuje sa dvoranom te
     * u zajedno čine određenu cijelinu koju nazivamo booking.
     *
     * @param osobe niz svih korisnika i trenera (u ovoj metodi koristimo samo instance klase {@link Coach}).
     * @param sc Scanner se šalje kroz parametar a definiran je napočetku u metodi main.
     * @param dvorane niz svih dostupnih dvorana kreiranih u metodi main.
     */
    private static void createBooking(Person[] osobe, Scanner sc, Hall[] dvorane) {
        log.trace("Ulaz u metodu createBooking.");
        log.info("Kreiramo booking.");
        String choice="";
        do {
            log.info("Odabir trenera.");
            System.out.println("\nOdaberi trenera:");
            Integer brojac = 0;
            Coach[] choiceCoaches = new Coach[NUMBER_OF_USERS];
            Integer choiceHall,cohiceCoach;

            for (Integer i = 0; i < osobe.length; i++) {
                if (osobe[i] instanceof Coach trener) {
                    choiceCoaches[brojac] = trener;
                    System.out.println((brojac + 1) + ". " + trener.getFirstName() + " " + trener.getLastName());
                    brojac++;
                }

            }
            try
            {
                System.out.print("Odabir >>");
                cohiceCoach = sc.nextInt();
                sc.nextLine();
                if (cohiceCoach < 1 || cohiceCoach > brojac) {
                    System.out.println("Nevažeći izbor trenera! Pokušajte ponovo!");
                    log.warn("Korisnik je pokušao odabrati trenera koji nepostoji!");
                    continue;

                }
            }   catch (InputMismatchException ime)
            {
                System.out.println("Unijeli ste slovo umjesto broja!");
                log.error(ime.getMessage());

                continue;
            }


            do {
                log.info("Odabir dvorana.");
                System.out.println("Odberi dvoranu:");
                for (Integer i = 0; i < dvorane.length; i++) {
                    System.out.println((i + 1) + ". " + dvorane[i].toString());

                }
                try {
                    System.out.print("Odabir >> ");
                    choiceHall = sc.nextInt();
                    sc.nextLine();
                    if (choiceHall < 1 || choiceHall > dvorane.length) {
                        System.out.println("Nevažeći izbor dvorane! Pokušajte ponovo!");
                        log.warn("Korisnik je pokušao odabrati dvoranu koja nepostoji!");
                        continue;
                    }

                } catch (InputMismatchException ime) {
                    log.error(ime.getMessage());
                    System.out.println("Unijeli ste slovo umjesto broja!");
                    sc.nextLine();
                    continue;
                }


                LocalDateTime datum = null;
                while (true) {
                    try {
                        log.info("Unos datuma i vremena.");
                        System.out.print("Unesi datum i vrijeme (dd.MM.yy HH:mm): ");
                        String datumString = sc.nextLine();
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
                        datum = LocalDateTime.parse(datumString, fmt);
                        break;
                    } catch (DateTimeParseException dtpe) {
                        log.error(dtpe.getMessage());
                        System.out.println("Unijeli ste krivi format! Pokušajte ponovo!");

                    }
                }

                log.info("Unos duljine trajanja treninga (u minutama).");

                while(true)
                {
                    try
                    {
                        System.out.print("Unesi duljinu trajanja treninga (u minutama): ");
                        Integer trainingTime = sc.nextInt();
                        sc.nextLine();
                        choiceCoaches[cohiceCoach - 1].createBooking(dvorane[choiceHall - 1], datum, trainingTime * 60);
                        break;
                    }
                    catch (ArrayIndexOutOfBoundsException aiofbe)
                    {
                        System.out.println("Desila se greška pristupa trenerima!");
                        sc.nextLine();
                        log.error(aiofbe.getMessage());

                    }
                    catch (InputMismatchException ime)
                    {
                        System.out.println("Unijeli ste broj za minute! Pokušajte ponovo.");
                        sc.nextLine();
                        log.error(ime.getMessage());
                    }
                }



                System.out.println("Želite li dodati još bookinga za trenra " + choiceCoaches[cohiceCoach - 1].getFirstName() + " (da/ne)?");
                choice = sc.nextLine();
            } while ("DA".equalsIgnoreCase(choice));


            System.out.println("Želite li nastaviti s kreiranjem bookinga (da/ne)?");
            choice = sc.nextLine();
        } while ("DA".equalsIgnoreCase(choice));
        log.trace("Izlaz iz metode createBooking");
    }


    /**
     * Pridruživanje korisnika iz niza osobe({@link User}) postojećim bookinzima u nizu osobe({@link Coach}).
     *
     *Traži se da se prvotno odabere određeni korisnik, a kasnije i trener kod kojeg su nam ponuđeni
     * svi njegovi treninzi koji nas onda opet tjeraju na odabir. Koristimo instaciranje odnosno pattern matching
     * kako bi razdjelili niz osobe na trenere i usere.
     *
     * @param osobe sadrži instance dviju različitih klasa ({@link User}/{@link Coach}) koje koristimo u dva navarata u klasi.
     * @param sc Scanner se šalje kroz parametar a definiran je napočetku u metodi main.
     */
    private static void joinBooking(Person[] osobe, Scanner sc) {
        log.trace("Ulazak u metodu joinBooking");
        log.info("Pridužujemo booking korisniku.");
        String choiceUserContinue="";
        User chosenUser;
        Coach chosenCoach;
        do
        {
            log.info("Odabir korinsika.");
            System.out.println("Odaberi korisnika:");
            User[] choiceUsers = new User[NUMBER_OF_USERS];
            Integer userCount = 0;
            for (int i = 0; i < osobe.length; i++) {
                if (osobe[i] instanceof User korisnik) {
                    choiceUsers[userCount] = korisnik;
                    System.out.println((userCount + 1) + ". " + korisnik.getFirstName() + " " + korisnik.getLastName());
                    userCount++;
                }
            }

            try
            {
                System.out.print("Odabir >> ");
                Integer chosenUserIndex = sc.nextInt() - 1;
                sc.nextLine();
                 chosenUser = choiceUsers[chosenUserIndex];
            }
            catch (InputMismatchException ime)
            {
                System.out.println("Unijeli ste slovo umjesto broja! Pokušajte ponovo.");
                log.error(ime.getMessage());
                sc.nextLine();
                continue;
            }
            catch (ArrayIndexOutOfBoundsException aioobe)
            {
                log.error(aioobe.getMessage());
                System.out.println("Unijeli ste nevažeći broj! Pokušajte ponovo.");
                continue;
            }

            log.info("Odabir trenera");
            System.out.println("Odaberi trenera:");
            Coach[] choiceCoaches = new Coach[NUMBER_OF_USERS];
            Integer coachCount = 0;
            for (Integer i = 0; i < osobe.length; i++) {
                if (osobe[i] instanceof Coach trener) {
                    choiceCoaches[coachCount] = trener;
                    System.out.println((coachCount + 1) + ". " + trener.getFirstName() + " " + trener.getLastName());
                    coachCount++;
                }
            }
            try
            {
                System.out.print("Odabir >> ");
                Integer chosenCoachIndex = sc.nextInt() - 1;
                sc.nextLine();
                chosenCoach = choiceCoaches[chosenCoachIndex];
            }
            catch (InputMismatchException ime)
            {
                log.error(ime.getMessage());
                System.out.println("Unijeli ste slovo umjesto broja! Pokušajte ponovo.");
                sc.nextLine();

                continue;
            }
            catch (ArrayIndexOutOfBoundsException aioobe)
            {
                log.error(aioobe.getMessage());
                System.out.println("Unijeli ste nevažeći broj! Pokušajte ponovo.");
                continue;
            }


            log.info("Odabir booking-a.");
            System.out.println("Odaberi booking:");
            for (Integer i = 0; i < chosenCoach.getMyBookings().length; i++) {
                if (chosenCoach.getMyBookings()[i] != null) {
                    System.out.println((i + 1) + ". " + chosenCoach.getMyBookings()[i]);
                }
            }
            try
            {
                System.out.print("Odabir >> ");
                Integer chosenBookingIndex = sc.nextInt() - 1;
                sc.nextLine();

                Booking chosenBooking = chosenCoach.getMyBookings()[chosenBookingIndex];
                chosenUser.joinBooking(chosenBooking);
            }catch (InputMismatchException ime)
            {

                System.out.println("Unijeli ste slovo umjesto broja! Pokušajte ponovo.");
                sc.nextLine();
                log.error(ime.getMessage());
                continue;
            }
            catch (ArrayIndexOutOfBoundsException aioobe)
            {
                log.error(aioobe.getMessage());
                System.out.println("Unijeli ste nevažeći broj! Pokušajte ponovo.");
                continue;
            }




            System.out.println("Želiš li nastaviti dalje(da/ne)?");
            choiceUserContinue = sc.nextLine();

        }while("DA".equalsIgnoreCase(choiceUserContinue));
        log.trace("Izlazak iz metode joinBooking");
    }


    /**
     * Pretraga niza "osobe" ovisno o parametrima koje korisnik unese.
     *
     *Od korisnika se traži odabir jednog od dva parametra "Ime/Prezime", u metodi se ovisno o odabiru
     * radi jedno od dvije mogućnosti, odnosno traži jednakost "Imena/Prezimena" nekog objekta iz "osobe" klasa {@link Person}
     * i Stringa kojeg je korisnik unio.
     *
     * @param people sadrži instance dviju različitih klasa ({@link User}/{@link Coach}), imajući to na umu ispisat će se različita poruka
     * @param sc - Scanner se šalje kroz parametar a definiran je napočetku u metodi main
     */
    private static void searchUsers(Person[] people,Scanner sc) {
        log.trace("Ulazak u metodu searchUsers.");
        log.info("Preražujemo korisnike.");

        System.out.println("Pretraži korisnika po imenu/prezimenu: ");
        System.out.println("1. ime");
        System.out.println("2. prezime");
        System.out.print("Odabir >> ");
        Integer choice = null;

        while (choice == null) {
            try {
                System.out.print("Odabir >> ");
                choice = sc.nextInt();
                sc.nextLine();
                if (choice != 1 && choice != 2) {
                    System.out.println("Krivo ste unjeli odabir!");
                    choice = null;

                }
            } catch (InputMismatchException ime) {
                log.error(ime.getMessage());
                System.out.println("Unijeli ste slovo umjesto broja! Pokušajte ponovo.");
                sc.nextLine();
            }
        }

        String input;
        switch (choice) {
            case 1 -> System.out.print("Unesi ime:");
            case 2 -> System.out.print("Unesi prezime:");
        }
        input=sc.nextLine();

        if (choice == 1) {
            boolean nadjen = false;
            for (Integer i = 0; i < people.length; i++) {

                if (people[i] == null) {
                    break;
                }
                if (people[i] instanceof User user) {
                    if (user.getFirstName().equalsIgnoreCase(input)) {
                        System.out.println("Pronađen korisnik" + user.getFirstName() + " " + user.getLastName() + ".");
                        System.out.println("---------------");
                        user.allMyBookings();
                        nadjen=true;
                        break;
                    }


                } else if (people[i] instanceof Coach coach) {
                    if (coach.getFirstName().equalsIgnoreCase(input)) {
                        System.out.println("Pronađen trener " + coach.getFirstName() + " " + coach.getLastName() + ".");
                        System.out.println("---------------");
                        coach.printMyBookings();
                        nadjen=true;
                        break;
                    }
                }

            }
            if (nadjen == false) {
                System.out.println("Nije pronađen korisnik!");
            }


        }
        else if(choice==2)
        {
            boolean nadjen = false;
            for (Integer i = 0; i < people.length; i++) {

                if (people[i] == null) {
                    break;
                }
                if (people[i] instanceof User user) {
                    if (user.getLastName().equalsIgnoreCase(input)) {
                        System.out.println("Pronađen korisnik" + user.getFirstName() + " " + user.getLastName() + ".");
                        System.out.println("");
                        user.allMyBookings();
                        nadjen=true;
                        break;
                    }


                } else if (people[i] instanceof Coach coach) {
                    if (coach.getLastName().equalsIgnoreCase(input)) {
                        System.out.println("Pronađen trener" + coach.getFirstName() + " " + coach.getLastName() + ".");
                        System.out.println("");
                        coach.getMyBookings();
                        nadjen=true;
                        break;
                    }
                }

            }
            if (nadjen == false) {
                System.out.println("Nije pronađen korisnik!");
            }
        }
        else
        {
            System.out.println("Krivo ste unjeli odabir!");
        }
        log.trace("Izlazak iz metode serachUsers.");
    }

    /**
     *Generianje korisnika tipa "{@link Coach}" kojeg spremamo u objekt tipa {@link Person} (polimorfizam)
     *
     * Od korisnika se traži unos parametara za klasu "Coach" te se nudi još dodatnih mogućnosti
     * unosa koje se mogu preskoćiti određenim unusom u konzoli. Nakon unosa poziva se BuilderPattern
     * iz klase Coach koji se preko nasljeđivanje wrappa u klasu "Person" te vraća u main klasu kao gotovi
     * objekt.
     *
     * @param sc - Scanner se šalje kroz parametar a definiran je napočetku u metodi main
     * @return vraća objekt tipa "Person" napravljen pomoću Coach.BuilderPattern-a
     * @throws InvalidOibException - provjera korisnikovog unosa za OIB (veličina mu mora biti toćno 11)
     */
    public static Person generateCoach(Scanner sc) throws InvalidOibException{
        log.trace("Ulazak u metodu generateCoach");
        log.info("Generiramo trenere.");

        String tempSpecialization = "";
        String tempPhoneNumber = "";
        String tempEmail = "";
        String tempFirstName,tempLastName;
        System.out.print("Unesi OIB: ");
        String tempOIB = sc.nextLine();
        if(tempOIB.length()!=11)
        {
            throw new InvalidOibException("Oib mora imati točno 11 znakova!");
        }
        while(true)
        {
            System.out.print("Unesi ime: ");
            tempFirstName = sc.nextLine();
            if (tempFirstName.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Ime ne smije sadržavati broj!");

            }
            System.out.print("Unesi prezime: ");
            tempLastName = sc.nextLine();
            if (tempLastName.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Prezime ne smije sadržavati broj!");

            }
            break;
        }

        String choice;
        System.out.println("Želiš li dodati email? (da/ne)");
        choice = sc.nextLine();
        if ("DA".equalsIgnoreCase(choice)) {

            while (true) {
                System.out.print("Unesi mail:");
                tempEmail = sc.nextLine();
                if (tempEmail == null || tempEmail.isEmpty()) {
                    System.out.println("Email ne može biti prazan!");
                    continue;
                } else if (!tempEmail.contains("@")) {
                    System.out.println("Email mora sadržavati @");
                    continue;
                }
                break;
            }

        }
        System.out.println("Želiš li dodati telefon? (da/ne)");
        choice = sc.nextLine();
        if ("DA".equalsIgnoreCase(choice)) {

            System.out.print("Unesi telefon: ");
            tempPhoneNumber = sc.nextLine();
        }
        System.out.println("Želiš li dodati specijaliciju? (da/ne): ");
        choice = sc.nextLine();
        if ("DA".equalsIgnoreCase(choice)) {

            System.out.print("Unesi specijalizaciju: ");
            tempSpecialization = sc.nextLine();
        }

        Person tempCoach = new Coach.CoachBuilder(tempOIB,tempFirstName,tempLastName)
                .specialization(tempSpecialization.isEmpty()?"":tempSpecialization)
                .email(tempEmail.isEmpty()?"":tempEmail)
                .phoneNumber(tempPhoneNumber.isEmpty()?"":tempPhoneNumber)
                .build(); //nebu radilo ako nije gore Person tip

        log.trace("Izlazak iz metode generateCoach");
            return tempCoach;
    }

    /**
     *Generianje korisnika tipa "{@link User}" kojeg spremamo u objekt tipa {@link Person} (polimorfizam)
     *
     * Od korisnika se traži unos parametara za klasu "User" te se nudi još dodatnih mogućnosti
     * unosa koje se mogu preskoćiti određenim unusom u konzoli. Nakon unosa poziva se BuilderPattern
     * iz klase User koji se preko nasljeđivanje wrappa u klasu "Person" te vraća u main klasu kao gotovi
     * objekt.
     *
     * @param sc - Scanner se šalje kroz parametar a definiran je napočetku u metodi main.
     * @return vraća objekt tipa "Person" napravljen pomoću User.BuilderPattern-a.
     * @throws InvalidOibException - provjera korisnikovog unosa za OIB (veličina mu mora biti toćno 11).
     */
    public static Person generateUser(Scanner sc) throws InvalidOibException
    {
        log.trace("Ulazak u metode generateUser");

        log.info("Generiramo korisnike.");
        System.out.print("Unesi OIB: ");
        String tempOIB = sc.nextLine();
        String tempFirstName,tempLastName;
        if(tempOIB.length()!=11)
        {
            throw new InvalidOibException("OIB mora imati točno 11 znakova!");
        }
        while(true)
        {
            System.out.print("Unesi ime: ");
            tempFirstName = sc.nextLine();
            if (tempFirstName.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Ime ne smije sadržavati broj!");

            }
            System.out.print("Unesi prezime: ");
            tempLastName = sc.nextLine();
            if (tempLastName.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Prezime ne smije sadržavati broj!");

            }
            break;
        }


        String tempEmail="";
        String tempPhoneNumber="";
        String tempUsername="";
        String tempPassword="";
        System.out.println("Želiš li dodati email? (da/ne)");
        String choice =  sc.nextLine();
        if("DA".equalsIgnoreCase(choice))
        {
            while (true) {
                System.out.print("Unesi mail:");
                tempEmail = sc.nextLine();

                if (tempEmail == null || tempEmail.isEmpty()) {
                    throw new IllegalArgumentException("Email ne može biti prazan!");
                }

                if (!tempEmail.contains("@")) {
                    throw new IllegalArgumentException("Email mora sadržavati znak @!");
                }
                break;
            }

        }
        System.out.println("Želiš li dodati telefon? (da/ne)");
        choice = sc.nextLine();
        if ("DA".equalsIgnoreCase(choice)) {

            System.out.print("Unesi telefon: ");
            tempPhoneNumber = sc.nextLine();


        }
        System.out.println("Želiš li dodati username i password? (da/ne)");
        choice = sc.nextLine();
        if ("DA".equalsIgnoreCase(choice)) {

            System.out.print("Unesi username: ");
            tempUsername = sc.nextLine();
            System.out.print("Unesi password: ");
            tempPassword = sc.nextLine();
        }

        Person tempUser = new User.UserBuilder(tempOIB,tempFirstName,tempLastName)
                .username(tempUsername.isEmpty()?"":tempUsername)
                .password(tempPassword.isEmpty()?"":tempPassword)
                .email(tempEmail.isEmpty()?"":tempEmail)
                .phoneNumber(tempPhoneNumber.isEmpty()?"":tempPhoneNumber)
                .build();


        log.trace("Izlazak iz metode generateUser");

        return tempUser;

    }

    /**
     * Generainje singularne dvorane i vraćanje iste u main
     *
     * Od korisnika se traći unos parametara za dvoranu, uz provjeru kapaciteta te par
     * sitnih provjera unosa sa konzole.
     *
     * @param sc - Scanner se šalje kroz parametar a definiran je napočetku u metodi main.
     * @return vraća singularni objekt tipa "Hall"
     * @throws InvalidHallCapacity provjerava je li unos za kapacitet koji je korisnik unio manji od 500
     */
    public static Hall generateHall(Scanner sc) throws InvalidHallCapacity
    {
        log.trace("Ulazak u metodu generateHall");

        log.info("Generiramo dvorane.");
        System.out.print("Unesi ime dvorane: ");
        String tempName = sc.nextLine();
        System.out.println("Unesi broj vrata:");

        String tempDoorNumber = sc.nextLine();
        Integer tempCapacity=0;
        boolean uspjeh=false;
        while(!uspjeh)
        {

            while (true)
            {
                try
                {
                    System.out.println("Unesi kapactitet dvorane:");
                    tempCapacity = sc.nextInt();
                    break;
                }
                catch (InputMismatchException ime)
                {
                    log.error(ime.getMessage());
                    System.out.println("Unijeli ste slovo! Pokušajte ponovo.");
                    sc.nextLine();
                }
            }


             if(tempCapacity>500)
             {
                 throw new InvalidHallCapacity("Unijeli ste preveliki kapacitet za dvoranu!");
             }
             else if(tempCapacity<=0)
             {
                 throw new InvalidHallCapacity("Vrijednost kapaciteta dvorena ne može biti negativan!");
             }
             uspjeh=true;
        }
        sc.nextLine();
        System.out.print("Unesi sport za koji je namijenjena dvorana: ");
        String tempSport = sc.nextLine();
        log.trace("Izlazak iz metode generateHall");
        return new Hall(tempName,tempDoorNumber,tempCapacity,tempSport);

    }
    /**
     * Pretraga niza tipa "{@link Hall}" ovisno o parametrima koje korisnik unese.
     *
     *  Od korisnika se traži odabir jednog od dva parametra "Najveći/Najmanji", u metodi se ovisno o odabiru
     *  radi jedno od dvije mogućnosti, odnosno traži "Najmanju/Najveću" dvoranu po kapacitetu unutar niza
     *  "halls".
     *
     *  @param halls niz tipa "Hall" sadrži popis dvorana unesenih pomocu metodu "generateHalls"
     * @param sc Scanner se šalje kroz parametar a definiran je napočetku u metodi main.
     */
    private static void searchHalls(Hall[] halls,Scanner sc)
    {
        log.trace("Ulazak u metodu searchHalls");
        log.info("Preražujemo dvorane.");
        System.out.println("Pronađi dvoranu najvećeg/najmanjeg kapaciteta nekog proizvoda: ");
        System.out.println("1. najveći");
        System.out.println("2. najmanji");
        System.out.print("Odabir >> ");
        Integer choice = sc.nextInt();
        sc.nextLine();
        Integer max = -1;
        Integer min = 100;
        int index=0;
        boolean uspjeh = false;
        while(!uspjeh)
        {
            switch (choice)
            {

                case 1 -> {
                    for(int i=0;i<halls.length;i++)
                    {
                        if(max<halls[i].getCapacity())
                        {
                            max = halls[i].getCapacity();
                            index = i;
                        }
                    }
                    System.out.println("Najveći kapacitet ima dvorana: " + halls[index].getName() + ",a u nju stane " + halls[index].getCapacity() + " ljudi." );
                    uspjeh=true;
                }
                case 2 -> {
                    for(int i=0;i<halls.length;i++)
                    {
                        if(min>halls[i].getCapacity())
                        {
                            min =  halls[i].getCapacity();
                            index = i;
                        }
                    }
                    System.out.println("Najmanji kapacitet ima dvorana: " + halls[index].getName() + ",a u nju stane " + halls[index].getCapacity() + " ljudi." );
                    uspjeh=true;
                }
                default -> System.out.println("Krivi unos odabira!");
            };
        }
        log.trace("Izlazak iz metode searchHalls");

    }

}

