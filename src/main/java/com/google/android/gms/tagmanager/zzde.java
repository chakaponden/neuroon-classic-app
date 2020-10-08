package com.google.android.gms.tagmanager;

class zzde extends Number implements Comparable<zzde> {
    private double zzblB;
    private long zzblC;
    private boolean zzblD = false;

    private zzde(double d) {
        this.zzblB = d;
    }

    private zzde(long j) {
        this.zzblC = j;
    }

    public static zzde zza(Double d) {
        return new zzde(d.doubleValue());
    }

    public static zzde zzam(long j) {
        return new zzde(j);
    }

    public static zzde zzgs(String str) throws NumberFormatException {
        try {
            return new zzde(Long.parseLong(str));
        } catch (NumberFormatException e) {
            try {
                return new zzde(Double.parseDouble(str));
            } catch (NumberFormatException e2) {
                throw new NumberFormatException(str + " is not a valid TypedNumber");
            }
        }
    }

    public byte byteValue() {
        return (byte) ((int) longValue());
    }

    public double doubleValue() {
        return zzHv() ? (double) this.zzblC : this.zzblB;
    }

    public boolean equals(Object other) {
        return (other instanceof zzde) && compareTo((zzde) other) == 0;
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public int hashCode() {
        return new Long(longValue()).hashCode();
    }

    public int intValue() {
        return zzHx();
    }

    public long longValue() {
        return zzHw();
    }

    public short shortValue() {
        return zzHy();
    }

    public String toString() {
        return zzHv() ? Long.toString(this.zzblC) : Double.toString(this.zzblB);
    }

    public boolean zzHu() {
        return !zzHv();
    }

    public boolean zzHv() {
        return this.zzblD;
    }

    public long zzHw() {
        return zzHv() ? this.zzblC : (long) this.zzblB;
    }

    public int zzHx() {
        return (int) longValue();
    }

    public short zzHy() {
        return (short) ((int) longValue());
    }

    /* renamed from: zza */
    public int compareTo(zzde zzde) {
        return (!zzHv() || !zzde.zzHv()) ? Double.compare(doubleValue(), zzde.doubleValue()) : new Long(this.zzblC).compareTo(Long.valueOf(zzde.zzblC));
    }
}
