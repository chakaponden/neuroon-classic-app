package io.fabric.sdk.android.services.common;

class AdvertisingInfo {
    public final String advertisingId;
    public final boolean limitAdTrackingEnabled;

    AdvertisingInfo(String advertisingId2, boolean limitAdTrackingEnabled2) {
        this.advertisingId = advertisingId2;
        this.limitAdTrackingEnabled = limitAdTrackingEnabled2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdvertisingInfo infoToCompare = (AdvertisingInfo) o;
        if (this.limitAdTrackingEnabled != infoToCompare.limitAdTrackingEnabled) {
            return false;
        }
        if (this.advertisingId != null) {
            if (this.advertisingId.equals(infoToCompare.advertisingId)) {
                return true;
            }
        } else if (infoToCompare.advertisingId == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.advertisingId != null) {
            result = this.advertisingId.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.limitAdTrackingEnabled) {
            i = 1;
        }
        return i2 + i;
    }
}
