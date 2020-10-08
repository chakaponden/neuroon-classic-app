package com.inteliclinic.neuroon.managers.network;

import com.inteliclinic.lucid.data.Configuration;
import com.inteliclinic.neuroon.managers.network.callbacks.BaseCallback;
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
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;

public interface INeuroonApi {
    @POST("/users/password-change-requests/")
    void changeUserPassword(@Body ChangePasswordModel changePasswordModel, BaseCallback baseCallback);

    @GET("/airporst/{icao}/weather/")
    void getAirportWeather(@Path("icao") String str, BaseCallback<Weather> baseCallback);

    @GET("/airports/radius-search/")
    void getAirports(@Query("radius") float f, @Query("lat") float f2, @Query("long") float f3, BaseCallback<List<Airport>> baseCallback);

    @GET("/airports/{city_name}/")
    void getAirports(@Path("city_name") String str, BaseCallback<List<Airport>> baseCallback);

    @GET("/app-configs/{version}/")
    void getAppConfig(@Path("version") int i, BaseCallback<Configuration> baseCallback);

    @GET("/app-configs/")
    void getAppConfigs(@Query("min_version") int i, @Query("lucid_version") long j, @Query("offset") int i2, @Query("limit") int i3, BaseCallback<List<Configuration>> baseCallback);

    @GET("/flights/{date}/{id}/status/")
    void getFlightStatus(@Path("date") Date date, @Path("id") String str, BaseCallback<FlightStatus> baseCallback);

    @GET("/flights/{year}-{month}-{day}/")
    void getFlights(@Path("year") int i, @Path("month") String str, @Path("day") String str2, @Query("departureICAO") String str3, @Query("arrivalICAO") String str4, BaseCallback<List<Flight>> baseCallback);

    @GET("/flights/{flight_code}/{year}-{month}-{day}/")
    void getFlights(@Path("flight_code") String str, @Path("year") int i, @Path("month") String str2, @Path("day") String str3, BaseCallback<List<Flight>> baseCallback);

    @GET("/mask-firmwares/{id}/data/")
    void getMaskFirmware(@Path("id") int i, BaseCallback<Response> baseCallback);

    @GET("/mask-firmwares/")
    void getMaskFirmwares(@Query("limit") int i, BaseCallback<List<MaskFirmwareMeta>> baseCallback);

    @GET("/mask-status/")
    Response getMaskStatus(@Query("serial_number") String str);

    @GET("/flights/{reservation_name}/{reservation_code}/")
    void getReservation(@Path("reservation_name") String str, @Path("reservation_code") String str2, BaseCallback<Reservation> baseCallback);

    @GET("/sleep-recordings/{id}/data/")
    Response getSleepRecordingData(@Path("id") long j);

    @GET("/sleep-recordings/{id}/")
    SleepRecordingMeta getSleepRecordingMeta(@Path("id") long j);

    @GET("/sleep-recordings/")
    List<SleepRecordingMeta> getSleepRecordingMetas();

    @GET("/tip/{id}/")
    void getTip(@Path("id") int i, BaseCallback baseCallback);

    @GET("/tips/")
    void getTips(@Query("min_version") int i, @Query("tags") String str, BaseCallback<List<Tip>> baseCallback);

    @GET("/tips/")
    void getTips(@Query("min_version") int i, @Query("lang") String str, @Query("tags") String str2, BaseCallback<List<Tip>> baseCallback);

    @GET("/users/{id}/")
    void getUser(@Path("id") int i, BaseCallback<User> baseCallback);

    @GET("/users/current/")
    void getUser(BaseCallback<User> baseCallback);

    @GET("/user-configs/{version}/")
    void getUserConfig(@Path("version") int i, BaseCallback<Configuration> baseCallback);

    @GET("/user-configs/")
    void getUserConfigs(@Query("min_version") int i, @Query("offset") int i2, @Query("limit") int i3, BaseCallback<List<Object>> baseCallback);

    @GET("/user-configs/")
    void getUserConfigs(BaseCallback<List<Configuration>> baseCallback);

    @GET("/users/")
    void getUsers(BaseCallback<List<User>> baseCallback);

    @FormUrlEncoded
    @POST("/auth/token/")
    void login(@Query("grant_type") String str, @Field("username") String str2, @Field("password") String str3, BaseCallback<AccessToken> baseCallback);

    @PATCH("/sleep-recordings/{id}/")
    SleepRecordingMeta patchSleepRecordingMeta(@Path("id") long j, @Body SleepRecordingMeta sleepRecordingMeta);

    @POST("/sleep-recordings/{id}/data/")
    @Multipart
    Response postSleepRecordingData(@Path("id") long j, @Part("file") TypedInput typedInput);

    @POST("/sleep-recordings/")
    SleepRecordingMeta postSleepRecordingMeta(@Body SleepRecordingMeta sleepRecordingMeta);

    @PUT("/user-configs/{version}/")
    void postUserConfig(@Body Configuration configuration, @Path("version") int i, BaseCallback<Configuration> baseCallback);

    @FormUrlEncoded
    @POST("/auth/token/")
    AccessToken refresh(@Query("grant_type") String str, @Field("refresh_token") String str2);

    @POST("/users/register/")
    void register(@Body RegisterUser registerUser, BaseCallback<User> baseCallback);

    @POST("/users/password-reset-requests/")
    void resetUserPassword(@Body ResetPasswordUser resetPasswordUser, BaseCallback<String> baseCallback);

    @PATCH("/users/{id}/")
    void updateUser(@Path("id") int i, @Body User user, BaseCallback<User> baseCallback);
}
