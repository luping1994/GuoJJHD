package net.suntrans.bieshuhd.bean;

import java.util.List;

/**
 * Created by Looney on 2017/11/9.
 */

public class RoomEntity extends RespondBody<RoomEntity.DataBean> {

    /**
     * code : 200
     * data : {"total":8,"lists":[{"id":143,"family_id":"3","user_id":"49","name":"主卧","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/sHDMh0ummc1WZC5n9lN5eqyjAgoHeTVM5z5AavPx.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"2","status":"1","created_at":"2017-11-09 13:00:52","updated_at":"2017-11-09 13:00:52","deleted_at":null},{"id":144,"family_id":"3","user_id":"49","name":"客厅","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/NiGDtzQ8ZyADF5z3tKv1jQDWEbclSLiyYRRETr1X.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"1","status":"1","created_at":"2017-11-09 13:00:55","updated_at":"2017-11-09 13:00:55","deleted_at":null},{"id":145,"family_id":"3","user_id":"49","name":"餐厅","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/KgcMNYEctMrAlyJxuhieJolMXvMHjSOltYrrw3D6.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"8","status":"1","created_at":"2017-11-09 13:00:57","updated_at":"2017-11-09 13:00:57","deleted_at":null},{"id":146,"family_id":"3","user_id":"49","name":"厨房","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/Pjx8fLFQGY72Nl03SnmmGIqJ0vtJAoachuNK5ok7.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"5","status":"1","created_at":"2017-11-09 13:00:59","updated_at":"2017-11-09 13:00:59","deleted_at":null},{"id":147,"family_id":"3","user_id":"49","name":"玄关","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/mpVFUiLMBDm2dUPkkK2YpXrZh8RO3flTUSwLjShG.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"7","status":"1","created_at":"2017-11-09 13:06:10","updated_at":"2017-11-09 13:06:10","deleted_at":null},{"id":148,"family_id":"3","user_id":"49","name":"儿童房","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/h5L1OibB2hjltLVxqyxlApL2EoFaNRMaLDrP1Lfo.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"4","status":"1","created_at":"2017-11-09 13:06:16","updated_at":"2017-11-09 13:06:16","deleted_at":null},{"id":149,"family_id":"3","user_id":"49","name":"卫生间","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/MpiqBbK8qHJERiv0UPhK1OtsVAay95K47zgupDa4.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"9","status":"1","created_at":"2017-11-09 13:01:06","updated_at":"2017-11-09 13:01:06","deleted_at":null},{"id":150,"family_id":"3","user_id":"49","name":"多功能厅","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/Zsykz8kagoLR2hLCzjeXGsRDdIVKRP9owq0fSbKU.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"10","status":"1","created_at":"2017-11-09 13:06:24","updated_at":"2017-11-09 13:06:24","deleted_at":null}]}
     * msg : 0
     */


    public static class DataBean {
        /**
         * total : 8
         * lists : [{"id":143,"family_id":"3","user_id":"49","name":"主卧","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/sHDMh0ummc1WZC5n9lN5eqyjAgoHeTVM5z5AavPx.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"2","status":"1","created_at":"2017-11-09 13:00:52","updated_at":"2017-11-09 13:00:52","deleted_at":null},{"id":144,"family_id":"3","user_id":"49","name":"客厅","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/NiGDtzQ8ZyADF5z3tKv1jQDWEbclSLiyYRRETr1X.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"1","status":"1","created_at":"2017-11-09 13:00:55","updated_at":"2017-11-09 13:00:55","deleted_at":null},{"id":145,"family_id":"3","user_id":"49","name":"餐厅","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/KgcMNYEctMrAlyJxuhieJolMXvMHjSOltYrrw3D6.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"8","status":"1","created_at":"2017-11-09 13:00:57","updated_at":"2017-11-09 13:00:57","deleted_at":null},{"id":146,"family_id":"3","user_id":"49","name":"厨房","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/Pjx8fLFQGY72Nl03SnmmGIqJ0vtJAoachuNK5ok7.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"5","status":"1","created_at":"2017-11-09 13:00:59","updated_at":"2017-11-09 13:00:59","deleted_at":null},{"id":147,"family_id":"3","user_id":"49","name":"玄关","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/mpVFUiLMBDm2dUPkkK2YpXrZh8RO3flTUSwLjShG.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"7","status":"1","created_at":"2017-11-09 13:06:10","updated_at":"2017-11-09 13:06:10","deleted_at":null},{"id":148,"family_id":"3","user_id":"49","name":"儿童房","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/h5L1OibB2hjltLVxqyxlApL2EoFaNRMaLDrP1Lfo.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"4","status":"1","created_at":"2017-11-09 13:06:16","updated_at":"2017-11-09 13:06:16","deleted_at":null},{"id":149,"family_id":"3","user_id":"49","name":"卫生间","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/MpiqBbK8qHJERiv0UPhK1OtsVAay95K47zgupDa4.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"9","status":"1","created_at":"2017-11-09 13:01:06","updated_at":"2017-11-09 13:01:06","deleted_at":null},{"id":150,"family_id":"3","user_id":"49","name":"多功能厅","name_en":null,"image":null,"img_url":"http://guojj.suntrans-cloud.com/upload/images/Zsykz8kagoLR2hLCzjeXGsRDdIVKRP9owq0fSbKU.jpeg","pad_show":"0","sensus_id":null,"img_id":null,"timer":null,"sort":"10","status":"1","created_at":"2017-11-09 13:06:24","updated_at":"2017-11-09 13:06:24","deleted_at":null}]
         */

        public int total;
        public List<ListsBean> lists;

        public static class ListsBean {
            /**
             * id : 143
             * family_id : 3
             * user_id : 49
             * name : 主卧
             * name_en : null
             * image : null
             * img_url : http://guojj.suntrans-cloud.com/upload/images/sHDMh0ummc1WZC5n9lN5eqyjAgoHeTVM5z5AavPx.jpeg
             * pad_show : 0
             * sensus_id : null
             * img_id : null
             * timer : null
             * sort : 2
             * status : 1
             * created_at : 2017-11-09 13:00:52
             * updated_at : 2017-11-09 13:00:52
             * deleted_at : null
             */

            public String id;
            public String family_id;
            public String user_id;
            public String name;
            public Object name_en;
            public Object image;
            public String img_url;
            public String pad_show;
            public Object sensus_id;
            public Object img_id;
            public Object timer;
            public String sort;
            public String status;
            public String created_at;
            public String updated_at;
            public Object deleted_at;
        }
    }
}
