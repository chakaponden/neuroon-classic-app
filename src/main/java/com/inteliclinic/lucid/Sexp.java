package com.inteliclinic.lucid;

public class Sexp {
    public static native boolean NullPointer(long j);

    public static native void SetSexpEvaled(long j);

    public static native void SexpAllocAndCreateNilPointerPointer(long j);

    public static native long SexpAllocAndInitPointer();

    public static native long SexpAllocPointerPointer();

    public static native boolean SexpCheckIfIsBroadcast(long j);

    public static native boolean SexpCheckIfIsMgrCall(long j);

    public static native long SexpCopyNext(long j);

    public static native long SexpCopyNextNext(long j);

    public static native void SexpCreateNilPointer(long j);

    public static native void SexpCreateNilPointerPointer(long j);

    public static native void SexpDeleteAndReuseMem(long j);

    public static native void SexpDeleteAndReuseMemPointerPointer(long j);

    public static native void SexpDeletePointer(long j);

    public static native void SexpFree(long j);

    public static native long SexpFromTwoToOnePointer(long j);

    public static native int SexpGetArraySize(long j);

    public static native double SexpGetArrayValue(long j, int i);

    public static native boolean SexpGetBoolValue(long j);

    public static native double SexpGetDoubleValue(long j);

    public static native int SexpGetIntegerValue(long j);

    public static native String SexpGetStringValue(long j);

    public static native int SexpGetType(long j);

    public static native int SexpGetValueSize(long j);

    public static native long SexpInitPointer();

    public static native boolean SexpIsSpecialFormAndSpecialQuote(long j);

    public static native void SexpListPrepend(long j, long j2);

    public static native long SexpNextPointer(long j);

    public static native long SexpNextPointerPointer(long j);

    public static native String SexpPrintSexp(long j);

    public static native void SexpSetNext(long j, long j2);

    public static native long SexpSubExpsHeadNextPointer(long j);

    public static native long SexpSubExpsHeadPointer(long j);

    public static native boolean isSpecialKeywordAlist(long j);

    public static native boolean isValidSexp(long j);

    public static native long listPop(long j);

    public static native void pointerEqualsDoublePointer(long j, long j2);

    public static native long valueSexp(long j);

    static {
        System.loadLibrary("lucid");
    }
}
