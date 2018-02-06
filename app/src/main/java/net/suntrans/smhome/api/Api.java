package net.suntrans.smhome.api;


import net.suntrans.smhome.bean.DeviceEntity;
import net.suntrans.smhome.bean.EnergyEntity;
import net.suntrans.smhome.bean.EnvEntity;
import net.suntrans.smhome.bean.HomeSceneBean;
import net.suntrans.smhome.bean.LoginEntity;
import net.suntrans.smhome.bean.NormalInfo;
import net.suntrans.smhome.bean.RespondBody;
import net.suntrans.smhome.bean.RoomDetailEntity;
import net.suntrans.smhome.bean.RoomEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Looney on 2017/1/4.
 */

public interface Api {

    /**
     * 登录api
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/login/token")
    Observable<LoginEntity> login(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("client_id") String client_id,
                                  @Field("client_secret") String client_secret,
                                  @Field("grant_type") String grant_type);

    @POST("api/v2/pad/home/sensus")
    Observable<EnvEntity> getEnvData();

    @POST("api/v2/pad/home/energy")
    Observable<EnergyEntity> getEnergyData();

    @POST("api/v2/pad/home/house")
    Observable<RoomEntity> getRoom();

    @POST("api/v2/pad/home/light")
    Observable<DeviceEntity> getLight();

    @FormUrlEncoded
    @POST("api/v2/pad/house/detail")
    Observable<RoomDetailEntity> getRoomDevices(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/v2/workman/switch/channelbyid")
    Observable<RespondBody> sendCmd(@Field("id") String id, @Field("cmd") String cmd);

    @POST("api/v2/pad/home/normal")
    Observable<NormalInfo> getNormal();

    @POST("api/v2/pad/home/scene")
    Observable<HomeSceneBean> getHomeScene();

    @FormUrlEncoded
    @POST("api/v2/workman/switch/scene")
    Observable<RespondBody> conScene(@Field("scene_id") String id);
}
