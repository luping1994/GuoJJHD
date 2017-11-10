package net.suntrans.guojjhd.bean;

/**
 * Created by Looney on 2017/4/19.
 */

public class LoginEntity extends RespondBody<LoginEntity.LoginInfo>{

    public static class LoginInfo{

        public TokenBean token;
        public UserBean user;
        public Timmer timer;

        public static class TokenBean {
            public String token_type;
            public String expires_in;
            public String access_token;
            public String refresh_token;
            public String expires_time;
        }

        public static class UserBean {
            public String id;
            public String family_id;
            public String family_name;
            public String username;
            public String nickname;
            public String mobile;
            public String login;
            public String manager;
            public String cover;
            public String created_at;
            public String updated_at;
        }
        public static class Timmer {
            public long sensus;
            public long ammeter;
            public long light;

        }
    }
}
