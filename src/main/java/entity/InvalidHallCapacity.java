package entity;

/**
 * Predstavlja označenu iznimku koja se baca kada se unese neispravan
 * ili neprihvatljiv kapacitet dvorane.
 *
 * Ova se iznimka javlja ako kapacitet dvorane prelazi dopušteni
 * maksimum (500) ili je na neki drugi način nevažeći, poput negativne vrijednosti.
 *
 * Nasljeđuje klasu {@link Exception}, što omogućuje korištenje standardnih
 * mehanizama za rukovanje označenim iznimkama (checked exceptions).
*/

public class InvalidHallCapacity extends Exception{
    /**
     * Stvara novu iznimku InvalidHallCapacity s navedenom porukom.
     *
     * @param poruka detaljna poruka koja objašnjava razlog iznimke
     */
    public InvalidHallCapacity(String poruka)
    {
        super(poruka);
    }
}
