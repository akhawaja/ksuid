package com.amirkhawaja;

import java.util.Arrays;

public class KsuidComponents {

    private byte[] payload;
    private String ksuid;
    private long timestamp;

    /**
     * Class constructor.
     *
     * @param ksuid The KSUID value.
     * @param timestamp The timestamp.
     * @param payload The payload.
     */
    public KsuidComponents(String ksuid, long timestamp, byte[] payload) {
        this.payload = payload;
        this.ksuid = ksuid;
        this.timestamp = timestamp;
    }

    public byte[] getPayload() {
        return payload;
    }

    public String getKsuid() {
        return ksuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KsuidComponents)) {
            return false;
        }

        KsuidComponents that = (KsuidComponents) o;

        if (getTimestamp() != that.getTimestamp()) {
            return false;
        }
        if (!Arrays.equals(getPayload(), that.getPayload())) {
            return false;
        }
        return getKsuid().equals(that.getKsuid());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getPayload());
        result = 31 * result + getKsuid().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "KsuidComponents{" +
            "payload=" + Arrays.toString(payload) +
            ", ksuid='" + ksuid + '\'' +
            ", timestamp=" + timestamp +
            '}';
    }
}
