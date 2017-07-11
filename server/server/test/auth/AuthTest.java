package auth;

import org.junit.jupiter.api.Test;

class AuthTest {

    @Test
    void test() {
        int auth = AuthHelper.ADMIN | AuthHelper.START_LOG;
        for (int aAuth : new int[] {AuthHelper.ADMIN, AuthHelper.START_LOG}) {
            if ((aAuth & auth) > 0) {
                System.out.println(aAuth);
            }
        }
    }

}
