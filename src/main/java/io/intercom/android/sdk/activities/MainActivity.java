package io.intercom.android.sdk.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.Inbox.ConversationsListFragment;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.conversation.ConversationFragment;
import io.intercom.android.sdk.conversation.SmallAnnouncementFragment;
import io.intercom.android.sdk.interfaces.OnConversationClickListener;
import io.intercom.android.sdk.interfaces.OnConversationInteractionListener;
import io.intercom.android.sdk.interfaces.OnSmallAnnouncementInteractionListener;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.Events.CloseIAMEvent;
import io.intercom.android.sdk.models.Part;
import io.intercom.com.squareup.otto.Subscribe;

@TargetApi(15)
public class MainActivity extends Activity implements OnConversationClickListener, OnConversationInteractionListener, OnSmallAnnouncementInteractionListener {
    public static final String OPEN_CONVERSATION = "OpenConversation";
    public static final String SHOW_INBOX = "showInbox";
    private static final String TAG_CONVERSATION = "conversationTag";
    private static final String TAG_INBOX = "inboxTag";
    private static final String TAG_SMALL_ANNOUNCEMENT = "smallAnnouncementTag";
    private final ConversationsListFragment inbox = ConversationsListFragment.getInstance();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intercomsdk_activity_main);
        Bridge.init(getApplication());
        getFragmentManager().executePendingTransactions();
        initializeSDK();
        Bridge.getBus().register(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Bridge.getBus().unregister(this);
        super.onDestroy();
    }

    private void initializeSDK() {
        Bundle data = getIntent().getExtras();
        Conversation conversation = new Conversation();
        boolean shouldShowInbox = false;
        boolean isRead = false;
        int unreadCount = 0;
        if (data != null) {
            if (data.containsKey(ConversationFragment.ARG_UNREAD_COUNT)) {
                unreadCount = data.getInt(ConversationFragment.ARG_UNREAD_COUNT);
            }
            if (data.containsKey(ConversationFragment.ARG_IS_READ)) {
                isRead = data.getBoolean(ConversationFragment.ARG_IS_READ, false);
            }
            if (data.containsKey(OPEN_CONVERSATION)) {
                conversation = (Conversation) data.getParcelable(OPEN_CONVERSATION);
            }
            if (data.containsKey(SHOW_INBOX)) {
                shouldShowInbox = data.getBoolean(SHOW_INBOX);
            }
        }
        displaySDK(shouldShowInbox, conversation, unreadCount, isRead);
    }

    private void displaySDK(boolean shouldShowInbox, Conversation conversation, int unreadCount, boolean isRead) {
        if (checkIfSmallAnnouncement(conversation)) {
            displayFragment(SmallAnnouncementFragment.newInstance(conversation.getId(), conversation.getLastPart()), TAG_SMALL_ANNOUNCEMENT, false);
        } else if (shouldShowInbox) {
            displayFragment(this.inbox, TAG_INBOX, false);
        } else {
            displayFragment(ConversationFragment.newInstance(conversation.getId(), unreadCount, isRead), TAG_CONVERSATION, false);
        }
    }

    private boolean checkIfSmallAnnouncement(Conversation conversation) {
        return Part.SMALL_ANNOUNCEMENT_MESSAGE_STYLE.equals(conversation.getLastPart().getMessageStyle());
    }

    private void displayFragment(Fragment frag, String tag, boolean addToBackStack) {
        IntercomLogger.INTERNAL("frag", "displaying " + frag);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, frag, tag);
        if (addToBackStack) {
            IntercomLogger.INTERNAL("frag", "adding " + frag + " to the back stack");
            transaction.addToBackStack((String) null);
        }
        transaction.commit();
    }

    public void onBackPressed() {
        IntercomLogger.INTERNAL("frag", "back pressed");
        super.onBackPressed();
    }

    public void loadConversation(String conversationId, int unreadCount, boolean isRead) {
        displayFragment(ConversationFragment.newInstance(conversationId, unreadCount, isRead), TAG_CONVERSATION, true);
    }

    public void loadInbox() {
        if (getFragmentManager().findFragmentByTag(TAG_INBOX) != null) {
            onBackPressed();
        } else {
            displayFragment(this.inbox, TAG_INBOX, false);
        }
    }

    public void transitionToConversation(String conversationId) {
        displayFragment(ConversationFragment.newInstance(conversationId, 0, true), TAG_CONVERSATION, false);
    }

    @Subscribe
    public void closeSDK(CloseIAMEvent event) {
        finish();
    }
}
