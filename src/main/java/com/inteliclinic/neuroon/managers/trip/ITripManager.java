package com.inteliclinic.neuroon.managers.trip;

import com.inteliclinic.neuroon.managers.trip.models.CurrentTrip;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.network.Flight;
import java.util.Date;

public interface ITripManager {
    void clearTrip();

    CurrentTrip getCurrentTrip();

    void saveTrip();

    void updateTrip(int i);

    void updateTrip(Airport airport, Airport airport2, Date date);

    void updateTrip(Flight flight);

    void updateTrip(String str, String str2);

    void updateTrip(boolean z);

    void updateTrip(String[] strArr);
}
