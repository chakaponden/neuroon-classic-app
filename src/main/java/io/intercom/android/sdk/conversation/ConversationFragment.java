package io.intercom.android.sdk.conversation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.attachments.AttachmentActivity;
import io.intercom.android.sdk.attachments.AttachmentData;
import io.intercom.android.sdk.blocks.BlockType;
import io.intercom.android.sdk.blocks.Blocks;
import io.intercom.android.sdk.blocks.Image;
import io.intercom.android.sdk.blocks.models.Block;
import io.intercom.android.sdk.blocks.models.BlockAttachment;
import io.intercom.android.sdk.commons.utilities.DeviceUtils;
import io.intercom.android.sdk.conversation.events.AdminIsTypingEvent;
import io.intercom.android.sdk.conversation.events.AdminTypingEndedEvent;
import io.intercom.android.sdk.conversation.events.CancelAdminTypingEvent;
import io.intercom.android.sdk.conversation.events.SendingEvent;
import io.intercom.android.sdk.models.Avatar;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.Events.ConversationEvent;
import io.intercom.android.sdk.models.Events.Failure.ConversationFailedEvent;
import io.intercom.android.sdk.models.Events.Failure.NewConversationFailedEvent;
import io.intercom.android.sdk.models.Events.Failure.ReplyFailedEvent;
import io.intercom.android.sdk.models.Events.Failure.UploadFailedEvent;
import io.intercom.android.sdk.models.Events.InboxEvent;
import io.intercom.android.sdk.models.Events.NewConversationEvent;
import io.intercom.android.sdk.models.Events.ReplyEvent;
import io.intercom.android.sdk.models.Events.UnreadConversationsEvent;
import io.intercom.android.sdk.models.Events.UploadEvent;
import io.intercom.android.sdk.models.Events.realtime.NewCommentEvent;
import io.intercom.android.sdk.models.LWR;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.Participant;
import io.intercom.android.sdk.nexus.NexusEvent;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.android.sdk.utilities.NameUtils;
import io.intercom.android.sdk.views.AdminIsTypingView;
import io.intercom.android.sdk.views.ConversationListView;
import io.intercom.com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@TargetApi(15)
public class ConversationFragment extends BaseConversationFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int ACTIVITY_RESULT_UPLOAD_CONFIRMED = 2;
    private static final int ACTIVITY_RESULT_UPLOAD_SELECTED = 1;
    private static final String ARG_CONVERSATION_ID = "conversationId";
    public static final String ARG_IS_READ = "intercomsdk-isRead";
    public static final String ARG_UNREAD_COUNT = "intercomsdk-unreadCount";
    private static final int DELIVERED_TIMER = 10000;
    private static final String INTERCOM_INBOUND_URL = "http://www.intercom.io/?utm_source=android-sdk&utm_medium=inbound&utm_campaign=powered-by-intercom";
    private static final String INTERCOM_OUTBOUND_URL = "http://www.intercom.io/?utm_source=android-sdk&utm_medium=outbound&utm_campaign=powered-by-intercom";
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    protected ComposerInputView composer;
    private Conversation conversation;
    private ConversationListView conversationList;
    private TextView conversationTitleText;
    private final Set<String> currentTypers = new HashSet();
    private ImageButton inboxButton;
    private LinearLayout loadingLayout;
    /* access modifiers changed from: private */
    public TextView pill;
    protected View rootView;
    private final List<Part> sendingFailures = new ArrayList();
    /* access modifiers changed from: private */
    public SpoolWrapper spool;
    private Future timestampUpdateFuture;
    private int unreadCount = 0;
    private Button unreadText;

    public static ConversationFragment newInstance(String conversationId, int unreadCount2, boolean isRead) {
        ConversationFragment frag = new ConversationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONVERSATION_ID, conversationId);
        args.putInt(ARG_UNREAD_COUNT, unreadCount2);
        args.putBoolean(ARG_IS_READ, isRead);
        frag.setArguments(args);
        return frag;
    }

    @SuppressLint({"NewApi"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.conversationId = args.getString(ARG_CONVERSATION_ID, "");
            this.unreadCount = args.getInt(ARG_UNREAD_COUNT, 0);
            if (!args.getBoolean(ARG_IS_READ, false)) {
                this.unreadCount--;
            }
        }
        this.conversationParts = new ArrayList();
        this.conversation = new Conversation();
        this.spool = new SpoolWrapper(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.intercomsdk_fragment_conversation, container, false);
        } else {
            ViewGroup parent = (ViewGroup) this.rootView.getParent();
            if (parent != null) {
                parent.removeView(this.rootView);
            }
        }
        this.unreadText = (Button) this.rootView.findViewById(R.id.unread_text);
        this.loadingLayout = (LinearLayout) this.rootView.findViewById(R.id.loading_layout);
        this.conversationTitleText = (TextView) this.rootView.findViewById(R.id.conversation_title_text);
        this.pill = (TextView) this.rootView.findViewById(R.id.pill);
        this.pill.setOnClickListener(this);
        this.conversationList = (ConversationListView) this.rootView.findViewById(R.id.conversation_list);
        this.conversationList.setOnBottomReachedListener(new ConversationListView.OnBottomReachedListener() {
            public void onBottomReached() {
                ConversationFragment.this.pill.setVisibility(8);
            }
        });
        ((GradientDrawable) this.unreadText.getBackground()).setColor(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
        this.blocks = new Blocks(getActivity().getApplicationContext());
        this.inboxButton = (ImageButton) this.rootView.findViewById(R.id.inbox_button);
        ((ImageButton) this.rootView.findViewById(R.id.close_composer_button)).setOnClickListener(this);
        this.inboxButton.setOnClickListener(this);
        this.conversationList.setOnItemClickListener(this);
        FontUtils.setTypeface(this.conversationTitleText, FontUtils.ROBOTO_MEDIUM, getActivity());
        this.adapter = new ConversationAdapter(getActivity(), R.layout.intercomsdk_row_user_part, this.conversationParts);
        this.conversationList.setAdapter(this.adapter);
        Bridge.getBus().register(this);
        this.composer = (ComposerInputView) this.rootView.findViewById(R.id.composer_input_view);
        setupComposer();
        updateUnreadCount(this.unreadCount);
        return this.rootView;
    }

    public void onStart() {
        super.onStart();
        if (!this.conversationId.isEmpty()) {
            Bridge.getPoller().startConversationPolling(this.conversationId);
        }
    }

    public void onStop() {
        super.onStop();
        if (!this.conversationId.isEmpty()) {
            Bridge.getPoller().endConversationPolling();
        }
    }

    public void onDestroyView() {
        Bridge.getBus().unregister(this);
        if (this.timestampUpdateFuture != null) {
            this.timestampUpdateFuture.cancel(true);
        }
        this.composer.cleanup();
        super.onDestroyView();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.inbox_button) {
            view.setEnabled(false);
            inboxTapped();
        } else if (id == R.id.close_composer_button) {
            closeTapped();
        } else if (id == R.id.pill) {
            this.conversationList.smoothScrollToPosition(this.conversationList.getBottom());
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Part part = (Part) this.conversationParts.get(position);
        if (Part.POWERED_BY_STYLE.equals(part.getMessageStyle())) {
            String poweredByUrl = INTERCOM_OUTBOUND_URL;
            Part firstPart = (Part) this.conversationParts.get(0);
            if (Part.WELCOME_MESSAGE_STYLE.equals(firstPart.getMessageStyle()) || !firstPart.isAdmin()) {
                poweredByUrl = INTERCOM_INBOUND_URL;
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(poweredByUrl));
            intent.setFlags(268435456);
            startActivity(intent);
        } else if (part.getMessageState() == Part.MessageState.FAILED) {
            part.setMessageState(Part.MessageState.SENDING);
            part.setFooter(getString(R.string.intercomsdk_sending));
            this.conversationParts.remove(this.conversationParts.indexOf(part));
            this.conversationParts.add(this.conversationParts.size(), part);
            this.adapter.notifyDataSetChanged();
            String partText = part.getBlocks().get(0).getText();
            if (TextUtils.isEmpty(this.conversationId)) {
                startConversation(partText, part.getId());
            } else {
                replyToConversation(partText, part, this.conversationParts.size() - 1);
            }
        } else if (part.getMessageState() == Part.MessageState.UPLOAD_FAILED) {
            part.setMessageState(Part.MessageState.SENDING);
            part.setFooter(getString(R.string.intercomsdk_sending));
            this.conversationParts.remove(this.conversationParts.indexOf(part));
            this.conversationParts.add(this.conversationParts.size(), part);
            this.adapter.notifyDataSetChanged();
            uploadFile(part.getFileUri(), part.getId());
        }
    }

    /* access modifiers changed from: protected */
    public void closeTapped() {
        this.composer.hideKeyboard();
        super.closeTapped();
    }

    private void inboxTapped() {
        this.composer.hideKeyboard();
        this.listener.loadInbox();
    }

    private void sendTapped() {
        String text = this.composer.getTrimmedText();
        if (!text.isEmpty()) {
            Part message = createSendingUI(text);
            this.spool.playReplySendingSound();
            if (TextUtils.isEmpty(this.conversationId)) {
                startConversation(text, message.getId());
                this.composer.clear();
            } else {
                replyToConversation(text, message, this.conversationParts.size() - 1);
                this.composer.clear();
            }
            this.conversationList.smoothScrollToPosition(this.conversationList.getBottom());
        }
    }

    private void attachmentTapped() {
        if (DeviceUtils.hasPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE")) {
            launchContentSelector();
        } else if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 1) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults[0] == 0) {
            launchContentSelector();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (-1 == resultCode) {
            switch (requestCode) {
                case 1:
                    Intent intent = new Intent(getActivity(), AttachmentActivity.class);
                    intent.setData(data.getData());
                    startActivityForResult(intent, 2);
                    break;
                case 2:
                    uploadFile(data.getData(), "");
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void launchContentSelector() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    private void uploadFile(Uri uri, String id) {
        if (uri != null) {
            new LoadFileTask().execute(new Object[]{uri, id});
        }
    }

    /* access modifiers changed from: private */
    public Part createAttachmentBlock(String name, String url, String contentType, int width, int height, Uri fileUri) {
        List<BlockAttachment> attachments = new ArrayList<>();
        List<Block> blockParts = new ArrayList<>();
        Participant user = getParticipant();
        Part message = new Part();
        if (contentType.contains(Image.MIME_TYPE)) {
            blockParts.add(new Block.Builder().withUrl(url).withType(BlockType.LOCALIMAGE.name()).withWidth(width).withHeight(height).build());
            message.isImageOnly(true);
        } else {
            attachments.add(new BlockAttachment.Builder().withName(name).withUrl(url).withContentType(contentType).build());
            blockParts.add(new Block.Builder().withAttachments(attachments).withType(BlockType.LOCAL_ATTACHMENT.name()).build());
        }
        message.setMessageStyle(Part.CHAT_MESSAGE_STYLE);
        message.setMessageState(Part.MessageState.SENDING);
        message.setParticipant(user);
        message.setBlocks(blockParts);
        message.setFooter(getString(R.string.intercomsdk_sending));
        message.setCreatedAt(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        message.setLayout(this.blocks.createBlocks(blockParts, this.generator.getPreviewHolder()));
        message.setAttachments(new ArrayList());
        message.setId(UUID.randomUUID().toString());
        message.setEntranceAnimation(true);
        message.setShowCreatedAt(true);
        message.setFileUri(fileUri);
        if (this.conversationParts.size() != 1 || !((Part) this.conversationParts.get(0)).getMessageStyle().equals(Part.POWERED_BY_STYLE)) {
            this.conversationParts.add(this.conversationParts.size(), message);
        } else {
            this.conversationParts.add(0, message);
        }
        this.sendingFailures.add(message);
        this.adapter.notifyDataSetChanged();
        this.conversationList.smoothScrollToPosition(this.conversationList.getBottom());
        return message;
    }

    private void setupComposer() {
        this.composer.requestInputFocus();
        configureInputView(this.conversation);
        if (this.conversationId.isEmpty()) {
            addWelcomeMessage();
            if (!hasPoweredBy(this.conversationParts) && Bridge.getIdentityStore().getAppConfig().isShowPoweredBy()) {
                addPoweredBy(false);
            }
            this.conversationList.setVisibility(0);
            this.conversationTitleText.setText(R.string.intercomsdk_new_conversation_title);
            this.composer.setHint(R.string.intercomsdk_start_new_conversation);
            return;
        }
        this.composer.setConversationId(this.conversationId);
        Bridge.getApi().getConversation(this.conversationId);
        this.loadingLayout.setVisibility(0);
        ((ProgressBar) this.loadingLayout.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()), PorterDuff.Mode.SRC_IN);
        this.conversationList.setVisibility(0);
        this.composer.setVisibility(8);
        this.rootView.findViewById(R.id.bottom_shadow).setVisibility(8);
        this.adapter.notifyDataSetChanged();
    }

    private void updateUnreadCount(int count) {
        if (count <= 0) {
            this.unreadText.setVisibility(8);
            this.inboxButton.setImageResource(R.drawable.intercomsdk_inbox_button_selector);
            return;
        }
        this.unreadText.setVisibility(0);
        this.unreadText.setText(String.valueOf(count));
        this.inboxButton.setImageResource(R.drawable.intercomsdk_inbox_button_badge_selector);
    }

    private void startConversation(String text, String partId) {
        Bridge.getApi().startNewConversation(text, this.conversationParts.size() - 1, partId);
    }

    private void startConversationWithAttachment(int uploadId, String partId) {
        Bridge.getApi().startNewAttachmentConversation(uploadId, this.conversationParts.size() - 1, partId);
    }

    private void replyToConversation(String text, Part part, int partPosition) {
        Bridge.getApi().textReply(this.conversationId, text, partPosition, part.getId());
    }

    private void attachmentToConversation(Part part, int partPosition, int uploadId) {
        Bridge.getApi().attachmentReply(this.conversationId, uploadId, partPosition, part.getId());
    }

    @Subscribe
    public void conversationSuccess(ConversationEvent event) {
        int conversationSize = this.conversation.getParts().size();
        List<Part> eventParts = event.getResponse().getParts();
        if (!eventParts.isEmpty() && eventParts.get(0).isAdmin()) {
            eventParts.get(0).setFirstChatPart(true);
        }
        if (event.getResponse().getId().equals(this.conversationId) && eventParts.size() > conversationSize) {
            this.conversation = event.getResponse();
            if (isAdded()) {
                setupWithConversation();
            }
            if (!this.conversation.getRead().booleanValue()) {
                markAsRead();
                Bridge.getNexusClient().fire(NexusEvent.getConversationSeenEvent(this.conversationId, Bridge.getIdentityStore().getIntercomId()));
            }
            if (this.conversationList.isAtBottom()) {
                this.conversationList.smoothScrollToPosition(this.conversationList.getBottom());
            } else if (conversationSize != 0) {
                this.pill.setVisibility(0);
            }
            if (conversationSize != 0) {
                Iterator<Part> it = eventParts.subList(conversationSize, eventParts.size()).iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().isAdmin()) {
                            this.spool.playAdminReplySound();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                Bridge.getBus().post(new CancelAdminTypingEvent());
                this.currentTypers.clear();
            }
            this.adapter.sort(new Comparator<Part>() {
                public int compare(Part lhs, Part rhs) {
                    return (int) (lhs.getCreatedAt() - rhs.getCreatedAt());
                }
            });
        }
    }

    @Subscribe
    public void conversationFailure(ConversationFailedEvent event) {
    }

    @Subscribe
    public void newConversationSuccess(NewConversationEvent event) {
        if (!this.conversationParts.isEmpty() && ((Part) this.conversationParts.get(this.conversationParts.size() - 1)).getId().equals(event.getIdentifier())) {
            this.conversation = event.getResponse();
            this.conversationId = this.conversation.getId();
            this.conversationParts.remove(this.conversationParts.size() - 1);
            this.sendingFailures.clear();
            this.conversationParts.add(createMessageUI(event.getResponse().getParts().get(0)));
            this.composer.setConversationId(this.conversationId);
            Bridge.getPoller().startConversationPolling(this.conversationId);
            Bridge.getNexusClient().fire(NexusEvent.getCreateConversationEvent(this.conversationId, Bridge.getIdentityStore().getIntercomId()));
            if (!hasPoweredBy(this.conversationParts) && Bridge.getIdentityStore().getAppConfig().isShowPoweredBy()) {
                addPoweredBy(true);
            }
            this.spool.playReplySuccessSound();
            this.adapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void newConversationFailure(NewConversationFailedEvent event) {
        markAsFailed(event.getPosition(), event.getPartId(), false);
    }

    @Subscribe
    public void replySuccess(ReplyEvent event) {
        if (event.getConversationId().equals(this.conversationId)) {
            Bridge.getNexusClient().fire(NexusEvent.getNewCommentEvent(this.conversationId, Bridge.getIdentityStore().getIntercomId()));
            Part response = event.getResponse();
            Participant participant = this.conversation.getParticipant(response.getParticipantId());
            if (participant instanceof Participant.NullParticipant) {
                participant = new Participant.Builder().withId(response.getParticipantId()).build();
                this.conversation.getParticipants().put(response.getParticipantId(), participant);
            }
            response.setParticipant(participant);
            int position = positionOfTempPart(event.getPosition(), event.getPartId());
            if (position >= 0) {
                this.sendingFailures.remove(this.conversationParts.remove(position));
            }
            final Part newPart = createMessageUI(response);
            newPart.setDisplayDelivered(true);
            this.conversationParts.add(this.conversationParts.size(), newPart);
            this.adapter.notifyDataSetChanged();
            this.spool.playReplySuccessSound();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    newPart.setDisplayDelivered(false);
                    ConversationFragment.this.adapter.notifyDataSetChanged();
                }
            }, 10000);
        }
    }

    @Subscribe
    public void replyFailure(ReplyFailedEvent event) {
        markAsFailed(event.getPosition(), event.getPartId(), event.isUpload());
        this.spool.playReplyFailSound();
    }

    @Subscribe
    public void uploadSuccess(UploadEvent event) {
        int partPosition = positionOfTempPart(event.getTempPartPosition(), event.getTempPartId());
        if (partPosition < 0) {
            return;
        }
        if (TextUtils.isEmpty(this.conversationId)) {
            startConversationWithAttachment(event.getUploadId(), event.getTempPartId());
        } else {
            attachmentToConversation((Part) this.conversationParts.get(partPosition), partPosition, event.getUploadId());
        }
    }

    @Subscribe
    public void uploadFailure(UploadFailedEvent event) {
        markAsFailed(event.getPosition(), event.getPartId(), true);
        this.spool.playReplyFailSound();
    }

    private void markAsFailed(int position, String partId, boolean uploadFailed) {
        int partPosition = positionOfTempPart(position, partId);
        if (partPosition >= 0) {
            Part failedPart = (Part) this.conversationParts.get(partPosition);
            failedPart.setMessageState(uploadFailed ? Part.MessageState.UPLOAD_FAILED : Part.MessageState.FAILED);
            failedPart.setFooter(getString(R.string.intercomsdk_sending_failure));
            this.adapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public int positionOfTempPart(int position, String partId) {
        if (position >= 0 && position < this.conversationParts.size()) {
            if (((Part) this.conversationParts.get(position)).getId().equals(partId)) {
                return position;
            }
            for (int i = this.conversationParts.size() - 1; i >= 0; i--) {
                if (((Part) this.conversationParts.get(i)).getId().equals(partId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void setupWithConversation() {
        this.composer.setHint(R.string.intercomsdk_write_a_reply);
        this.conversationList.setVisibility(0);
        this.loadingLayout.setVisibility(8);
        createConversationUI(this.conversation.getParts());
        updateTimeStampScheduler();
    }

    private void addWelcomeMessage() {
        List<Block> welcomeBlocks = Bridge.getIdentityStore().getAppConfig().getWelcomeMessage();
        if (!welcomeBlocks.isEmpty()) {
            LinearLayout layout = this.blocks.createBlocks(welcomeBlocks, this.generator.getWelcomeHolder());
            layout.addView((ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.intercomsdk_welcome_message_icon, layout, false), 0);
            layout.addView((ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.intercomsdk_rowdivider, layout, false));
            Participant user = new Participant.Builder().build();
            Part part = new Part();
            part.setMessageStyle(Part.WELCOME_MESSAGE_STYLE);
            part.setParticipant(user);
            part.setBlocks(welcomeBlocks);
            part.setLayout(layout);
            part.setAttachments(new ArrayList());
            part.setId(UUID.randomUUID().toString());
            this.conversationParts.add(0, part);
            this.adapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void configureInputView(Conversation conversation2) {
        if (conversation2.getParts().isEmpty() || conversation2.getParts().size() != 1 || (conversation2.getParts().get(0).getLightweightReply() instanceof LWR.NullLWR)) {
            this.composer.setVisibility(0);
            this.composer.requestInputFocus();
            this.rootView.findViewById(R.id.bottom_shadow).setVisibility(0);
            return;
        }
        this.composer.setVisibility(8);
        this.rootView.findViewById(R.id.bottom_shadow).setVisibility(8);
    }

    private void createConversationUI(List<Part> parts) {
        List<Participant> admins = new ArrayList<>();
        this.conversationParts.clear();
        for (Part part : parts) {
            if (part.getLayout() == null) {
                part = createMessageUI(part);
            }
            this.conversationParts.add(this.conversationParts.size(), part);
            if (Participant.ADMIN_TYPE.equals(part.getParticipant().getType()) && !part.getParticipantId().equals(Bridge.getIdentityStore().getIntercomId())) {
                admins.add(part.getParticipant());
            }
        }
        if (!parts.isEmpty() && Participant.USER_TYPE.equals(parts.get(0).getParticipant().getType()) && !parts.get(0).getMessageStyle().equals(Part.WELCOME_MESSAGE_STYLE)) {
            addWelcomeMessage();
        }
        if (!hasPoweredBy(parts) && Bridge.getIdentityStore().getAppConfig().isShowPoweredBy()) {
            addPoweredBy(false);
        }
        this.conversationParts.addAll(this.sendingFailures);
        if (parts.get(parts.size() - 1).isFirstChatPart()) {
            this.conversationList.setSelection(0);
        } else {
            this.conversationList.setSelection(this.conversationParts.size() - 1);
        }
        configureInputView(this.conversation);
        this.adapter.notifyDataSetChanged();
        this.conversationList.setVisibility(0);
        updateTitle(admins);
    }

    private Part createSendingUI(String text) {
        List<Block> blockParts = new ArrayList<>();
        blockParts.add(new Block.Builder().withText(text).withType(BlockType.PARAGRAPH.name()).build());
        Participant user = getParticipant();
        Part message = new Part();
        message.setMessageStyle(Part.CHAT_MESSAGE_STYLE);
        message.setMessageState(Part.MessageState.SENDING);
        message.setParticipant(user);
        message.setBlocks(blockParts);
        message.setFooter(getString(R.string.intercomsdk_sending));
        message.setCreatedAt(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        message.setLayout(this.blocks.createBlocks(blockParts, this.generator.getPreviewHolder()));
        message.setAttachments(new ArrayList());
        message.setId(UUID.randomUUID().toString());
        message.setEntranceAnimation(true);
        message.setShowCreatedAt(true);
        if (this.conversationParts.size() != 1 || !((Part) this.conversationParts.get(0)).getMessageStyle().equals(Part.POWERED_BY_STYLE)) {
            this.conversationParts.add(this.conversationParts.size(), message);
        } else {
            this.conversationParts.add(0, message);
        }
        this.sendingFailures.add(message);
        this.adapter.notifyDataSetChanged();
        return message;
    }

    private void updateTitle(List<Participant> admins) {
        Set<String> uniqueAdminIds = new LinkedHashSet<>();
        List<String> adminNames = new ArrayList<>();
        for (int i = admins.size() - 1; i >= 0; i--) {
            Participant user = admins.get(i);
            if (!uniqueAdminIds.contains(user.getId())) {
                adminNames.add(user.getDisplayName());
                uniqueAdminIds.add(user.getId());
            }
        }
        this.conversationTitleText.setText(NameUtils.getFormattedAdmins(adminNames, getActivity()));
    }

    /* access modifiers changed from: protected */
    public int getInputViewVisibility() {
        return this.composer.getVisibility();
    }

    @Subscribe
    public void inboxCallback(InboxEvent event) {
        updateUnreadCount(filterCurrentConversation(event.getResponse().getTotalUnreadCount(), event.getResponse().getConversations()));
    }

    @Subscribe
    public void previewServiceCallback(UnreadConversationsEvent event) {
        updateUnreadCount(filterCurrentConversation(event.getResponse().getTotalUnreadCount(), event.getResponse().getConversations()));
    }

    private int filterCurrentConversation(int newUnreadCount, List<Conversation> conversations) {
        for (Conversation c : conversations) {
            if (c.getId().equals(this.conversationId) && !c.getRead().booleanValue()) {
                return newUnreadCount - 1;
            }
        }
        return newUnreadCount;
    }

    private boolean hasPoweredBy(List<Part> parts) {
        for (Part part : parts) {
            if (part.getMessageStyle().equals(Part.POWERED_BY_STYLE)) {
                return true;
            }
        }
        return false;
    }

    private void addPoweredBy(boolean shouldAnimate) {
        if (!this.conversationParts.isEmpty()) {
            Part poweredBy = new Part();
            poweredBy.setEntranceAnimation(shouldAnimate);
            poweredBy.setMessageStyle(Part.POWERED_BY_STYLE);
            poweredBy.setCreatedAt(((Part) this.conversationParts.get(0)).getCreatedAt() + 1);
            this.conversationParts.add(1, poweredBy);
            this.adapter.notifyDataSetChanged();
        }
    }

    private void updateTimeStampScheduler() {
        this.timestampUpdateFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            public void run() {
                ConversationFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ConversationFragment.this.adapter.notifyDataSetChanged();
                    }
                });
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private Participant getParticipant() {
        String intercomId = Bridge.getIdentityStore().getIntercomId();
        Participant user = this.conversation.getParticipant(intercomId);
        if (user instanceof Participant.NullParticipant) {
            return new Participant.Builder().withId(intercomId).build();
        }
        return user;
    }

    @Subscribe
    public void newComment(NewCommentEvent event) {
        if (!event.getConversationId().equals(this.conversationId)) {
            return;
        }
        if (Bridge.getIdentityStore().getAppConfig().isRealTime()) {
            Bridge.getApi().getConversation(this.conversationId);
            Bridge.getPoller().resetConversationPoll();
            return;
        }
        Bridge.getPoller().throttledConversationPoll(this.conversationId);
    }

    @Subscribe
    public void adminIsTyping(AdminIsTypingEvent event) {
        if (this.conversationId.equals(event.getConversationId())) {
            Bridge.getPoller().resetConversationPoll();
            if (!this.currentTypers.contains(event.getAdminId())) {
                Part isTypingPart = new Part();
                Participant participant = this.conversation.getParticipant(event.getAdminId());
                if (participant instanceof Participant.NullParticipant) {
                    participant = new Participant.Builder().withId(event.getAdminId()).withName(event.getAdminName()).withAvatar(new Avatar.Builder().withImageUrl(event.getAdminAvatarUrl())).withType(Participant.ADMIN_TYPE).build();
                }
                isTypingPart.setParticipant(participant);
                isTypingPart.setMessageStyle(Part.ADMIN_IS_TYPING_STYLE);
                isTypingPart.setId(UUID.randomUUID().toString());
                isTypingPart.setEntranceAnimation(true);
                LinearLayout contentLayout = (LinearLayout) View.inflate(getActivity().getApplicationContext(), R.layout.intercomsdk_blocks_admin_layout, (ViewGroup) null);
                contentLayout.addView(new AdminIsTypingView(getActivity().getApplicationContext(), event.getAdminId(), this.conversationId, isTypingPart.getId()));
                isTypingPart.setLayout(contentLayout);
                this.conversationParts.add(this.conversationParts.isEmpty() ? 0 : this.conversationParts.size(), isTypingPart);
                this.adapter.notifyDataSetChanged();
                this.currentTypers.add(event.getAdminId());
                if (this.conversationList.isAtBottom()) {
                    this.conversationList.smoothScrollToPosition(this.conversationList.getBottom());
                }
            }
        }
    }

    @Subscribe
    public void adminIsntTyping(AdminTypingEndedEvent event) {
        int position = positionOfTempPart(this.conversationParts.size() - 1, event.getPartId());
        if (position >= 0) {
            this.currentTypers.remove(event.getAdminId());
            this.conversationParts.remove(position);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void sendMessage(SendingEvent event) {
        if (event.isAttachment()) {
            attachmentTapped();
        } else {
            sendTapped();
        }
    }

    private class LoadFileTask extends AsyncTask<Object, Void, AttachmentData> {
        private LoadFileTask() {
        }

        /* access modifiers changed from: protected */
        public AttachmentData doInBackground(Object... params) {
            return new AttachmentData(params[0], params[1], ConversationFragment.this.getActivity());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(AttachmentData data) {
            UploadProgressListener listener;
            String id = data.getId();
            if (id.isEmpty()) {
                Part message = ConversationFragment.this.createAttachmentBlock(data.getFileName(), data.getPath(), data.getMimeType(), data.getImageWidth(), data.getImageHeight(), data.getUri());
                ConversationFragment.this.spool.playReplySendingSound();
                id = message.getId();
                listener = (UploadProgressListener) message.getLayout().getChildAt(0);
            } else {
                int i = ConversationFragment.this.positionOfTempPart(ConversationFragment.this.conversationParts.size() - 1, id);
                if (i >= 0) {
                    listener = (UploadProgressListener) ((Part) ConversationFragment.this.conversationParts.get(i)).getLayout().getChildAt(0);
                } else {
                    listener = new UploadProgressListener() {
                        public void uploadNotice(byte percentUploaded) {
                        }
                    };
                }
            }
            Bridge.getApi().uploadFile(data.getFile(), data.getFileName(), data.getSize(), data.getMimeType(), data.getImageWidth(), data.getImageHeight(), ConversationFragment.this.conversationParts.size() - 1, id, listener, ConversationFragment.this.getActivity());
        }
    }
}
