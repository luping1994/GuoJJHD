package net.suntrans.guojjhd.api;


import net.suntrans.guojjhd.bean.DeviceEntity;
import net.suntrans.guojjhd.bean.EnergyEntity;
import net.suntrans.guojjhd.bean.EnvEntity;
import net.suntrans.guojjhd.bean.LoginEntity;
import net.suntrans.guojjhd.bean.RoomEntity;

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
    @POST("api/v1/login/token")
    Observable<LoginEntity> login(@Field("username") String username, @Field("password") String password);

    @POST("api/v1/pad/home/environment")
    Observable<EnvEntity> getEnvData();

    @POST("api/v1/pad/home/energy")
    Observable<EnergyEntity> getEnergyData();

    @POST("api/v1/pad/home/house")
    Observable<RoomEntity> getRoom();

    @POST("api/v1/pad/home/light")
    Observable<DeviceEntity> getLight();
}
