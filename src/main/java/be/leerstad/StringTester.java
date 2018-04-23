package be.leerstad;

/**
 *
 * @author Hiel Jorn
 * @version 1.0, jan 2018
 * @since 1.0
 *
 */

public final class StringTester {

    /**
     *
     * @param string
     * @param lengte
     * @return boolean
     * @throws IllegalArgumentException
     */
    public final static Boolean opLengte(String string , Integer lengte) throws IllegalArgumentException
{
    if(string.length()<lengte) {throw new IllegalArgumentException();}

    return true;
}
}
