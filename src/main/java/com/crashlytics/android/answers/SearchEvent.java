package com.crashlytics.android.answers;

public class SearchEvent extends PredefinedEvent<SearchEvent> {
    static final String QUERY_ATTRIBUTE = "query";
    static final String TYPE = "search";

    public SearchEvent putQuery(String query) {
        this.predefinedAttributes.put(QUERY_ATTRIBUTE, query);
        return this;
    }

    /* access modifiers changed from: package-private */
    public String getPredefinedType() {
        return TYPE;
    }
}
