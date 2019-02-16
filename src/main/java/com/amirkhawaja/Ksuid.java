package com.amirkhawaja;

import io.seruco.encoding.base62.Base62;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
     * @return New KSUID value.
     * @throws IOException Thrown when unable to generate a KSUID
     */
    public String generate() throws IOException {
        final KsuidGenerator uid = new KsuidGenerator(random);
        return uid.nextId();
    }

    /**
     * Generate a new KSUID.
     *
     * @return New KSUID value.
     * @throws IOException Thrown when unable to generate a KSUID
     */
    public String generate(final int timestamp) throws IOException {
        final KsuidGenerator uid = new KsuidGenerator(random);
        final byte[] ts = ByteBuffer.allocate(TIMESTAMP_LENGTH).putInt(timestamp - EPOCH).array();
        return uid.nextId(ts);
    }

    public String print(String ksuid) {
        final KsuidParser parser = new KsuidParser();
        return parser.print(ksuid);
    }

    public KsuidComponents parse(String ksuid) {
        final KsuidParser parser = new KsuidParser();
        return parser.parse(ksuid);
    }

    class KsuidGenerator {

        private final SecureRandom random;

        KsuidGenerator(SecureRandom random) {
            this.random = random;
        }

        private byte[] makeTimestamp() {
            final long utc = ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli() / 1000;
            final int timestamp = (int) (utc - EPOCH);

            return ByteBuffer.allocate(TIMESTAMP_LENGTH).putInt(timestamp).array();
        }

        private byte[] makePayload() {
            final byte[] bytes = new byte[PAYLOAD_LENGTH];
            random.nextBytes(bytes);

            return bytes;
        }


        String nextId() throws IOException, RuntimeException {
            return nextId(makeTimestamp());
        }

        String nextId(final byte[] timestamp) throws IOException, RuntimeException {
            final byte[] payload = makePayload();
            final Base62 base62 = Base62.createInstance();

            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(timestamp);
            output.write(payload);

            final String uid = new String(base62.encode(output.toByteArray()));

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

        private byte[] decodePayload(final byte[] decodedKsuid) {
            final byte[] payload = new byte[PAYLOAD_LENGTH];

            System.arraycopy(decodedKsuid, TIMESTAMP_LENGTH,
                payload, 0, decodedKsuid.length - TIMESTAMP_LENGTH);

            return payload;
        }

        /**
         * Print a Ksuid and display its component parts.
         *
         * @param ksuid The Ksuid to parse.
         * @return The component parts of the Ksuid.
         */
        String print(String ksuid) {
            KsuidComponents ksuidComponents = parse(ksuid);
            final ZonedDateTime utc = Instant.ofEpochSecond(ksuidComponents.getTimestamp())
                .atZone(ZoneId.of("UTC"));

            return String.format("Time: %s\nTimestamp: %d\nPayload: %s",
                utc, ksuidComponents.getTimestamp(), Arrays.toString(ksuidComponents.getPayload()));
        }

        /**
         * Parse a Ksuid and display its component parts.
         *
         * @param ksuid The Ksuid to parse.
         * @return The component parts of the Ksuid.
         */
        KsuidComponents parse(String ksuid) {
            final Base62 base62 = Base62.createInstance();
            final byte[] buffer = base62.decode(ksuid.getBytes());

            final long timestamp = decodeTimestamp(buffer);
            final byte[] payload = decodePayload(buffer);

            return new KsuidComponents(ksuid, timestamp, payload);
        }

    }

}
