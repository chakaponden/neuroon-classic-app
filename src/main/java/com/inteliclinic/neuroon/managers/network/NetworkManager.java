package com.inteliclinic.neuroon.managers.network;

import android.util.Base64;
import com.inteliclinic.lucid.data.Configuration;
import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
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
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.net.Proxy;
import java.util.Date;
import java.util.List;
import java.util.Map;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.mime.TypedByteArray;

public final class NetworkManager extends BaseManager implements INetworkManager {
    private static final String CLIENT_ID = "Inltr9LtR4OMmZRv1BjpuO576o1IWbmrAB2eKnGo";
    private static final String CLIENT_SECRET = "egzGOfdaznnLAvsHNJdoOI9JYogkUaxZCJ7Ri86ziTMyUn4SoUTrQjueY97hAy58RnqXxkgc4lnRhbQn9GIZaBBFQYyG4k3uDRYVgtXBCcaIP7ZpBWEmFqEN7DZEyE1y";
    private static final String ENDPOINT = "https://api.neuroon.com/v1/";
    private static RestAdapter.Builder mBuilder;
    private static INetworkManager mInstance;

    public enum RequestKey {
        TIPS,
        FLIGHTS,
        APP_CONFIG,
        USER_CONFIG,
        MASK_FIRMWARE_CHECK,
        MASK_FIRMWARE
    }

    private NetworkManager() {
        mBuilder = new RestAdapter.Builder().setClient((Client) new OkClient(new OkHttpClient().setAuthenticator(new Authenticator() {
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                AccessToken accessToken;
                if (!response.request().header(HttpRequest.HEADER_AUTHORIZATION).startsWith("Basic") && (accessToken = AccountManager.getInstance().refreshToken()) != null && !accessToken.getAuthorization().equals(response.request().header(HttpRequest.HEADER_AUTHORIZATION))) {
                    return response.request().newBuilder().header(HttpRequest.HEADER_AUTHORIZATION, accessToken.getAuthorization()).build();
                }
                return null;
            }

            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                AccessToken accessToken;
                if (!response.request().header(HttpRequest.HEADER_AUTHORIZATION).startsWith("Basic") && (accessToken = AccountManager.getInstance().refreshToken()) != null && !accessToken.getAuthorization().equals(response.request().header(HttpRequest.HEADER_PROXY_AUTHORIZATION))) {
                    return response.request().newBuilder().header(HttpRequest.HEADER_PROXY_AUTHORIZATION, accessToken.getAuthorization()).build();
                }
                return null;
            }
        })));
        mBuilder.setEndpoint(ENDPOINT);
    }

    public static INetworkManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public static void setClient(Client client) {
        mBuilder.setClient(client);
    }

    private static <S> S createService(Class<S> serviceClass, String clientId, String clientSecret) {
        if (!(clientId == null || clientSecret == null)) {
            final String credentials = clientId + ":" + clientSecret;
            mBuilder.setRequestInterceptor(new RequestInterceptor() {
                public void intercept(RequestInterceptor.RequestFacade request) {
                    request.addHeader("Accept", "application/json");
                    request.addHeader(HttpRequest.HEADER_AUTHORIZATION, "Basic " + Base64.encodeToString(credentials.getBytes(), 2));
                }
            });
        }
        return mBuilder.build().create(serviceClass);
    }

    private static <S> S createSimpleService(Class<S> serviceClass, final AccessToken accessToken) {
        if (accessToken != null) {
            mBuilder.setRequestInterceptor(new RequestInterceptor() {
                public void intercept(RequestInterceptor.RequestFacade request) {
                    request.addHeader(HttpRequest.HEADER_AUTHORIZATION, accessToken.getAuthorization());
                }
            });
        }
        return mBuilder.build().create(serviceClass);
    }

    private static <S> S createService(Class<S> serviceClass, final AccessToken accessToken) {
        if (accessToken != null) {
            mBuilder.setRequestInterceptor(new RequestInterceptor() {
                public void intercept(RequestInterceptor.RequestFacade request) {
                    request.addHeader("Accept", "application/json");
                    request.addHeader(HttpRequest.HEADER_AUTHORIZATION, accessToken.getAuthorization());
                }
            });
        }
        return mBuilder.build().create(serviceClass);
    }

    public String getLucidDelegateKey() {
        return "network-manager";
    }

    public void login(String username, String password, LoginCallback callback) {
        ((INeuroonApi) createService(INeuroonApi.class, CLIENT_ID, CLIENT_SECRET)).login("password", username, password, callback);
    }

    public void getUser(AccessToken token, BaseCallback<User> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getUser(callback);
    }

    public AccessToken refreshToken(AccessToken accessToken) {
        return ((INeuroonApi) createService(INeuroonApi.class, CLIENT_ID, CLIENT_SECRET)).refresh("refresh_token", accessToken.getRefreshToken());
    }

    public void register(RegisterUser user, RegisterCallback callback) {
        ((INeuroonApi) createService(INeuroonApi.class, CLIENT_ID, CLIENT_SECRET)).register(user, callback);
    }

    public void resetPassword(ResetPasswordUser user, BaseCallback<String> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, CLIENT_ID, CLIENT_SECRET)).resetUserPassword(user, callback);
    }

    public void changePassword(AccessToken token, int userId, ChangePasswordModel passwords, BaseTokenCallback callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).changeUserPassword(passwords, callback);
    }

    public void getAppConfig(AccessToken token, int version, long lucidVersion, BaseTokenCallback<List<Configuration>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getAppConfigs(version, lucidVersion, 0, 1, callback);
    }

    public void getUserConfig(AccessToken token, int version, BaseTokenCallback<List<Object>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getUserConfigs(version, 0, 1, callback);
    }

    public void postUserConfig(AccessToken token, Configuration userConfig, int version, BaseTokenCallback<Configuration> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).postUserConfig(userConfig, version, callback);
    }

    public void getTips(AccessToken token, int minVersion, Map<String, Float> tags, BaseTokenCallback<List<Tip>> callback) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Float> item : tags.entrySet()) {
            builder.append(",").append(item.getKey()).append(":").append(item.getValue());
        }
        if (!tags.isEmpty()) {
            builder.deleteCharAt(0);
        }
        ((INeuroonApi) createService(INeuroonApi.class, token)).getTips(minVersion, builder.toString(), callback);
    }

    public void getTips(AccessToken token, int minVersion, String lang, Map<String, Float> tags, BaseTokenCallback<List<Tip>> callback) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Float> item : tags.entrySet()) {
            builder.append(",").append(item.getKey()).append(":").append(item.getValue());
        }
        if (!tags.isEmpty()) {
            builder.deleteCharAt(0);
        }
        ((INeuroonApi) createService(INeuroonApi.class, token)).getTips(minVersion, lang, builder.toString(), callback);
    }

    public void getMaskFirmwareMeta(AccessToken token, BaseTokenCallback<List<MaskFirmwareMeta>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getMaskFirmwares(1, callback);
    }

    public void getMaskFirmware(AccessToken token, int version, BaseTokenCallback<retrofit.client.Response> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getMaskFirmware(version, callback);
    }

    public retrofit.client.Response getMaskStatus(AccessToken token, String serialNumber) {
        return ((INeuroonApi) createService(INeuroonApi.class, token)).getMaskStatus(serialNumber);
    }

    public List<SleepRecordingMeta> getSleepMetas(AccessToken mAccessToken) {
        return ((INeuroonApi) createService(INeuroonApi.class, mAccessToken)).getSleepRecordingMetas();
    }

    public SleepRecordingMeta getSleepMeta(AccessToken mAccessToken, long id) {
        return ((INeuroonApi) createService(INeuroonApi.class, mAccessToken)).getSleepRecordingMeta(id);
    }

    public SleepRecordingMeta postSleepMeta(AccessToken token, SleepRecordingMeta sleepRecordingMeta) {
        return ((INeuroonApi) createService(INeuroonApi.class, token)).postSleepRecordingMeta(sleepRecordingMeta);
    }

    public SleepRecordingMeta patchSleepMeta(AccessToken accessToken, long id, SleepRecordingMeta sleepRecordingMeta) {
        return ((INeuroonApi) createService(INeuroonApi.class, accessToken)).patchSleepRecordingMeta(id, sleepRecordingMeta);
    }

    public retrofit.client.Response getSleepData(AccessToken token, long id) {
        return ((INeuroonApi) createService(INeuroonApi.class, token)).getSleepRecordingData(id);
    }

    public retrofit.client.Response postSleepData(AccessToken token, long id, TypedByteArray typedByteArray) {
        return ((INeuroonApi) createSimpleService(INeuroonApi.class, token)).postSleepRecordingData(id, typedByteArray);
    }

    public void getAirports(AccessToken token, String cityName, BaseTokenCallback<List<Airport>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getAirports(cityName, callback);
    }

    public void getAirports(AccessToken token, float radius, float longitude, float latitude, BaseTokenCallback<List<Airport>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getAirports(radius, latitude, longitude, callback);
    }

    public void getAirportWeather(AccessToken token, String airportCode, BaseTokenCallback<Weather> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getAirportWeather(airportCode, callback);
    }

    public void getFlights(AccessToken token, int year, String month, String day, String departureAirportCode, String arrivalAirportCode, BaseTokenCallback<List<Flight>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getFlights(year, month, day, departureAirportCode, arrivalAirportCode, callback);
    }

    public void getFlights(AccessToken token, String flightCode, int year, String month, String day, BaseTokenCallback<List<Flight>> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getFlights(flightCode, year, month, day, callback);
    }

    public void getReservation(AccessToken token, String reservationName, String reservationCode, BaseTokenCallback<Reservation> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getReservation(reservationName, reservationCode, callback);
    }

    public void getFlightStatus(AccessToken token, Date date, String flightId, BaseTokenCallback<FlightStatus> callback) {
        ((INeuroonApi) createService(INeuroonApi.class, token)).getFlightStatus(date, flightId, callback);
    }
}
