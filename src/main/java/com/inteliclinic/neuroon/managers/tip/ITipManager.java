package com.inteliclinic.neuroon.managers.tip;

import android.content.Context;
import com.inteliclinic.neuroon.models.data.Tip;

public interface ITipManager {
    Tip getBestTip();

    void start(Context context);
}
