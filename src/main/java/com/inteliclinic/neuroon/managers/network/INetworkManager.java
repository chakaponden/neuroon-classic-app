package com.inteliclinic.neuroon.managers.network;

import com.inteliclinic.lucid.data.Configuration;
import com.inteliclinic.neuroon.managers.network.callbacks.BaseCallback;
import com.inteliclinic.neuroon.managers.network.callbacks.BaseTokenCallback;
import com.inteliclinic.neuroon.managers.network.callbacks.LoginCallback;
import com.inteliclinic.neuroon.managers.network.callbacks.RegisterCallback;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.Tip;
import com.inteliclinic.neuroon.models.network.AccessToken;
import com.inteliclinic.neuroon.models.network.ChangePasswordModel;
import com.inteliclinic.neuroon.models.network.Flight;
import com.inteliclinic.neuroon.models.network.FlightStatus;
import com.inteliclinic.neuroon.models.network.MaskFirmwareMeta;
import com.inteliclinic.neuroon.models.network.RegisterUser;
import com.inteliclinic.neuroon.models.network.Reservation;
import com.inteliclinic.neuroon.models.network.ResetPasswordUser;
import com.inteliclinic.neuroon.models.network.SleepRecordingMeta;
import com.inteliclinic.neuroon.models.network.User;
import com.inteliclinic.neuroon.models.network.Weather;
import java.util.Date;
import java.util.List;
import java.util.Map;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public interface INetworkManager {
    void changePassword(AccessToken accessToken, int i, ChangePasswordModel changePasswordModel, BaseTokenCallback baseTokenCallback);

    void getAirportWeather(AccessToken accessToken, String str, BaseTokenCallback<Weather> baseTokenCallback);

    void getAirports(AccessToken accessToken, float f, float f2, float f3, BaseTokenCallback<List<Airport>> baseTokenCallback);

    void getAirports(AccessToken accessToken, String str, BaseTokenCallback<List<Airport>> baseTokenCallback);

    void getAppConfig(AccessToken accessToken, int i, long j, BaseTokenCallback<List<Configuration>> baseTokenCallback);

    void getFlightStatus(AccessToken accessToken, Date date, String str, BaseTokenCallback<FlightStatus> baseTokenCallback);

    void getFlights(AccessToken accessToken, int i, String str, String str2, String str3, String str4, BaseTokenCallback<List<Flight>> baseTokenCallback);

    void getFlights(AccessToken accessToken, String str, int i, String str2, String str3, BaseTokenCallback<List<Flight>> baseTokenCallback);

    void getMaskFirmware(AccessToken accessToken, int i, BaseTokenCallback<Response> baseTokenCallback);

    void getMaskFirmwareMeta(AccessToken accessToken, BaseTokenCallback<List<MaskFirmwareMeta>> baseTokenCallback);

    Response getMaskStatus(AccessToken accessToken, String str);

    void getReservation(AccessToken accessToken, String str, String str2, BaseTokenCallback<Reservation> baseTokenCallback);

    Response getSleepData(AccessToken accessToken, long j);

    SleepRecordingMeta getSleepMeta(AccessToken accessToken, long j);

    List<SleepRecordingMeta> getSleepMetas(AccessToken accessToken);

    void getTips(AccessToken accessToken, int i, String str, Map<String, Float> map, BaseTokenCallback<List<Tip>> baseTokenCallback);

    void getTips(AccessToken accessToken, int i, Map<String, Float> map, BaseTokenCallback<List<Tip>> baseTokenCallback);

    void getUser(AccessToken accessToken, BaseCallback<User> baseCallback);

    void getUserConfig(AccessToken accessToken, int i, BaseTokenCallback<List<Object>> baseTokenCallback);

    void login(String str, String str2, LoginCallback loginCallback);

    SleepRecordingMeta patchSleepMeta(AccessToken accessToken, long j, SleepRecordingMeta sleepRecordingMeta);

    Response postSleepData(AccessToken accessToken, long j, TypedByteArray typedByteArray);

    SleepRecordingMeta postSleepMeta(AccessToken accessToken, SleepRecordingMeta sleepRecordingMeta);

    void postUserConfig(AccessToken accessToken, Configuration configuration, int i, BaseTokenCallback<Configuration> baseTokenCallback);

    AccessToken refreshToken(AccessToken accessToken);

    void register(RegisterUser registerUser, RegisterCallback registerCallback);

    void resetPassword(ResetPasswordUser resetPasswordUser, BaseCallback<String> baseCallback);
}
