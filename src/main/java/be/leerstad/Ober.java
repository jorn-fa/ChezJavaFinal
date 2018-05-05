package be.leerstad;

import org.apache.log4j.Logger;

import java.util.Comparator;

/**
 *
 * @author Hiel Jorn
 * @version 1.0, jan 2018
 * @since 1.0
 *
 */

public final class Ober implements Comparable<Ober> , java.io.Serializable{


    private int oberID;
    private final String naam;
    private final String voornaam;
    private static Logger logger = Logger.getLogger(Ober.class.getName());

    public Ober(){this(0,null,null);}

    public Ober(int Id, String naam, String voornaaam) {
        this.oberID =Id;
        this.naam=naam;
        this.voornaam=voornaaam;
        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());
    }

    public Ober(int Id, String naam, String voornaaam, String password) {
        this.oberID =Id;
        this.naam=naam;
        this.voornaam=voornaaam;
        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());
    }


    /**
    @return name of waiter
     */
    public String getNaam() {
        return naam;
    }

    /**

     * @return firstname of waiter
     */
    public String getVoornaam() {
        return voornaam;
    }


    /**
     *
     * @return Id of waiter
     */
    public int getID() {
        return oberID;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ober ober = (Ober) o;

        return oberID == ober.oberID && naam.equals(ober.naam) && voornaam.equals(ober.voornaam);
    }

    @Override
    public int hashCode() {
        int result = oberID;
        result = 31 * result + naam.hashCode();
        result = 31 * result + voornaam.hashCode();
        return result;
    }

    public int compareTo(Ober ober) {
        return Comparator.comparing(Ober::getNaam)
                .thenComparing(Ober::getVoornaam)
                .compare(this,ober);
    }

    @Override
    public String toString() {
        return ("Ober{" + "naam='" + naam + '\'' + ", voornaam='" + voornaam + "\'}");
    }


}
