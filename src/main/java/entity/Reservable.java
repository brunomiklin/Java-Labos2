package entity;

import java.time.LocalDateTime;
/**
 * Predstavlja sučelje koje definira mogućnost provjere dostupnosti
 * objekta za određeni termin rezervacije.
 *
 * Implementacije ovog sučelja omogućuju provjeru je li resurs
 * (npr. dvorana, prostor ili uređaj) slobodan u zadanom vremenu
 * i za određeno trajanje.
 *
 * Metodom {@link #isAvailable(LocalDateTime, Integer)} može se provjeriti
 * raspoloživost prije dodavanja nove rezervacije.
 */
public interface Reservable {


    /**
     * Provjerava je li objekt dostupan u zadanom vremenu i trajanju.
     *
     * @param time početno vrijeme rezervacije
     * @param duration trajanje rezervacije u minutama
     * @return true ako je objekt slobodan, inače false
     */
     boolean isAvailable(LocalDateTime time,Integer duration);

}
