package net.suntrans.guojjhd.bean;

/**
 * Created by Looney on 2017/11/9.
 * Des:
 */

public class EnergyEntity extends RespondBody<EnergyEntity.DataBean>{



    public static class DataBean {
        /**
         * power : 2.10
         * yesterday : 1.1
         * today : 1.2
         * month : 198.2
         */

        public String power;
        public String yesterday;
        public String today;
        public String month;
    }
}
