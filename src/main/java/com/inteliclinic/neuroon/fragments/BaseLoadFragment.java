package com.inteliclinic.neuroon.fragments;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

public abstract class BaseLoadFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Void> {
    /* access modifiers changed from: protected */
    public abstract void fillView();

    /* access modifiers changed from: protected */
    public abstract int getUniqueId();

    /* access modifiers changed from: protected */
    public abstract void loadInBackground();

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(getUniqueId(), (Bundle) null, this).forceLoad();
    }

    public android.content.Loader<Void> onCreateLoader(int id, Bundle args) {
        return new Loader(getActivity().getApplicationContext());
    }

    public void onLoadFinished(android.content.Loader<Void> loader, Void data) {
        fillView();
    }

    public void onLoaderReset(android.content.Loader<Void> loader) {
    }

    public class Loader extends AsyncTaskLoader<Void> {
        public Loader(Context context) {
            super(context);
        }

        public Void loadInBackground() {
            BaseLoadFragment.this.loadInBackground();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onStartLoading() {
            if (takeContentChanged()) {
                forceLoad();
            }
        }

        /* access modifiers changed from: protected */
        public void onStopLoading() {
            cancelLoad();
        }
    }
}
