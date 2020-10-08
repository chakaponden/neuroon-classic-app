package io.intercom.android.sdk.activities;

import android.app.Activity;
import android.os.Bundle;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.interfaces.OnConversationInteractionListener;

public class TestActivity extends Activity implements OnConversationInteractionListener {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intercomsdk_activity_test);
    }

    public void loadInbox() {
    }
}
