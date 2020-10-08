package io.intercom.android.sdk.Inbox;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.interfaces.OnConversationClickListener;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.ConversationList;
import io.intercom.android.sdk.models.Events.CloseIAMEvent;
import io.intercom.android.sdk.models.Events.ConversationEvent;
import io.intercom.android.sdk.models.Events.ConversationsListDataChanged;
import io.intercom.android.sdk.models.Events.Failure.InboxFailedEvent;
import io.intercom.android.sdk.models.Events.InboxEvent;
import io.intercom.android.sdk.models.Events.NewConversationEvent;
import io.intercom.android.sdk.models.Events.ReadEvent;
import io.intercom.android.sdk.models.Events.ReplyEvent;
import io.intercom.android.sdk.models.Events.realtime.CreateConversationEvent;
import io.intercom.android.sdk.models.Events.realtime.NewCommentEvent;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.com.squareup.otto.Subscribe;
import java.util.Iterator;

@TargetApi(15)
public class ConversationsListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    private static final int INBOX_PAGE_SIZE = 10;
    private ImageButton composerButton;
    private OnConversationClickListener conversationClickListener;
    protected ConversationsHolder conversationsHolder;
    private ConversationsListAdapter conversationsListAdapter;
    private LinearLayout emptyView;
    private LinearLayout errorView;
    private ListView inboxView;
    private boolean loading;
    private LinearLayout loadingView;
    private String openConversationId = "";
    private Button retryButton;
    private View rootView;
    private int unreadCount;

    public static ConversationsListFragment getInstance() {
        return new ConversationsListFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bridge.init(getActivity().getApplication());
        this.loading = true;
        this.conversationsHolder = new ConversationsHolder();
        this.conversationsListAdapter = new ConversationsListAdapter(getActivity(), R.layout.intercomsdk_inbox_row, this.conversationsHolder);
        Bridge.getBus().register(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        IntercomLogger.INTERNAL("inbox frag", "creating view for fragment");
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.intercomsdk_fragment_inbox, container, false);
            this.loadingView = (LinearLayout) this.rootView.findViewById(R.id.loading_layout);
            this.errorView = (LinearLayout) this.rootView.findViewById(R.id.error_layout);
            this.emptyView = (LinearLayout) this.rootView.findViewById(R.id.empty_layout);
            this.inboxView = (ListView) this.rootView.findViewById(R.id.listView);
            this.retryButton = (Button) this.rootView.findViewById(R.id.retry_button);
            this.composerButton = (ImageButton) this.rootView.findViewById(R.id.compose_button);
            FontUtils.setTypeface(this.retryButton, FontUtils.ROBOTO_MEDIUM, getActivity());
            FontUtils.setTypeface((TextView) this.rootView.findViewById(R.id.appTitleText), FontUtils.ROBOTO_MEDIUM, getActivity());
            this.composerButton.setOnClickListener(this);
            ((ImageButton) this.rootView.findViewById(R.id.close_button)).setOnClickListener(this);
            this.retryButton.setOnClickListener(this);
            this.inboxView.setOnItemClickListener(this);
            this.inboxView.setOnScrollListener(this);
            this.inboxView.setAdapter(this.conversationsListAdapter);
            this.conversationsListAdapter.notifyDataSetChanged();
            displayLoadingView();
            fetchNewConversations();
        } else {
            ViewGroup parent = (ViewGroup) this.rootView.getParent();
            if (parent != null) {
                parent.removeView(this.rootView);
            }
        }
        this.openConversationId = "";
        enableComposerButton(true);
        return this.rootView;
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        if (this.loading) {
            return;
        }
        if (this.conversationsHolder.isEmpty()) {
            displayEmptyView();
        } else {
            displayInbox();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        Bridge.getBus().unregister(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close_button) {
            Bridge.getBus().post(new CloseIAMEvent());
        } else if (id == R.id.compose_button) {
            this.conversationClickListener.loadConversation("", this.unreadCount, true);
        } else if (id == R.id.retry_button) {
            displayLoadingView();
            fetchNewConversations();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Conversation conversation = (Conversation) this.conversationsHolder.get(position);
        this.conversationClickListener.loadConversation(conversation.getId(), this.unreadCount, conversation.getRead().booleanValue());
        this.openConversationId = conversation.getId();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.conversationClickListener = (OnConversationClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement OnConversationClickListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.conversationClickListener = null;
    }

    private void enableComposerButton(boolean enable) {
        if (!Bridge.getIdentityStore().getAppConfig().isInboundMessages() || !enable) {
            this.composerButton.setVisibility(8);
        } else {
            this.composerButton.setVisibility(0);
        }
    }

    private void displayInbox() {
        if (isAdded()) {
            this.errorView.setVisibility(8);
            this.inboxView.setVisibility(0);
            this.emptyView.setVisibility(8);
            this.loadingView.setVisibility(8);
        }
    }

    private void displayError() {
        if (isAdded()) {
            this.errorView.setVisibility(0);
            this.inboxView.setVisibility(8);
            this.emptyView.setVisibility(8);
            this.loadingView.setVisibility(8);
        }
    }

    private void displayEmptyView() {
        if (isAdded()) {
            ((TextView) this.emptyView.findViewById(R.id.empty_text)).setText(getResources().getString(R.string.intercomsdk_empty_conversations));
            this.errorView.setVisibility(8);
            this.inboxView.setVisibility(8);
            this.emptyView.setVisibility(0);
            this.loadingView.setVisibility(8);
        }
    }

    private void displayLoadingView() {
        if (isAdded()) {
            this.errorView.setVisibility(8);
            this.inboxView.setVisibility(8);
            this.emptyView.setVisibility(8);
            this.loadingView.setVisibility(0);
            ((ProgressBar) this.loadingView.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()), PorterDuff.Mode.SRC_IN);
        }
    }

    private void displayAsSessionError() {
        this.retryButton.setVisibility(8);
        enableComposerButton(false);
        displayError();
    }

    private void addLoadingCell() {
        this.conversationsHolder.add(new Conversation.Loading());
        this.conversationsListAdapter.notifyDataSetChanged();
    }

    private void removeLoadingCell() {
        int numConversations = this.conversationsHolder.size();
        if (numConversations > 0 && (this.conversationsHolder.get(numConversations - 1) instanceof Conversation.Loading)) {
            this.conversationsHolder.remove(numConversations - 1);
            this.conversationsListAdapter.notifyDataSetChanged();
        }
    }

    private void fetchNewConversations() {
        Bridge.getApi().getInbox();
        Bridge.getPoller().resetInboxPoll();
    }

    private void realtimeUpdate() {
        if (Bridge.getIdentityStore().getAppConfig().isRealTime()) {
            fetchNewConversations();
        } else {
            Bridge.getPoller().throttledInboxPoll();
        }
    }

    @Subscribe
    public void inboxSuccess(InboxEvent event) {
        ConversationList list = event.getResponse();
        this.unreadCount = list.getTotalUnreadCount();
        removeLoadingCell();
        this.conversationsHolder.syncInbox(list);
        if (isAdded()) {
            if (!list.getConversations().isEmpty() || !this.conversationsHolder.isEmpty()) {
                displayInbox();
            } else {
                displayEmptyView();
            }
            this.loading = false;
            enableComposerButton(true);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!this.loading && totalItemCount >= firstVisibleItem && totalItemCount <= firstVisibleItem + visibleItemCount && visibleItemCount > 1 && totalItemCount >= 10) {
            Bridge.getApi().getInboxBefore(((Conversation) this.conversationsHolder.get(this.conversationsHolder.size() - 1)).getLastPart().getCreatedAt());
            addLoadingCell();
            this.loading = true;
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private void markConversationAsRead(String conversationId) {
        if (conversationId != null) {
            int i = 0;
            while (true) {
                if (i >= this.conversationsHolder.size()) {
                    break;
                }
                Conversation conversation = (Conversation) this.conversationsHolder.get(i);
                if (conversation.getId().equals(conversationId)) {
                    conversation.setRead(true);
                    break;
                }
                i++;
            }
            conditionallyNotifyDataSetChanged();
        }
    }

    @Subscribe
    public void inboxFailedToLoad(InboxFailedEvent event) {
        if (isAdded()) {
            displayError();
        }
    }

    @Subscribe
    public void newComment(NewCommentEvent event) {
        if (!event.getConversationId().equals(this.openConversationId)) {
            realtimeUpdate();
        }
    }

    @Subscribe
    public void newConversation(CreateConversationEvent event) {
        realtimeUpdate();
    }

    @Subscribe
    public void markedAsRead(ReadEvent event) {
        Iterator it = this.conversationsHolder.iterator();
        while (it.hasNext()) {
            Conversation conversation = (Conversation) it.next();
            if (conversation.getId().equals(event.getConversationId()) && !conversation.getRead().booleanValue()) {
                this.unreadCount--;
                conversation.setRead(true);
                conditionallyNotifyDataSetChanged();
                return;
            }
        }
    }

    @Subscribe
    public void newConversationEvent(NewConversationEvent event) {
        Conversation newConversation = event.getResponse();
        if (!this.conversationsHolder.contains(newConversation)) {
            this.conversationsHolder.add(0, newConversation);
            conditionallyNotifyDataSetChanged();
        }
    }

    @Subscribe
    public void conversationRefresh(ConversationEvent event) {
        Conversation newConversation = event.getResponse();
        for (int i = 0; i < this.conversationsHolder.size(); i++) {
            Conversation conversation = (Conversation) this.conversationsHolder.get(i);
            if (conversation.getId().equals(newConversation.getId())) {
                this.conversationsHolder.remove(i);
                this.conversationsHolder.add(i, newConversation);
                if (!newConversation.getRead().booleanValue() && conversation.getRead().booleanValue()) {
                    this.unreadCount++;
                }
                conditionallyNotifyDataSetChanged();
                return;
            }
        }
    }

    @Subscribe
    public void replyInConversation(ReplyEvent event) {
        for (int i = 0; i < this.conversationsHolder.size(); i++) {
            if (((Conversation) this.conversationsHolder.get(i)).getId().equals(event.getConversationId())) {
                Conversation conversation = (Conversation) this.conversationsHolder.remove(i);
                conversation.getParts().add(event.getResponse());
                this.conversationsHolder.add(0, conversation);
                conditionallyNotifyDataSetChanged();
                return;
            }
        }
    }

    @Subscribe
    public void dataSetChanged(ConversationsListDataChanged event) {
        conditionallyNotifyDataSetChanged();
    }

    private void conditionallyNotifyDataSetChanged() {
        if (isAdded()) {
            this.conversationsListAdapter.notifyDataSetChanged();
        }
    }
}
