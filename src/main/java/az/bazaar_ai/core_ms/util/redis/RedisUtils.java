package az.bazaar_ai.core_ms.util.redis;

public final class RedisUtils {

    private RedisUtils() {
    }

    public static final String REGISTER_PREFIX = "auth:register:email:";

    public static String registerKey(String email) {
        return REGISTER_PREFIX + email;
    }
}
