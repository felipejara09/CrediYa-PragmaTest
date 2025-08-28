package co.com.bancolombia.api.helpers;

public class ErrorHelpers {

    private ErrorHelpers() {}

    public static boolean contains(String s, String token) {
        return s != null && token != null && s.contains(token);
    }
    public static boolean containsIgnoreCase(String s, String token) {
        return s != null && token != null && s.toLowerCase().contains(token.toLowerCase());
    }
    public static String deepMessage(Throwable t) {
        String last = null;
        while (t != null) { last = t.getMessage(); t = t.getCause(); }
        return last == null ? "" : last;
    }

}
