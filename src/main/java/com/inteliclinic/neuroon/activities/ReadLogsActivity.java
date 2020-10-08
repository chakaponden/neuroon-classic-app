package com.inteliclinic.neuroon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.mask.MaskConnectedEvent;
import com.inteliclinic.neuroon.mask.MaskLogger;
import de.greenrobot.event.EventBus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReadLogsActivity extends AppCompatActivity {
    private static final String TAG = ReadLogsActivity.class.getSimpleName();
    @InjectView(2131755120)
    Button button;
    @InjectView(2131755119)
    Spinner logsSpinner;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mLogFileNames;
    /* access modifiers changed from: private */
    public List<File> mLogFiles;
    /* access modifiers changed from: private */
    public File mPickedFile;
    private Timer mTimer;
    @InjectView(2131755121)
    TextView text;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_read_logs);
        ButterKnife.inject((Activity) this);
        this.logsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                File unused = ReadLogsActivity.this.mPickedFile = (File) ReadLogsActivity.this.mLogFiles.get((int) id);
                ReadLogsActivity.this.showLog(ReadLogsActivity.this.mPickedFile);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                File unused = ReadLogsActivity.this.mPickedFile = null;
                ReadLogsActivity.this.text.setText(0);
            }
        });
        loadSpinnerData();
    }

    @OnClick({2131755120})
    public void onReloadClick() {
        loadSpinnerData();
        if (this.mPickedFile != null) {
            showLog(this.mPickedFile);
        }
        stopTimer();
        startTimer();
    }

    /* access modifiers changed from: private */
    public void showLog(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                final StringBuilder stringBuilder = new StringBuilder();
                while (true) {
                    String receiveString = bufferedReader.readLine();
                    if (receiveString == null) {
                        break;
                    }
                    stringBuilder.append(receiveString);
                    stringBuilder.append("\n");
                }
                inputStream.close();
                if (this.text != null) {
                    this.text.post(new Runnable() {
                        public void run() {
                            ReadLogsActivity.this.text.setText(stringBuilder.toString());
                        }
                    });
                }
            }
        } catch (FileNotFoundException | IOException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        startTimer();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void startTimer() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new ShowLogTask(), 500, 500);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        stopTimer();
        super.onPause();
    }

    public void onEventMainThread(MaskConnectedEvent event) {
        loadSpinnerData();
    }

    private void stopTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    private void loadSpinnerData() {
        this.mLogFiles = MaskLogger.getLogFiles();
        this.mLogFileNames = parseFiles(this.mLogFiles);
        this.mArrayAdapter = new ArrayAdapter<>(this, 17367049, this.mLogFileNames);
        if (this.logsSpinner != null) {
            this.logsSpinner.setAdapter(this.mArrayAdapter);
        }
    }

    private List<String> parseFiles(List<File> files) {
        List<String> fileName = new ArrayList<>();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            fileName.add(file.getName().substring(0, file.getName().length() - 4));
        }
        return fileName;
    }

    public class ShowLogTask extends TimerTask {
        public ShowLogTask() {
        }

        public void run() {
            if (ReadLogsActivity.this.mPickedFile != null) {
                ReadLogsActivity.this.showLog(ReadLogsActivity.this.mPickedFile);
            }
        }
    }
}
