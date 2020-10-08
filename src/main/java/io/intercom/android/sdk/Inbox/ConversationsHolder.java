package io.intercom.android.sdk.Inbox;

import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.ConversationList;
import io.intercom.android.sdk.models.Events.ConversationsListDataChanged;
import java.util.ArrayList;
import java.util.List;

public class ConversationsHolder extends ArrayList<Conversation> {
    /* access modifiers changed from: protected */
    public void syncInbox(ConversationList response) {
        List<Conversation> newConversations = new ArrayList<>();
        List<Conversation> removeConversations = new ArrayList<>();
        List tempConversations = response.getConversations();
        for (int i = 0; i < tempConversations.size(); i++) {
            Conversation tempConversation = tempConversations.get(i);
            boolean found = false;
            for (int j = 0; j < size(); j++) {
                Conversation currentConversation = (Conversation) get(j);
                if (!(currentConversation instanceof Conversation.Loading) && tempConversation.getId().equals(currentConversation.getId())) {
                    if (tempConversation.getLastPart().getCreatedAt() > currentConversation.getLastPart().getCreatedAt()) {
                        removeConversations.add(currentConversation);
                        newConversations.add(tempConversation);
                    }
                    found = true;
                }
            }
            if (!found) {
                newConversations.add(tempConversation);
            }
        }
        removeAll(removeConversations);
        if (newConversations.isEmpty() || isEmpty()) {
            addAll(0, newConversations);
        } else {
            int pos = 0;
            if (newConversations.get(0).getLastPart().getCreatedAt() <= ((Conversation) get(size() - 1)).getLastPart().getCreatedAt()) {
                pos = size();
            }
            addAll(pos, newConversations);
        }
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        Bridge.getBus().post(new ConversationsListDataChanged());
    }
}
