package com.inteliclinic.neuroon.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Airport;
import java.util.List;

public class AirportLoader extends AsyncTaskLoader<List<Airport>> {
    private final String filterText;
    private final int limit;
    private List<Airport> mData;

    public AirportLoader(Context context, int limit2, String filterText2) {
        super(context);
        this.limit = limit2;
        this.filterText = filterText2;
    }

    public List<Airport> loadInBackground() {
        return DataManager.getInstance().getAirportsWithIata(this.limit, this.filterText);
    }

    public void deliverResult(List<Airport> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }
        List<Airport> oldData = this.mData;
        this.mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        if (this.mData != null) {
            deliverResult(this.mData);
        }
        if (takeContentChanged() || this.mData == null) {
            forceLoad();
        }
    }

    /* access modifiers changed from: protected */
    public void onStopLoading() {
        cancelLoad();
    }

    /* access modifiers changed from: protected */
    public void onReset() {
        onStopLoading();
        if (this.mData != null) {
            releaseResources(this.mData);
            this.mData = null;
        }
    }

    public void onCanceled(List<Airport> data) {
        super.onCanceled(data);
        releaseResources(data);
    }

    private void releaseResources(List<Airport> list) {
    }
}
