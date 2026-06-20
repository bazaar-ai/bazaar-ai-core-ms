package az.bazaar_ai.core_ms.util.generators;

import java.security.SecureRandom;

public final class OTPGenerator {

    private static final int MIN = 100_000;
    private static final int MAX = 999_999;

    private static final SecureRandom RANDOM = new SecureRandom();

    private OTPGenerator() {
    }

    public static String generateSixDigitOTP() {
        return String.valueOf(RANDOM.nextInt(MIN, MAX + 1));
    }
}
