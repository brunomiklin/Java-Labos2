package app;
import entity.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

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
    public static final Integer NUMBER_OF_USERS = 5;

    /**
     * Pozivanje različitih metoda te generianje i manipulacija korisnicima, bookingom i dvoranama
     *
     * Glavna metoda u aplikaciji omogućuje generiranje trenera i korisnika, koje kasnije koristi
     * kao izvor kod kreiranja bookinga, pretrage i u konačnici pružanje usluge istima
     */
    static void main() {
        log.trace("Ulaz u metodu main.");
        Scanner sc = new Scanner(System.in);
        Set<Person> osobe = new HashSet<>();
        List<Hall> dvorane = new ArrayList<>();

        for (Integer i = 0; i < NUMBER_OF_USERS; i++) {
            log.trace("Iteracija kroz for-petlju (NUMBER_OF_USERS)");
            while (true)
            {

                try {
                    System.out.print("Odaberi tip korisnika (1. User, 2. Trener): ");
                    Integer choice = Integer.parseInt(sc.nextLine());
                     switch (choice) {
                        case 1 -> osobe.add(generateUser(sc));
                        case 2 -> osobe.add(generateCoach(sc));
                        default -> {
                            System.out.println("Nepoznat izbor, unosimo User po defaultu.");
                             osobe.add(generateUser(sc));

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
                    dvorane.add(generateHall(sc));
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




        log.debug("Pozivam metodu searchUsers pomocu ovih parametara [List<Hall> " + osobe.getClass() + " [Scanner " + sc.getClass() + "]");
        searchHalls(dvorane, sc);

        log.debug("Pozivam metodu groupPeople pomocu ovih parametara [List<Osobe> " + osobe.getClass() + " [Scanner " + sc.getClass() + "]");
        groupPeople(osobe, sc);

        log.debug("Pozivam metodu partitiongByEmail pomocu ovih parametara [List<Osobe> " + osobe.getClass() + "]");
        partitioningByEmail(osobe);


        List<List<Coach>> top3Coaches = osobe.stream()
                .filter(p->p instanceof Coach)
                .map(p->(Coach)p)
                .sorted((c1, c2) -> Integer.compare(c2.getMyBookings().length, c1.getMyBookings().length))
                .gather(Gatherers.windowFixed(3))
                .toList();



        System.out.println("Top 3 trenera po broju zakazanih termina:");
        for (List<Coach> window : top3Coaches) {
            for (Coach coach : window) {
                System.out.println(coach.getFirstName() + " " + coach.getLastName()
                        + " - broj termina: " + coach.getMyBookings().length);
            }
            System.out.println("---"); // razdvoji "prozor" radi čitljivosti
        }

        log.trace("Izlaz iz metode main.");
    }



    /**
     * Particiranje osoba po emailu (ako korisnika ima ili nema email).
     *
     *Ispisuje korisnike za koje smo unijeli mail i one koje nismo.
     */
    public static void partitioningByEmail(Set<Person> osobe)
    {
        Map<Boolean,List<Person>> resultat = osobe.stream().collect(Collectors.partitioningBy(p->p.getEmail()!=null && p.getEmail().isEmpty()));

        System.out.println("Osobe s mailom:");
        resultat.get(true).forEach(p-> System.out.println(p.getEmail() + " - " + p.getFirstName() + " " + p.getLastName()));

        System.out.println("Osobe bez maila:");
        resultat.get(false).forEach(p-> System.out.println(p.getFirstName() + " " + p.getLastName()));
    }


    /**
     * Grupiranje osoba po tipu (User ili Coach) i ispis ovisno o korisničkom izboru.
     *
     * @param osobe skup osoba koje sadrže instance klasa {@link User} i {@link Coach}
     * @param sc Scanner za unos korisničkog izbora
     */
    private static void groupPeople(Set<Person> osobe, Scanner sc) {
        log.trace("Ulazak u metodu groupPeople");
        log.info("Grupiramo osobe po tipu (User/Coach).");

        Map<String, List<Person>> grouped = osobe.stream()
                .collect(Collectors.groupingBy(p -> {
                    if (p instanceof Coach) {
                        return "Coach";
                    } else {
                        return "User";
                    }
                }));

        System.out.println("Želite li vidjeti popis korisnika ili trenera?");
        System.out.println("1. Korisnici");
        System.out.println("2. Treneri");
        System.out.print("Odabir >> ");

        Integer choice = 0;
        while (choice != 1 && choice != 2) {
            try {
                choice = sc.nextInt();
                sc.nextLine();
                if (choice != 1 && choice != 2) {
                    System.out.println("Nevažeći unos! Pokušajte ponovo.");
                }
            } catch (InputMismatchException ime) {
                System.out.println("Unijeli ste slovo umjesto broja! Pokušajte ponovo.");
                log.error(ime.getMessage());
                sc.nextLine();
            }
        }

        String key = (choice == 1) ? "User" : "Coach";
        List<Person> selectedGroup = grouped.getOrDefault(key, new ArrayList<>());

        selectedGroup.stream().sorted(Comparator.comparing(Person::getFirstName)); //sortiranje po imenu

        if (selectedGroup.isEmpty()) {
            System.out.println("Nema pronađenih osoba za odabrani tip!");
        } else {
            System.out.println("\n--- Popis " + (choice == 1 ? "korisnika" : "trenera") + " ---");
            selectedGroup.sort(Comparator.comparing(Person::getLastName)); // opcionalno sortiranje
            for (Person p : selectedGroup) {
                if (p instanceof User user) {
                    System.out.println("User: " + user.getFirstName() + " " + user.getLastName());
                } else if (p instanceof Coach coach) {
                    System.out.println("Coach: " + coach.getFirstName() + " " + coach.getLastName());
                }
            }
        }


        log.trace("Izlazak iz metode groupPeople");
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
    private static void createBooking(Set<Person> osobe, Scanner sc, List<Hall> dvorane) {
        log.trace("Ulaz u metodu createBooking.");
        log.info("Kreiramo booking.");
        String choice="";
        do {
            log.info("Odabir trenera.");
            System.out.println("\nOdaberi trenera:");

            Integer choiceHall,cohiceCoach;

            List<Coach> choiceCoaches = osobe.stream()
                    .filter(p->p instanceof Coach)
                    .map(p->(Coach) p)
                    .collect(Collectors.toList()); //vraća modified listu


            choiceCoaches.sort(Comparator.comparing(Coach::getLastName).reversed()); //sortiranje trenera po prezimenu
            for(Integer i=0;i<choiceCoaches.size();i++)
            {
                Coach tempCoach = choiceCoaches.get(i);
                System.out.println((i+1) + "." + tempCoach.getFirstName() + " " + tempCoach.getLastName());
            }



            try
            {
                System.out.print("Odabir >>");
                cohiceCoach = sc.nextInt();
                sc.nextLine();
                if (cohiceCoach < 1 || cohiceCoach > choiceCoaches.size()) {
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

            dvorane.sort(Comparator.comparingInt(Hall::getCapacity).reversed()); // prikaz dvorana od veće prema manjoj
            do {
                log.info("Odabir dvorana.");
                System.out.println("Odberi dvoranu:");
                for (Integer i = 0; i < dvorane.size(); i++) {
                    System.out.println((i + 1) + ". " + dvorane.get(i).toString());

                }
                try {
                    System.out.print("Odabir >> ");
                    choiceHall = sc.nextInt();
                    sc.nextLine();
                    if (choiceHall < 1 || choiceHall > dvorane.size()) {
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
                        choiceCoaches.get(cohiceCoach-1).createBooking(dvorane.get(choiceHall - 1), datum, trainingTime * 60);
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



                System.out.println("Želite li dodati još bookinga za trenra " + choiceCoaches.get(cohiceCoach - 1).getFirstName() + " (da/ne)?");
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
    private static void joinBooking(Set<Person> osobe, Scanner sc) {
        log.trace("Ulazak u metodu joinBooking");
        log.info("Pridužujemo booking korisniku.");
        String choiceUserContinue="";
        User chosenUser;
        Coach chosenCoach;
        do
        {
            log.info("Odabir korinsika.");
            System.out.println("Odaberi korisnika:");

            Integer userCount = 0;

            List<User> choiceUsers = osobe.stream().filter(p->p instanceof User)
                    .map(u->(User) u)
                    .collect(Collectors.toList());

            for (Integer i = 0; i < choiceUsers.size(); i++) {

                User tempUser = choiceUsers.get(i);
                System.out.println((i+1)+ ". " + tempUser.getOIB() + " " + tempUser.getFirstName() + " " + tempUser.getLastName());

            }

            try
            {
                System.out.print("Odabir >> ");
                Integer chosenUserIndex = sc.nextInt() - 1;
                sc.nextLine();
                 chosenUser = choiceUsers.get(chosenUserIndex);
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
            List<Coach> choiceCoaches = osobe.stream().filter(p->p instanceof Coach).
                    map(p-> (Coach) p).collect(Collectors.toList());

            for (Integer i = 0; i < choiceCoaches.size(); i++) {
                Coach tempCoach = choiceCoaches.get(i);
                System.out.println((i+1)+". " + tempCoach.getOIB() + " " + tempCoach.getFirstName() + " " + tempCoach.getLastName());

            }
            try
            {
                System.out.print("Odabir >> ");
                Integer chosenCoachIndex = sc.nextInt() - 1;
                sc.nextLine();
                chosenCoach = choiceCoaches.get(chosenCoachIndex);
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
     * @param osobe sadrži instance dviju različitih klasa ({@link User}/{@link Coach}), imajući to na umu ispisat će se različita poruka
     * @param sc - Scanner se šalje kroz parametar a definiran je napočetku u metodi main
     */
    private static void searchUsers(Set<Person> osobe,Scanner sc) {
        log.trace("Ulazak u metodu searchUsers.");
        log.info("Preražujemo korisnike.");

        System.out.println("Pretraži korisnika po imenu/prezimenu: ");
        System.out.println("1. ime");
        System.out.println("2. prezime");
        System.out.print("Odabir >> ");
        Integer choice = 0;

        while (choice != 1 && choice != 2) { {
            try {
                System.out.print("Odabir >> ");
                choice = sc.nextInt();
                sc.nextLine();
                if (choice != 1 && choice != 2) {
                    System.out.println("Krivo ste unjeli odabir!");
                    choice = 0;

                }
            } catch (InputMismatchException ime) {
                log.error(ime.getMessage());
                System.out.println("Unijeli ste slovo umjesto broja! Pokušajte ponovo.");
                sc.nextLine();
            }
        }


        switch (choice) {
            case 1 -> System.out.print("Unesi ime:");
            case 2 -> System.out.print("Unesi prezime:");
        }
        String input=sc.nextLine();


            final Integer finalChoice = choice;

            List<Person> results = osobe.stream()
                    .filter(p -> (finalChoice == 1 && p.getFirstName().equalsIgnoreCase(input)) ||
                            (finalChoice == 2 && p.getLastName().equalsIgnoreCase(input)))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                System.out.println("Nije pronađen korisnik!");
            } else {
                for (Person p : results) {
                    if (p instanceof User user) {
                        System.out.println("Pronađen korisnik: " + user.getFirstName() + " " + user.getLastName());
                        user.allMyBookings();
                    } else if (p instanceof Coach coach) {
                        System.out.println("Pronađen trener: " + coach.getFirstName() + " " + coach.getLastName());
                        coach.printMyBookings();
                    }
                    System.out.println("---------------");
                }
            }

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
        log.trace("Ulazak u metodu unesiSport");

        SportType tempSport = choseSport(sc);

        log.trace("Izlazak iz metode generateHall");
        return new Hall(tempName,tempDoorNumber,tempCapacity,tempSport);

    }


    /**
     * Omogućuje korisniku da odabere sport iz definirane liste {@link SportType} enum vrijednosti.
     *
     * Metoda ispisuje sve moguće sportove prema njihovim vrijednostima u enum-u te traži unos
     * korisnika kao brojčani indeks. Unos se provjerava na ispravnost i ponavlja dok korisnik ne
     * unese valjanu vrijednost.
     *
     * @param sc Scanner koji se koristi za unos s konzole.
     * @return Odabrani sport kao vrijednost tipa {@link SportType}.
     */
    public static SportType choseSport(Scanner sc)
    {
        SportType[] sports = SportType.values();
        System.out.println("Odaberite sport za koji je namijenjena dvorana:");
        for (int i = 0; i < sports.length; i++) {
            System.out.println((i + 1) + ". " + sports[i]);
        }
        Integer choice = 0;
        while (choice < 1 || choice > sports.length) {
            try {
                System.out.print("Odabir (1-" + sports.length + "): ");
                choice = sc.nextInt();
                sc.nextLine();
                if (choice < 1 || choice > sports.length) {
                    System.out.println("Nevažeći unos, pokušajte ponovno.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Unijeli ste nevažeći unos, pokušajte ponovo.");
                sc.nextLine();
            }
        }
        return sports[choice - 1];
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
    private static void searchHalls(List<Hall> halls,Scanner sc)
    {
        log.trace("Ulazak u metodu searchHalls");
        log.info("Preražujemo dvorane.");
        System.out.println("Pronađi dvoranu najvećeg/najmanjeg kapaciteta nekog proizvoda: ");
        System.out.println("1. najveći");
        System.out.println("2. najmanji");
        System.out.print("Odabir >> ");
        Integer choice = 0;

        while (choice != 1 && choice != 2) {
            try {
                System.out.print("Odabir >> ");
                choice = sc.nextInt();
                sc.nextLine();
                if (choice != 1 && choice != 2) {
                    System.out.println("Krivi unos, pokušajte ponovno.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Unijeli ste nevažeći unos! Pokušajte ponovno.");
                sc.nextLine();
            }
        }

        Hall targetHall = new Hall();

        switch (choice)
        {
            case 1 -> targetHall = halls.stream().max(Comparator.comparingInt(Hall::getCapacity)).orElse(null);
            case 2 -> targetHall = halls.stream().min(Comparator.comparingInt(Hall::getCapacity)).orElse(null);
        }


        if(targetHall!=null)
        {
            if(choice==1)
            {
                System.out.println("Najveći kapacitet ima dvorana: " + targetHall.toString());
            }
            else
            {
                System.out.println("Najmanji kapacitet ima dvorana: " + targetHall.toString());
            }

        }
        else
        {
            System.out.println("Nije pronađena nijedna dvorana!");
        }


        log.trace("Izlazak iz metode searchHalls");

    }
    }









