package com.inteliclinic.neuroon.activities;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class ReadLogsActivity$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final ReadLogsActivity target, Object source) {
        target.text = (TextView) finder.findRequiredView(source, R.id.text, "field 'text'");
        target.logsSpinner = (Spinner) finder.findRequiredView(source, R.id.logs_spinner, "field 'logsSpinner'");
        View view = finder.findRequiredView(source, R.id.button, "field 'button' and method 'onReloadClick'");
        target.button = (Button) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onReloadClick();
            }
        });
    }

    public static void reset(ReadLogsActivity target) {
        target.text = null;
        target.logsSpinner = null;
        target.button = null;
    }
}
