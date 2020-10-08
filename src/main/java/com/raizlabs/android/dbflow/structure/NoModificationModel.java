package com.raizlabs.android.dbflow.structure;

abstract class NoModificationModel implements Model {
    NoModificationModel() {
    }

    public void save() {
        throw new InvalidSqlViewOperationException("View " + getClass().getName() + " is not saveable");
    }

    public void delete() {
        throw new InvalidSqlViewOperationException("View " + getClass().getName() + " is not deleteable");
    }

    public void update() {
        throw new InvalidSqlViewOperationException("View " + getClass().getName() + " is not updateable");
    }

    public void insert() {
        throw new InvalidSqlViewOperationException("View " + getClass().getName() + " is not insertable");
    }

    static class InvalidSqlViewOperationException extends RuntimeException {
        InvalidSqlViewOperationException(String detailMessage) {
            super(detailMessage);
        }
    }
}
