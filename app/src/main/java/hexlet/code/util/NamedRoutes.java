package hexlet.code.util;

public final class NamedRoutes {
    private NamedRoutes() {
    }

    public static String homePath() {
        return "/";
    }
    
    public static String urlsPath() {
        return "/urls";
    }

    public static String urlsPath(Integer id) {
        return urlsPath(String.valueOf(id));
    }

    public static String urlsPath(String id) {
        return "/urls/".concat(id);
    }
}
