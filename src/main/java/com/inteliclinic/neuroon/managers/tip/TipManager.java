package com.inteliclinic.neuroon.managers.tip;

import android.content.Context;
import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.managers.context.ContextManager;
import com.inteliclinic.neuroon.managers.context.ContextUpdatedEvent;
import com.inteliclinic.neuroon.managers.context.IContextManager;
import com.inteliclinic.neuroon.models.data.Tip;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class TipManager extends BaseManager implements ITipManager {
    private static TipManager mInstance;
    private List<Pair<Float, Tip>> mCurrentTips;
    private final Comparator<Pair<Float, Tip>> mPairComparator = new Comparator<Pair<Float, Tip>>() {
        public int compare(Pair<Float, Tip> lhs, Pair<Float, Tip> rhs) {
            int i = ((Float) rhs.first).compareTo((Float) lhs.first);
            if (i != 0) {
                return i;
            }
            if (((Tip) rhs.second).getLastShownDate() == null || ((Tip) lhs.second).getLastShownDate() == null) {
                return 0;
            }
            return ((Tip) lhs.second).getLastShownDate().compareTo(((Tip) rhs.second).getLastShownDate());
        }
    };

    private TipManager() {
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new ManagerStartedEvent(this));
    }

    public static ITipManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TipManager();
        }
        return mInstance;
    }

    public static ITipManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new NullPointerException();
    }

    private Map<String, Float> convertFileTagsToTags(ArrayList<Map<String, Object>> list) {
        Map<String, Float> tags = new HashMap<>(list.size());
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> tag = it.next();
            tags.put((String) tag.get("TagKeyName"), Float.valueOf(((Double) tag.get("TagKeyValue")).floatValue()));
        }
        return tags;
    }

    public void onEvent(ContextUpdatedEvent event) {
        getCurrentTips(event.getContext());
    }

    public String getLucidDelegateKey() {
        return "tip-manager";
    }

    public void updateLastUseDate(Date date, Tip tip) {
        if (tip != null) {
            tip.setDate(date);
            DataManager.getInstance().saveTip(tip, false);
        }
    }

    public Tip getBestTip() {
        if (this.mCurrentTips == null || this.mCurrentTips.isEmpty()) {
            return null;
        }
        for (Pair<Float, Tip> currentTip : this.mCurrentTips) {
            Date lastShownDate = ((Tip) currentTip.second).getLastShownDate();
            if (lastShownDate == null) {
                lastShownDate = new Date(0);
            }
            long diff = (new Date().getTime() - lastShownDate.getTime()) / 60000;
            if (diff > 1) {
                Object unused = currentTip.first = Float.valueOf((((double) (diff / 10)) > 1.0d ? 1.0f : (float) (diff / 10)) + ((Float) currentTip.first).floatValue());
            }
        }
        Collections.sort(this.mCurrentTips, this.mPairComparator);
        Tip bestTip = (Tip) this.mCurrentTips.get(0).second;
        if (bestTip != null) {
            updateLastUseDate(new Date(), bestTip);
        }
        return bestTip;
    }

    public void start(Context context) {
        getCurrentTips(ContextManager.getInstance());
    }

    private void getCurrentTips(IContextManager context) {
        List<Pair<Float, Tip>> elements = new ArrayList<>();
        List<Tip> tips = DataManager.getInstance().getTips();
        Map<String, Float> tags = context.getTags();
        for (Tip tip : tips) {
            float value = 0.0f;
            for (String key : tags.keySet()) {
                Float tipValue = tip.getTags().get(key);
                if (tipValue != null) {
                    value = (float) (((double) value) + 0.1d);
                    if (tipValue.floatValue() > tags.get(key).floatValue()) {
                        value += tipValue.floatValue() - tags.get(key).floatValue();
                    }
                } else {
                    value = (float) (((double) value) - 0.05d);
                }
            }
            if (value != 0.0f) {
                elements.add(new Pair(Float.valueOf(value), tip));
            }
        }
        Collections.sort(elements, this.mPairComparator);
        if (elements.size() > 3) {
            elements = elements.subList(0, 3);
        }
        saveCurrentTips(elements);
    }

    private void saveCurrentTips(List<Pair<Float, Tip>> elements) {
        if (this.mCurrentTips == null) {
            this.mCurrentTips = new ArrayList();
        } else {
            this.mCurrentTips.clear();
        }
        this.mCurrentTips.addAll(elements);
    }

    private static class Pair<T, Y> {
        /* access modifiers changed from: private */
        public T first;
        /* access modifiers changed from: private */
        public Y second;

        Pair(T first2, Y second2) {
            this.first = first2;
            this.second = second2;
        }
    }
}
