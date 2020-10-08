package io.fabric.sdk.android.services.common;

public abstract class Crash {
    private static final String UNKNOWN_EXCEPTION = "<unknown>";
    private final String exceptionName;
    private final String sessionId;

    public Crash(String sessionId2) {
        this(sessionId2, UNKNOWN_EXCEPTION);
    }

    public Crash(String sessionId2, String exceptionName2) {
        this.sessionId = sessionId2;
        this.exceptionName = exceptionName2;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public static class LoggedException extends Crash {
        public LoggedException(String sessionId) {
            super(sessionId);
        }

        public LoggedException(String sessionId, String exceptionName) {
            super(sessionId, exceptionName);
        }
    }

    public static class FatalException extends Crash {
        public FatalException(String sessionId) {
            super(sessionId);
        }

        public FatalException(String sessionId, String exceptionName) {
            super(sessionId, exceptionName);
        }
    }
}
