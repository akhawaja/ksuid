import com.amirkhawaja.Base62;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

public class Base62Tests {

    private static final int BYTE_LENGTH = 20;
    private static final int MAX_STRING_LENGTH = 27;

    private SecureRandom random;

    @Before
    public void setup() {
        random = new SecureRandom();
    }

    @Test
    public void testEncodedStringIsDecodedCorrectly() {
        final byte[] bytes = new byte[BYTE_LENGTH];
        random.nextBytes(bytes);

        final String s = Base62.encode(bytes);
        final byte[] decode = Base62.decode(s.toCharArray());
        final String encode = Base62.encode(decode);

        assertEquals(0, encode.compareTo(s));
    }

}
