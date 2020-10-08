package com.inteliclinic.neuroon.managers.therapy;

import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.network.Flight;
import java.util.Date;
import java.util.List;

public interface ITherapyManager {
    void deleteTherapy();

    CurrentTherapy getCurrentTherapy();

    float getTherapyProgress();

    void planTherapy(float f, Date date, boolean z, float f2, Airport airport, Airport airport2, List<Flight> list);

    void updateTherapy();
}
