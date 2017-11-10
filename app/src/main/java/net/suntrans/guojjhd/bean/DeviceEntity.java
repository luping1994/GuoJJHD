package net.suntrans.guojjhd.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class DeviceEntity extends RespondBody<DeviceEntity.DataBean>{

    /**
     * code : 200
     * data : {"total":6,"lists":[{"id":749,"status":"1","name":"卫生间灯"},{"id":750,"status":"1","name":"玄关灯"},{"id":751,"status":"1","name":"客厅灯"},{"id":752,"status":"0","name":"卧室灯"},{"id":753,"status":"0","name":"厨房筒灯"},{"id":754,"status":"0","name":"儿童房灯"}]}
     * msg : 0
     */



    public static class DataBean {
        /**
         * total : 6
         * lists : [{"id":749,"status":"1","name":"卫生间灯"},{"id":750,"status":"1","name":"玄关灯"},{"id":751,"status":"1","name":"客厅灯"},{"id":752,"status":"0","name":"卧室灯"},{"id":753,"status":"0","name":"厨房筒灯"},{"id":754,"status":"0","name":"儿童房灯"}]
         */

        public int total;
        public List<ListsBean> lists;

        public static class ListsBean {
            /**
             * id : 749
             * status : 1
             * name : 卫生间灯
             */
            public String id;
            public String status;
            public String name;
        }
    }
}
