package net.suntrans.smhome.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class NormalInfo extends RespondBody<NormalInfo.DataBean>{


    /**
     * code : 200
     * data : {"device":["冰箱","第六感","WIFI"],"status":1}
     * msg : 0
     */

    public static class DataBean {
        /**
         * device : ["冰箱","第六感","WIFI"]
         * status : 1
         */

        public String status;
        public List<String> device;
    }
}
