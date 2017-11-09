package net.suntrans.guojjhd.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2017/11/9.
 * Des:
 */

public class EnvEntity extends RespondBody<EnvEntity.DataBean> {

    public static class DataBean {

        public int id;
        public String updated_at;
        public String name;
        public String image;
        public String dev_id;
        public String pm1;
        public String pm10;
        public String pm25;
        public String jiaquan;
        public String yanwu;
        public String wendu;
        public String shidu;
        public String renyuan;
        public String x_zhou;
        public String y_zhou;
        public String z_zhou;
        public String zhendong;
        public String guangzhao;
        public String daqiya;
        public String created_at;
    }
}
