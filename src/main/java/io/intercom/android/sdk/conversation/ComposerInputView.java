package io.intercom.android.sdk.conversation;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.conversation.events.SendingEvent;

public class ComposerInputView extends LinearLayout implements View.OnFocusChangeListener, View.OnClickListener {
    protected ImageButton attachmentButton;
    protected EditText messageTextBox;
    protected ImageButton sendButton = ((ImageButton) findViewById(R.id.send_button));
    protected ComposerTextWatcher textWatcher;

    public ComposerInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.intercomsdk_composer_input_layout, this);
        this.sendButton.setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
        this.attachmentButton = (ImageButton) findViewById(R.id.attachment_button);
        this.attachmentButton.setColorFilter(context.getResources().getColor(R.color.intercomsdk_attachment_color));
        this.messageTextBox = (EditText) findViewById(R.id.input_text);
        this.messageTextBox.setOnFocusChangeListener(this);
        this.sendButton.setOnClickListener(this);
        this.attachmentButton.setOnClickListener(this);
        this.textWatcher = new ComposerTextWatcher(this.sendButton, this.attachmentButton, Bridge.getIdentityStore().getIntercomId());
        this.messageTextBox.addTextChangedListener(this.textWatcher);
    }

    public void onFocusChange(View view, boolean hasFocus) {
        if (view.getId() == R.id.input_text && !hasFocus) {
            ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.send_button) {
            Bridge.getBus().post(new SendingEvent(false));
        } else {
            Bridge.getBus().post(new SendingEvent(true));
        }
    }

    /* access modifiers changed from: protected */
    public void setConversationId(String conversationId) {
        this.textWatcher.setConversationId(conversationId);
    }

    /* access modifiers changed from: protected */
    public String getTrimmedText() {
        return this.messageTextBox.getText().toString().trim();
    }

    /* access modifiers changed from: protected */
    public void hideKeyboard() {
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.messageTextBox.getWindowToken(), 0);
    }

    /* access modifiers changed from: protected */
    public void setHint(int hintRes) {
        this.messageTextBox.setHint(hintRes);
    }

    /* access modifiers changed from: protected */
    public void requestInputFocus() {
        this.messageTextBox.requestFocus();
    }

    /* access modifiers changed from: protected */
    public void clear() {
        this.messageTextBox.setText("");
    }

    /* access modifiers changed from: protected */
    public void cleanup() {
        this.messageTextBox.removeTextChangedListener(this.textWatcher);
        clear();
        this.textWatcher.cleanup();
    }
}
