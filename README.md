Read.ME

Opis:
Ova aplikacija omogućuje kreiranje korisnika (User) i trenera (Coach), upravljanje dvoranama (Hall) te rezervaciju termina (Booking) za treninge. Program omogućuje:
* Kreiranje korisnika i trenera s opcionalnim podacima (email, telefon, specijalizacija, username, password).
* Dodavanje dvorana s informacijama o kapacitetu i sportu.
* Treneri mogu kreirati booking termine u odabranim dvoranama.
* Korisnici se mogu pridružiti booking terminima trenera.
* Pretraživanje korisnika po imenu i prezimenu.
* Pronalazak dvorane s najvećim ili najmanjim kapacitetom.
Struktura programa
* Main – glavna klasa koja upravlja programom i interakcijom s korisnikom.
* entity paket – sadrži klase:
    * Person – bazna klasa za korisnike i trenere.
    * User – korisnik aplikacije, može se pridružiti booking terminima.
    * Coach – trener, može kreirati booking termine.
    * Hall – dvorana u kojoj se održavaju treninzi.
    * Booking – rezervacija termina.
Pokretanje programa
1. Pokreni Main klasu.
2. Program traži unos korisnika (User ili Coach) i njihovih podataka.
3. Nakon kreiranja korisnika, unosi se 5 dvorana s pripadajućim podacima.
4. Treneri kreiraju booking termine:
    * Odabere se trener.
    * Odabere se dvorana.
    * Unosi se datum i vrijeme termina (dd.MM.yy HH:mm).
    * Unosi se trajanje treninga u minutama.
    * Ponavlja se sve dok se ne unesu svi termini.
5. Korisnici se pridružuju booking terminima:
    * Odabere se korisnik.
    * Odabere se trener.
    * Odabere se booking termin.
    * Ponavlja se dok korisnik ne odluči prestati.
6. Pretraživanje:
    * Po imenu ili prezimenu korisnika/trenera.
    * Pretraga dvorana po najvećem ili najmanjem kapacitetu.
**Napomene**
* Broj korisnika je ograničen na 10.
* Broj dvorana je ograničen na 5.
* Datum i vrijeme termina moraju biti u formatu dd.MM.yy HH:mm.
* Unos teksta (da/ne) nije osjetljiv na velika/mala slova.
