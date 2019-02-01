package com.amirkhawaja;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

public class Ksuid {

    private static final int EPOCH = 1400000000;
    private static final int TIMESTAMP_LENGTH = 4;
    private static final int PAYLOAD_LENGTH = 16;
    private static final int MAX_ENCODED_LENGTH = 27;

    private static SecureRandom random;

    public Ksuid() {
        random = new SecureRandom();
    }

    /**
     * Generate a new KSUID.
     *
     * @throws IOException Thrown when unable to generate a KSUID
     * @return New KSUID value.
     */
    public String generate() throws IOException {
        final KsuidGenerator uid = new KsuidGenerator(random);
        return uid.nextId();
    }

    public String parse(String ksuid) {
        final KsuidParser parser = new KsuidParser();
        return parser.parse(ksuid);
    }

    class KsuidGenerator {

        private final SecureRandom random;

        KsuidGenerator(SecureRandom random) {
            this.random = random;
        }

        private byte[] makeTimestamp() {
            final long utc = new DateTime(DateTimeZone.UTC).getMillis() / 1000;
            final int timestamp = (int) (utc - EPOCH);

            return ByteBuffer.allocate(TIMESTAMP_LENGTH).putInt(timestamp).array();
        }

        private byte[] makePayload() {
            final byte[] bytes = new byte[PAYLOAD_LENGTH];
            random.nextBytes(bytes);

            return bytes;
        }


        String nextId() throws IOException, RuntimeException {
            final byte[] timestamp = makeTimestamp();
            final byte[] payload = makePayload();

            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(timestamp);
            output.write(payload);

            final String uid = Base62.encode(output.toByteArray());

            if (uid.length() > MAX_ENCODED_LENGTH) {
                return uid.substring(0, MAX_ENCODED_LENGTH);
            }

            return uid;
        }

    }

    class KsuidParser {

        private long decodeTimestamp(final byte[] decodedKsuid) {
            final byte[] timestamp = new byte[TIMESTAMP_LENGTH];
            System.arraycopy(decodedKsuid, 0, timestamp, 0, TIMESTAMP_LENGTH);

            return ByteBuffer.wrap(timestamp).getInt() + EPOCH;
        }

        private String decodePayload(final byte[] decodedKsuid) {
            final byte[] payload = new byte[PAYLOAD_LENGTH];

            System.arraycopy(decodedKsuid, TIMESTAMP_LENGTH,
                payload, 0, decodedKsuid.length - TIMESTAMP_LENGTH);

            return Arrays.toString(payload);
        }

        /**
         * Parse a Ksuid and display its component parts.
         *
         * @param ksuid The Ksuid to parse.
         * @return The component parts of the Ksuid.
         */
        String parse(String ksuid) {
            final byte[] bytes = Base62.decode(ksuid.toCharArray());

            final long timestamp = decodeTimestamp(bytes);
            final String payload = decodePayload(bytes);
            final DateTime utc = new DateTime(timestamp * 1000, DateTimeZone.UTC);

            return String.format("Time: %s\nTimestamp: %d\nPayload: %s", utc, timestamp, payload);
        }

    }

}
