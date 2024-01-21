package ru.org.sevn.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.servlet.http.*;

public class CookiesUtil {
    public static Cookie [] getCookies (final HttpServletRequest request) {
        var res = request.getCookies ();
        return (res == null) ? new Cookie [0] : res;
    }

    public static Optional<Cookie> findFirst (final HttpServletRequest request, final String name) {
        return Stream.of (getCookies (request)).filter (c -> name.equals (c.getName ())).findFirst ();
    }

    public static List<Cookie> find (final HttpServletRequest request, final String name) {
        return Stream.of (getCookies (request)).filter (c -> name.equals (c.getName ())).collect (Collectors.toList ());
    }

    public static String getValue (final HttpServletRequest request, final String name) {
        var oc = Stream.of (getCookies (request)).filter (c -> name.equals (c.getName ())).findFirst ();
        if (oc.isPresent ()) {
            return oc.get ().getValue ();
        }
        else {
            return null;
        }
    }

    public static Boolean getBooleanValue (final HttpServletRequest request, final String name) {
        var sval = getValue (request, name);
        if (sval != null) {
            try {
                return Boolean.valueOf (sval);
            }
            catch (final Exception ex) {
                //nothing
            }
        }
        return false;
    }

}
