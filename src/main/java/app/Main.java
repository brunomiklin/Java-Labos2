package app;
import entity.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static final Integer NUMBER_OF_OBJECTS = 5;
    public static final Integer NUMBER_OF_USERS = 10;

    static void main() {
        Scanner sc = new Scanner(System.in);
        Person[] osobe = new Person[NUMBER_OF_USERS];
        Hall[] dvorane = new Hall[NUMBER_OF_OBJECTS];

        for (Integer i = 0; i < NUMBER_OF_USERS; i++) {

            System.out.print("Odaberi tip korisnika (1. User, 2. Trener): ");
            Integer choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> osobe[i] = generateUser(sc);
                case 2 -> osobe[i] = generateCoach(sc);
                default -> {
                    System.out.println("Nepoznat izbor, unosimo User po defaultu.");
                    osobe[i] = generateUser(sc);
                }
            }

        }
        System.out.println();
        System.out.println();
        for (Integer i = 0; i < NUMBER_OF_OBJECTS; i++) {
            dvorane[i] = generateHall(sc);
        }
        String choice;
        do {
            System.out.println();
            System.out.println();
            System.out.println("Odaberi trenera:");
            Integer brojac = 0;
            Coach[] choiceCoaches = new Coach[NUMBER_OF_USERS];
            for (Integer i = 0; i < osobe.length; i++) {
                if (osobe[i] instanceof Coach trener) {
                    choiceCoaches[brojac] = trener;
                    System.out.println((brojac + 1) + ". " + trener.getFirstName() + " " + trener.getLastName());
                    brojac++;
                }

            }

            System.out.print("Odabir >>");
            Integer cohiceCoach = sc.nextInt();
            sc.nextLine();
            do {
                System.out.println("Odberi dvoranu:");
                for (Integer i = 0; i < dvorane.length; i++) {
                    System.out.println((i + 1) + ". " + dvorane[i].toString());

                }
                System.out.print("Odabir >> ");
                Integer choiceHall = sc.nextInt();
                sc.nextLine();

                System.out.print("Unesi datum i vrijeme (dd.MM.yy HH:mm): ");
                String datumString = sc.nextLine();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
                LocalDateTime datum = LocalDateTime.parse(datumString, fmt);
                System.out.print("Unesi duljinu trajanja treninga (u minutama): ");
                Integer trainingTime = sc.nextInt();
                sc.nextLine();
                choiceCoaches[cohiceCoach - 1].createBooking(dvorane[choiceHall - 1], datum, trainingTime * 60);

                System.out.println("Želite li dodati još bookinga za trenra " + choiceCoaches[cohiceCoach - 1].getFirstName() + " (da/ne)?");
                choice = sc.nextLine();
            } while ("DA".equalsIgnoreCase(choice));


            System.out.println("Želite li nastaviti s kreiranjem bookinga (da/ne)?");
            choice = sc.nextLine();
        } while ("DA".equalsIgnoreCase(choice));
        String choiceUserContinue;
        do
        {
            System.out.println("Odaberi korisnika:");
            User[] choiceUsers = new User[NUMBER_OF_USERS];
            int userCount = 0;
            for (int i = 0; i < osobe.length; i++) {
                if (osobe[i] instanceof User korisnik) {
                    choiceUsers[userCount] = korisnik;
                    System.out.println((userCount + 1) + ". " + korisnik.getFirstName() + " " + korisnik.getLastName());
                    userCount++;
                }
            }
            System.out.print("Odabir >> ");
            Integer chosenUserIndex = sc.nextInt() - 1;
            sc.nextLine();
            User chosenUser = choiceUsers[chosenUserIndex];


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
            System.out.print("Odabir >> ");
            Integer chosenCoachIndex = sc.nextInt() - 1;
            sc.nextLine();
            Coach chosenCoach = choiceCoaches[chosenCoachIndex];


            System.out.println("Odaberi booking:");
            for (Integer i = 0; i < chosenCoach.getMyBookings().length; i++) {
                if (chosenCoach.getMyBookings()[i] != null) {
                    System.out.println((i + 1) + ". " + chosenCoach.getMyBookings()[i]);
                }
            }
            System.out.print("Odabir >> ");
            Integer chosenBookingIndex = sc.nextInt() - 1;
            sc.nextLine();

            Booking chosenBooking = chosenCoach.getMyBookings()[chosenBookingIndex];
            chosenUser.joinBooking(chosenBooking);



            System.out.println("Želiš li nastaviti dalje(da/ne)?");
            choiceUserContinue = sc.nextLine();

        }while("DA".equalsIgnoreCase(choiceUserContinue));


        searchUsers(osobe,sc);
        searchHalls(dvorane,sc);
    }

    private static void searchUsers(Person[] people,Scanner sc) {

        System.out.println("Pretraži korisnika po imenu/prezimenu: ");
        System.out.println("1. ime");
        System.out.println("2. prezime");
        System.out.print("Odabir >> ");
        Integer choice = sc.nextInt();
        sc.nextLine();
        String input;
        switch (choice) {
            case 1 -> System.out.print("Unesi ime:");
            case 2 -> System.out.print("Unesi prezime:");
        }
        ;
        input = sc.nextLine();

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
                        System.out.println("Pronađen korisnik" + coach.getFirstName() + " " + coach.getLastName() + ".");
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
    }

    public static Person generateCoach(Scanner sc) {


        String tempSpecialization = "";
        String tempPhoneNumber = "";
        String tempEmail = "";

        System.out.print("Unesi OIB: ");
        String tempOIB = sc.nextLine();
        System.out.print("Unesi ime: ");
        String tempFirstName = sc.nextLine();
        System.out.print("Unesi prezime: ");
        String tempLastName = sc.nextLine();
        String choice;
        System.out.println("Želiš li dodati email? (da/ne)");
        choice = sc.nextLine();
        if ("DA".equalsIgnoreCase(choice)) {

            System.out.print("Unesi mail:");
            tempEmail = sc.nextLine();
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

            System.out.print("Unesi telefon: ");
            tempSpecialization = sc.nextLine();
        }

        Person tempCoach = new Coach.CoachBuilder(tempOIB,tempFirstName,tempLastName)
                .specialization(tempSpecialization.isEmpty()?"":tempSpecialization)
                .email(tempEmail.isEmpty()?"":tempEmail)
                .phoneNumber(tempPhoneNumber.isEmpty()?"":tempPhoneNumber)
                .build(); //nebu radilo ako nije gore Person tip


            return tempCoach;
    }

    public static Person generateUser(Scanner sc)
    {
        System.out.print("Unesi OIB: ");
        String tempOIB = sc.nextLine();
        System.out.print("Unesi ime: ");
        String tempFirstName = sc.nextLine();
        System.out.print("Unesi prezime: ");
        String tempLastName = sc.nextLine();

        String tempEmail="";
        String tempPhoneNumber="";
        String tempUsername="";
        String tempPassword="";
        System.out.println("Želiš li dodati email? (da/ne)");
        String choice =  sc.nextLine();
        if("DA".equalsIgnoreCase(choice))
        {
            System.out.print("Unesi mail:");
            tempEmail = sc.nextLine();
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

        return tempUser;

    }

    public static Hall generateHall(Scanner sc)
    {
        System.out.print("Unesi ime dvorane: ");
        String tempName = sc.nextLine();
        System.out.println("Unesi broj vrata:");
        String tempDoorNumber = sc.nextLine();
        System.out.println("Unesi kapactitet dvorane:");
        Integer tempCapacity = sc.nextInt();
        sc.nextLine();
        System.out.print("Unesi sport za koji je namijenjena dvorana: ");
        String tempSport = sc.nextLine();
        return new Hall(tempName,tempDoorNumber,tempCapacity,tempSport);

    }

    private static void searchHalls(Hall[] halls,Scanner sc)
    {
        System.out.println("Pronađi dvoranu najvećeg/najmanjeg kapaciteta nekog proizvoda: ");
        System.out.println("1. najveći");
        System.out.println("2. najmanji");
        System.out.print("Odabir >> ");
        Integer choice = sc.nextInt();
        sc.nextLine();
        Integer max = -1;
        Integer min = 100;
        int index=0;
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
            }
            default -> System.out.println("Krivi unos odabira!");
        };

    }
}

