package net.suntrans.bieshuhd.bean;

/**
 * Created by Looney on 2017/11/9.
 * Des:
 */

public class EnvEntity extends RespondBody<EnvEntity.DataBean> {

    public static class DataBean {


        /**
         * wendu : {"value":"23.85","text":"舒适","unit":"°C","color":"#00c847"}
         * shidu : {"value":"70.5","text":"舒适","unit":"%Rh","color":"#00ff00"}
         * jiaquan : {"value":"0.000","text":"清洁","unit":"ug/m³","color":"#2dc221"}
         * pm25 : {"value":"51.0","text":"轻度","unit":"ug/m³","color":"#2dc221"}
         */

        public EnvBean wendu;
        public EnvBean shidu;
        public EnvBean jiaquan;
        public EnvBean pm25;

        public static class EnvBean {
            /**
             * value : 23.85
             * text : 舒适
             * unit : °C
             * color : #00c847
             */

            public String value;
            public String text;
            public String unit;
            public String color;
        }


    }
}
