package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.old_guava.Iterators;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.util.HashSet;
import java.util.Set;

public class MaskPickerFragment extends BaseFragment {
    @InjectView(2131755378)
    ImageView buttonBackground;
    private MyAdapter mAdapter;
    /* access modifiers changed from: private */
    public Set<BluetoothDevice> mDevices = new HashSet();
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private BluetoothDevice mPickedDevice;
    @InjectView(2131755376)
    RecyclerView mRecyclerView;
    @InjectView(2131755377)
    FrameLayout pair;

    public interface OnFragmentInteractionListener {
        void pickDevice(String str);
    }

    public static MaskPickerFragment newInstance(Set<BluetoothDevice> mDevices2) {
        MaskPickerFragment maskPickerFragment = new MaskPickerFragment();
        maskPickerFragment.mDevices.addAll(mDevices2);
        return maskPickerFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_mask_picker, container, false);
        ButterKnife.inject((Object) this, inflate);
        initList();
        this.buttonBackground.setEnabled(false);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("mask_select");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    private void initList() {
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this.mRecyclerView.getContext());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mAdapter = new MyAdapter();
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        this.mDevices.clear();
    }

    @OnClick({2131755377})
    public void onPairClick() {
        pairDevice();
    }

    private void pairDevice() {
        if (this.buttonBackground.isEnabled() && this.mListener != null && this.mPickedDevice != null) {
            this.mListener.pickDevice(this.mPickedDevice.getAddress());
        }
    }

    /* access modifiers changed from: private */
    public void setUpDevice(BluetoothDevice bluetoothDevice) {
        this.mPickedDevice = bluetoothDevice;
        if (bluetoothDevice != null) {
            this.buttonBackground.setEnabled(true);
        } else {
            this.buttonBackground.setEnabled(false);
        }
    }

    public void fillWithDevices(Set<BluetoothDevice> devices) {
        this.mDevices.addAll(devices);
        this.mAdapter = new MyAdapter();
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        /* access modifiers changed from: private */
        public int mSelectedItemPosition = -1;

        public MyAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mask_picker_item, parent, false));
        }

        public void onBindViewHolder(final ViewHolder holder, int position) {
            final BluetoothDevice bluetoothDevice = (BluetoothDevice) Iterators.get(MaskPickerFragment.this.mDevices.iterator(), position);
            String name = bluetoothDevice.getName();
            if ("NeuroOn".equals(name)) {
                holder.mTextView.setText(R.string.neuroon_device_name);
            } else {
                holder.mTextView.setText(name);
            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    holder.mProgress.setVisibility(0);
                    new Thread() {
                        public void run() {
                            MaskManager.getInstance().illuminateDevice(bluetoothDevice);
                            holder.mProgress.post(new Runnable() {
                                public void run() {
                                    holder.mProgress.setVisibility(4);
                                }
                            });
                        }
                    }.start();
                    int unused = MyAdapter.this.mSelectedItemPosition = holder.getAdapterPosition();
                    MyAdapter.this.notifyDataSetChanged();
                    MaskPickerFragment.this.setUpDevice(bluetoothDevice);
                }
            });
            if (position == this.mSelectedItemPosition) {
                holder.mView.setSelected(true);
            } else {
                holder.mView.setSelected(false);
            }
        }

        public int getItemCount() {
            return MaskPickerFragment.this.mDevices.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ProgressBar mProgress;
            ThinTextView mTextView;
            /* access modifiers changed from: private */
            public View mView;

            public ViewHolder(View v) {
                super(v);
                this.mView = v;
                this.mTextView = (ThinTextView) v.findViewById(R.id.device_title);
                this.mProgress = (ProgressBar) v.findViewById(R.id.progress);
            }
        }
    }
}
