package com.inteliclinic.neuroon.models.data;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.old_guava.Strings;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tip extends BaseModel {
    @SerializedName("content")
    String content;
    int frequency;
    @SerializedName("db_id")
    long id;
    Date lastUseDate;
    @SerializedName("link")
    String link;
    List<String> mParameters;
    Map<String, Float> mTags;
    String params;
    @SerializedName("id")
    int serverId;
    String tags;
    @SerializedName("title")
    String title;
    int version;

    public void setDate(Date date) {
        this.lastUseDate = date;
    }

    public Map<String, Float> getTags() {
        if (this.mTags == null) {
            this.mTags = convertJsonTagsToTags();
        }
        if (this.mTags == null) {
            return new HashMap();
        }
        return this.mTags;
    }

    private Map<String, Float> convertJsonTagsToTags() {
        Map<String, Float> res = new HashMap<>();
        List<HashMap<String, Float>> maps = (List) new Gson().fromJson(this.tags, new TypeToken<List<HashMap<String, Float>>>() {
        }.getType());
        if (maps != null) {
            for (Map<String, Float> map : maps) {
                for (Map.Entry<String, Float> stringFloatEntry : map.entrySet()) {
                    res.put(stringFloatEntry.getKey(), stringFloatEntry.getValue());
                }
            }
        }
        return res;
    }

    public Date getLastShownDate() {
        return this.lastUseDate;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version2) {
        this.version = version2;
    }

    public void setServerId(int serverId2) {
        this.serverId = serverId2;
    }

    public String getContentRaw() {
        return this.content;
    }

    public String getContent() {
        getParameters();
        if (this.mParameters.size() > 0) {
            this.mParameters.remove("p-none");
            if (this.mParameters.size() > 0) {
                String[] parameters = new String[this.mParameters.size()];
                for (int i = 0; i < this.mParameters.size(); i++) {
                    parameters[i] = DataManager.getInstance().getParameterForKey(this.mParameters.get(i));
                    if ("".equals(parameters[i])) {
                        return null;
                    }
                }
                try {
                    return String.format(this.content, parameters);
                } catch (Exception ex) {
                    if (Crashlytics.getInstance() != null) {
                        Crashlytics.logException(ex);
                    }
                    return "";
                }
            }
        }
        return this.content;
    }

    private List<String> getParameters() {
        if (this.mParameters == null) {
            this.mParameters = (List) new Gson().fromJson(this.params, new TypeToken<List<String>>() {
            }.getType());
        }
        return this.mParameters;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean hasLink() {
        return !Strings.isNullOrEmpty(this.link) && !"none".equals(this.link);
    }

    public String getLink() {
        return this.link;
    }

    public static List<Tip> filterNotInDb(List<Tip> tipNetworks) {
        List<Tip> list = new ArrayList<>();
        for (Tip tip : tipNetworks) {
            if (DataManager.getInstance().getTipByServerId(tip.serverId) == null) {
                list.add(tip);
            }
        }
        return list;
    }
}
