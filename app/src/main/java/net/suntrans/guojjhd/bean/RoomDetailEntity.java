package net.suntrans.guojjhd.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class RoomDetailEntity extends RespondBody<RoomDetailEntity.DataBean>{



    public static class DataBean {
        /**
         * house : {"id":143,"name":"主卧","img_big":"http://guojj.suntrans-cloud.com/upload/images/b143.png"}
         * light : {"total":1,"lists":[{"id":752,"status":"0","name":"卧室灯"}]}
         * socket : {"total":1,"lists":[{"id":755,"status":"0","name":"主卧插座"}]}
         */

        public HouseBean house;
        public LightBean socket;
        public LightBean light;

        public static class HouseBean {
            /**
             * id : 143
             * name : 主卧
             * img_big : http://guojj.suntrans-cloud.com/upload/images/b143.png
             */

            public int id;
            public String name;
            public String img_big;
        }

        public static class LightBean{
            public String total;
            public List<DeviceEntity.DataBean.ListsBean> lists;

        }

    }
}
