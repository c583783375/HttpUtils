package com.sprint.www.httputils.bean;

import java.util.List;


public class Content {


    private String message;
    private int total_number;
    private boolean has_more;
    private int login_status;
    private int show_et_status;
    private String post_content_hint;
    private boolean has_more_to_refresh;
    private int action_to_last_stick;
    private int feed_flag;
    private TipsBean tips;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public int getShow_et_status() {
        return show_et_status;
    }

    public void setShow_et_status(int show_et_status) {
        this.show_et_status = show_et_status;
    }

    public String getPost_content_hint() {
        return post_content_hint;
    }

    public void setPost_content_hint(String post_content_hint) {
        this.post_content_hint = post_content_hint;
    }

    public boolean isHas_more_to_refresh() {
        return has_more_to_refresh;
    }

    public void setHas_more_to_refresh(boolean has_more_to_refresh) {
        this.has_more_to_refresh = has_more_to_refresh;
    }

    public int getAction_to_last_stick() {
        return action_to_last_stick;
    }

    public void setAction_to_last_stick(int action_to_last_stick) {
        this.action_to_last_stick = action_to_last_stick;
    }

    public int getFeed_flag() {
        return feed_flag;
    }

    public void setFeed_flag(int feed_flag) {
        this.feed_flag = feed_flag;
    }

    public TipsBean getTips() {
        return tips;
    }

    public void setTips(TipsBean tips) {
        this.tips = tips;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class TipsBean {
        /**
         * type : app
         * display_duration : 2
         * display_info : 今日头条推荐引擎有15条更新
         * display_template : 今日头条推荐引擎有%s条更新
         * open_url :
         * web_url :
         * download_url :
         * app_name : 今日头条
         * package_name :
         */

        private String type;
        private int display_duration;
        private String display_info;
        private String display_template;
        private String open_url;
        private String web_url;
        private String download_url;
        private String app_name;
        private String package_name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDisplay_duration() {
            return display_duration;
        }

        public void setDisplay_duration(int display_duration) {
            this.display_duration = display_duration;
        }

        public String getDisplay_info() {
            return display_info;
        }

        public void setDisplay_info(String display_info) {
            this.display_info = display_info;
        }

        public String getDisplay_template() {
            return display_template;
        }

        public void setDisplay_template(String display_template) {
            this.display_template = display_template;
        }

        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

    }

    public static class DataBean {
        /**
         * content : {"abstract":"央视网消息：在25日的例行记者会上，有记者提问：周末，朝鲜外相李勇浩在联大发言时称，朝鲜将不可避免地将导弹对准美国。美国总统特朗普对此回应称，朝鲜不会存在太久了。随着朝美两国口舌之争愈演愈烈，中方是否担心半岛会爆发真正的战争？","action_extra":"{\"channel_id\": 3189398996}","action_list":[{"action":1,"desc":"","extra":{}},{"action":3,"desc":"","extra":{}},{"action":7,"desc":"","extra":{}},{"action":9,"desc":"","extra":{}}],"aggr_type":1,"allow_download":false,"article_sub_type":0,"article_type":0,"article_url":"http://toutiao.com/group/6469689990082200078/","ban_comment":1,"behot_time":1506404571,"bury_count":6,"cell_flag":11,"cell_layout_style":1,"cell_type":0,"comment_count":2,"cursor":1506404571999,"digg_count":2,"display_url":"http://toutiao.com/group/6469689990082200078/","filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:572097967","is_selected":false,"name":"拉黑作者:央视网新闻"},{"id":"2:11384971","is_selected":false,"name":"不想看:时政外交"}],"forward_info":{"forward_count":12},"gallary_image_count":2,"group_id":6469689990082200078,"has_image":true,"has_m3u8_video":false,"has_mp4_video":0,"has_video":false,"hot":0,"ignore_web_transform":1,"is_subject":false,"item_id":6469689990082200078,"item_version":0,"keywords":"朝鲜,联合国安理会,六方会谈,安理会,半岛","level":0,"like_count":2,"log_pb":{"impr_id":"20170926134251010011018084655D90"},"media_info":{"avatar_url":"http://p1.pstatp.com/large/bc20000b91968707dab","follow":false,"is_star_user":false,"media_id":50044041847,"name":"央视网新闻","recommend_reason":"","recommend_type":0,"user_id":50025817786,"user_verified":true,"verified_content":""},"media_name":"央视网新闻","middle_image":{"height":281,"uri":"list/3c6600024c2bd32a84f9","url":"http://p3.pstatp.com/list/300x196/3c6600024c2bd32a84f9.webp","url_list":[{"url":"http://p3.pstatp.com/list/300x196/3c6600024c2bd32a84f9.webp"},{"url":"http://pb9.pstatp.com/list/300x196/3c6600024c2bd32a84f9.webp"},{"url":"http://pb1.pstatp.com/list/300x196/3c6600024c2bd32a84f9.webp"}],"width":500},"publish_time":1506342085,"read_count":843047,"repin_count":3648,"rid":"20170926134251010011018084655D90","share_count":452,"share_url":"http://m.toutiao.com/a6469689990082200078/?iid=134852330716\u0026app=news_article","show_portrait":false,"show_portrait_article":false,"source":"央视网新闻","source_icon_style":4,"source_open_url":"sslocal://profile?uid=50025817786","tag":"news_world","tag_id":6469689990082200078,"tip":0,"title":"中方是否担心半岛会爆发真正的战争？外交部回应","ugc_recommend":{"activity":"","reason":"央视网新闻频道官方帐号"},"url":"http://toutiao.com/group/6469689990082200078/","user_info":{"avatar_url":"http://p3.pstatp.com/thumb/bc20000b91968707dab","description":"央视网原创内容，包含热点解析，精彩图片，说新闻等。","follow":false,"follower_count":0,"name":"央视网新闻","user_auth_info":"{\"auth_type\": \"0\", \"auth_info\": \"央视网新闻频道官方帐号\"}","user_id":50025817786,"user_verified":true,"verified_content":"央视网新闻频道官方帐号"},"user_repin":0,"user_verified":1,"verified_content":"央视网新闻频道官方帐号","video_style":0}
         * code :
         */

        private String content;
        private String code;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "content='" + content + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Content{" +
                "data=" + data +
                '}';
    }
}
