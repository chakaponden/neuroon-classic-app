package io.intercom.android.sdk.Inbox;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.Participant;
import io.intercom.android.sdk.utilities.AvatarUtils;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.android.sdk.utilities.NameUtils;
import io.intercom.android.sdk.utilities.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConversationsListAdapter extends ArrayAdapter<Conversation> {
    private static int CONVERSATION_TYPE = 0;
    private static int LOADING_TYPE = 1;
    private Holder holder;
    private int rowResourceID;

    public ConversationsListAdapter(Context context, int textViewResourceId, List<Conversation> conversationListArray) {
        super(context, textViewResourceId, conversationListArray);
        this.rowResourceID = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder2;
        int type = getItemViewType(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (type == LOADING_TYPE) {
            View convertView2 = inflater.inflate(R.layout.intercomsdk_row_loading, parent, false);
            ((ProgressBar) convertView2.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()), PorterDuff.Mode.SRC_IN);
            convertView2.setMinimumHeight(parent.getMeasuredHeight());
            return convertView2;
        }
        Conversation conversation = (Conversation) getItem(position);
        if (convertView == null) {
            holder2 = new Holder();
            convertView = inflater.inflate(this.rowResourceID, parent, false);
            holder2.description = (TextView) convertView.findViewById(R.id.description);
            holder2.authorName = (TextView) convertView.findViewById(R.id.name);
            holder2.timeLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            holder2.avatar = (ImageView) convertView.findViewById(R.id.avatarView);
            holder2.unreadImageView = (ImageView) convertView.findViewById(R.id.unreadImageView);
            convertView.setTag(holder2);
            FontUtils.setTypeface(holder2.authorName, FontUtils.ROBOTO_MEDIUM, getContext());
        } else {
            holder2 = (Holder) convertView.getTag();
        }
        holder2.description.setText(conversation.getLastPart().getSummary());
        Participant admin = conversation.getLastAdmin();
        if ((admin instanceof Participant.NullParticipant) || !admin.hasDisplayName()) {
            updateAvatar(holder2, new Participant());
            holder2.authorName.setText(getContext().getResources().getString(R.string.intercomsdk_you));
        } else {
            updateAvatar(holder2, conversation.getLastAdmin(), conversation.getParticipants());
        }
        holder2.timeLabel.setText(new TimeUtils().getFormattedTime(conversation.getLastPart().getCreatedAt(), getContext()));
        if (!conversation.getRead().booleanValue()) {
            holder2.authorName.setTextColor(getContext().getResources().getColor(R.color.intercomsdk_text_light_black));
            holder2.unreadImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.intercomsdk_circle));
            holder2.unreadImageView.setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder2.authorName.setTextColor(getContext().getResources().getColor(R.color.intercomsdk_text_light_black));
            holder2.unreadImageView.setImageDrawable((Drawable) null);
        }
        return convertView;
    }

    private void updateAvatar(Holder holder2, Participant user) {
        holder2.authorName.setText(user.getDisplayName().split(" ")[0]);
        AvatarUtils.createAvatar(user.isAdmin(), user.getAvatar(), holder2.avatar, getContext());
    }

    private void updateAvatar(Holder holder2, Participant admin, Map<String, Participant> involving) {
        List<String> names = new ArrayList<>();
        names.add(admin.getDisplayName());
        for (Map.Entry<String, Participant> entry : involving.entrySet()) {
            Participant p = (Participant) entry.getValue();
            if (p.isAdmin() && !p.getId().equals(admin.getId())) {
                names.add(p.getDisplayName());
            }
        }
        holder2.authorName.setText(NameUtils.getFormattedAdmins(names, getContext()));
        AvatarUtils.createAvatar(admin.isAdmin(), admin.getAvatar(), holder2.avatar, getContext());
    }

    private class Holder {
        public TextView authorName;
        public ImageView avatar;
        public TextView description;
        public TextView timeLabel;
        public ImageView unreadImageView;

        private Holder() {
        }
    }

    public int getViewTypeCount() {
        return 3;
    }

    public int getItemViewType(int position) {
        int type = CONVERSATION_TYPE;
        if (getItem(position) instanceof Conversation.Loading) {
            return LOADING_TYPE;
        }
        return type;
    }

    public boolean isEnabled(int position) {
        return !(getItem(position) instanceof Conversation.Loading);
    }

    public boolean areAllItemsEnabled() {
        return true;
    }
}
