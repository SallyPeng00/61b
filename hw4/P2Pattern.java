import net.sf.saxon.style.XSLOutput;
import org.w3c.dom.ls.LSOutput;

/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /** Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "([1-9]|0[1-9]|1[1-2])/([1-9]|0[1-9]|[1-2][0-9]|3[0-1])/[2-9][0-9]{3}|19[0-9]{2}";


    /** Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "^\\(([0-9]{1,},\\s*)+[0-9]{1,}\\)$"; //FIXME: Add your regex here

    /** Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "(www|[a-z]{1,})[.]([a-z]{1,}-*[a-z]{1,}[.])*[a-z]{2,6}"; //FIXME: Add your regex here

    /** Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "[^0-9].*"; //FIXME: Add your regex here

    /** Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "(([0-9]|[0-9][0-9]|[0-1][0-9][0-9]|2[0-4][0-9]|25[0-5])[.]){3}([0-9]|[0-9][0-9]|[0-1][0-9][0-9]|2[0-4][0-9]|25[0-5])"; //FIXME: Add your regex here

}
