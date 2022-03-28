package upn.proj.sowad.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "No se puede verificar el Token";
    public static final String APRENDO_EN_WEB = "Aprendo en Web";
    public static final String APRENDO_EN_WEB_ADMINISTRATION = "Login Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Usted debe iniciar sesión para acceder a esta página";
    public static final String ACCESS_DENIED_MESSAGE = "Usted no tiene permiso para acceder a esta página";
    public static final String OPTIONS_HTTP_METHOD = "OPCIONES";
    //public static final String[] PUBLIC_URLS = { "/user/login", "/user/register", "/user/image/**","/tema/image/**" };
    public static final String[] PUBLIC_URLS = { "**" };
}
