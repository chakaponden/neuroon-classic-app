package com.inteliclinic.neuroon.activities;

import android.os.Bundle;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.first_time.AboutYouFragment;
import com.inteliclinic.neuroon.fragments.first_time.DateOfBirthFragment;
import com.inteliclinic.neuroon.fragments.first_time.HeightFragment;
import com.inteliclinic.neuroon.fragments.first_time.PersonalDataFragment;
import com.inteliclinic.neuroon.fragments.first_time.SexFragment;
import com.inteliclinic.neuroon.fragments.first_time.WeightFragment;

public class FirstSetUpActivity extends BaseActivity implements PersonalDataFragment.OnFragmentInteractionListener, SexFragment.OnFragmentInteractionListener, DateOfBirthFragment.OnFragmentInteractionListener, HeightFragment.OnFragmentInteractionListener, WeightFragment.OnFragmentInteractionListener, AboutYouFragment.OnFragmentInteractionListener {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_first_set_up);
        getFragmentManager().beginTransaction().add(R.id.container, PersonalDataFragment.newInstance()).commit();
    }

    public void closeFragments() {
    }

    public void openAboutYou() {
        getFragmentManager().beginTransaction().replace(R.id.container, AboutYouFragment.newInstance()).addToBackStack((String) null).commit();
    }

    public void openWeight() {
        getFragmentManager().beginTransaction().replace(R.id.container, WeightFragment.newInstance()).addToBackStack((String) null).commit();
    }

    public void openHeight() {
        getFragmentManager().beginTransaction().replace(R.id.container, HeightFragment.newInstance()).addToBackStack((String) null).commit();
    }

    public void openDateOfBirth() {
        getFragmentManager().beginTransaction().replace(R.id.container, DateOfBirthFragment.newInstance()).addToBackStack((String) null).commit();
    }

    public void openSexFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, SexFragment.newInstance()).addToBackStack((String) null).commit();
    }
}
