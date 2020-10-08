package com.inteliclinic.neuroon.managers.context;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inteliclinic.lucid.events.NotifyTagEvent;
import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.managers.tip.Tags;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.Map;

public final class ContextManager extends BaseManager implements IContextManager {
    private static IContextManager mInstance;
    private final Context mContext;
    private Map<String, Float> mTags = new HashMap();

    private ContextManager(Context context) {
        this.mContext = context;
        EventBus.getDefault().post(new ManagerStartedEvent(this));
        EventBus.getDefault().register(this);
        lucidBroadcast(Tags.GENERAL, 1.0f);
    }

    public static IContextManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new NullPointerException();
    }

    public static IContextManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ContextManager(context);
        }
        return mInstance;
    }

    public void onEvent(NotifyTagEvent event) {
        checkDelayedReadTags();
        if (event.getValue().floatValue() == -1.0f) {
            this.mTags.remove(event.getKey());
        } else {
            this.mTags.put(event.getKey(), event.getValue());
        }
        saveContextToFile(this.mContext);
        EventBus.getDefault().post(new ContextUpdatedEvent(this));
    }

    private void checkDelayedReadTags() {
        if (this.mTags == null || this.mTags.size() == 0) {
            readContextFromFile(this.mContext);
        }
    }

    private void saveContextToFile(Context context) {
        context.getSharedPreferences("Context", 0).edit().putString("Tags", new Gson().toJson((Object) this.mTags)).commit();
    }

    private void readContextFromFile(Context context) {
        this.mTags = (Map) new Gson().fromJson(context.getSharedPreferences("Context", 0).getString("Tags", (String) null), new TypeToken<Map<String, Float>>() {
        }.getType());
        if (this.mTags == null) {
            this.mTags = new HashMap();
        }
    }

    public Map<String, Float> getTags() {
        checkDelayedReadTags();
        return this.mTags;
    }

    public String getLucidDelegateKey() {
        return "context-manager";
    }
}
