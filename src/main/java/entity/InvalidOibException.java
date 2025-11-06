package entity;

/**
 * Predstavlja označenu iznimku koja se baca kada je uneseni OIB neispravan.
 *
 * Ova se iznimka javlja kada operacija koja uključuje OIB otkrije
 * neispravan ili neprihvatljiv oblik OIB-a, primjerice zbog pogrešnog
 * broja znamenki ili nevaljane kontrolne znamenke.
 *
 * Nasljeđuje klasu {@link Exception}, što omogućuje korištenje standardnih
 * mehanizama za rukovanje označenim iznimkama (checked exceptions).
 *

 */
public class InvalidOibException extends Exception{

    /**
     * Stvara novu iznimku InvalidOibException s navedenom porukom.
     *
     * @param message detaljna poruka koja objašnjava razlog iznimke
     */
    public InvalidOibException(String message)
    {
        super(message);
    }
}
