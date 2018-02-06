package net.suntrans.smhome.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class HomeSceneBean extends RespondBody<HomeSceneBean.DataBean> {



    public static class DataBean {
        /**
         * total : 2
         * lists : [{"id":167,"name":"回家"},{"id":168,"name":"离家"}]
         */

        public int total;
        public List<ListsBean> lists;

        public static class ListsBean {
            /**
             * id : 167
             * name : 回家
             */

            public int id;
            public String name;
        }
    }
}
