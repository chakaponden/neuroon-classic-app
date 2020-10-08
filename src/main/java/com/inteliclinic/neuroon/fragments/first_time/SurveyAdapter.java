package com.inteliclinic.neuroon.fragments.first_time;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.util.ArrayList;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SleepHabitsViewHolder> {
    /* access modifiers changed from: private */
    public List<Pair<String, Boolean>> questions;

    public SurveyAdapter(List<Pair<String, Boolean>> questions2) {
        this.questions = questions2;
    }

    public SleepHabitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item, parent, false);
        final SleepHabitsViewHolder sleepHabitsViewHolder = new SleepHabitsViewHolder(inflate);
        inflate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sleepHabitsViewHolder.toggleSelected();
                SurveyAdapter.this.questions.set(sleepHabitsViewHolder.mQuestionId, new Pair(((Pair) SurveyAdapter.this.questions.get(sleepHabitsViewHolder.mQuestionId)).first, Boolean.valueOf(sleepHabitsViewHolder.mIsSelected)));
            }
        });
        return sleepHabitsViewHolder;
    }

    public void onBindViewHolder(SleepHabitsViewHolder holder, int position) {
        Pair<String, Boolean> s = this.questions.get(position);
        holder.mTextView.setText((CharSequence) s.first);
        holder.setSelected(((Boolean) s.second).booleanValue());
        int unused = holder.mQuestionId = holder.getAdapterPosition();
    }

    public List<Boolean> getResults() {
        ArrayList<Boolean> objects = new ArrayList<>(this.questions.size());
        for (Pair<String, Boolean> question : this.questions) {
            objects.add(question.second);
        }
        return objects;
    }

    public int getItemCount() {
        return this.questions.size();
    }

    public class SleepHabitsViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        /* access modifiers changed from: private */
        public boolean mIsSelected;
        /* access modifiers changed from: private */
        public int mQuestionId;
        ThinTextView mTextView;

        public SleepHabitsViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView.findViewById(R.id.imageview);
            this.mTextView = (ThinTextView) itemView.findViewById(R.id.text);
        }

        public void setSelected(boolean selected) {
            this.mIsSelected = selected;
            this.mImageView.setSelected(this.mIsSelected);
        }

        public void toggleSelected() {
            setSelected(!this.mIsSelected);
        }
    }
}
