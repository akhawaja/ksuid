import com.amirkhawaja.Ksuid;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class KsuidTests {

    private static final int MAX_KSUID_LENGTH = 27;
    private Ksuid ksuid;

    @Before
    public void setup() {
        ksuid = new Ksuid();
    }

    @Test
    public void testSingleKsuidWorksCorrectly() throws IOException {
        final String uid = ksuid.generate();
        System.out.println(uid);
        assertEquals(MAX_KSUID_LENGTH, uid.length());
        System.out.println(ksuid.parse(uid));
    }

    @Test
    public void testMultipleKsuidWillBeCorrectLength() throws IOException {
        for (int i = 0; i < 50; i++) {
            final String s = ksuid.generate();
            assertEquals(MAX_KSUID_LENGTH, s.length());
        }
    }

    @Test
    public void testToEnsureWeDoNotHaveDuplicateKsuidValues() throws IOException {
        final int max = 10000;
        final ArrayList<String> bucket = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            final String uid = ksuid.generate();
            assertEquals(false, bucket.contains(uid));
            bucket.add(uid);
        }

        assertEquals(max, bucket.size());
    }

}
