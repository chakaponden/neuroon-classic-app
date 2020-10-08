package com.raizlabs.android.dbflow.structure;

public class InvalidDBConfiguration extends RuntimeException {
    public InvalidDBConfiguration() {
        super("No Databases were found. Did you create a Database Annotation placeholder class?");
    }

    public InvalidDBConfiguration(String message) {
        super(message);
    }
}
