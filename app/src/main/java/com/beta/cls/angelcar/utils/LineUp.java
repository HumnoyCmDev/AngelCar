package com.beta.cls.angelcar.utils;

import android.support.annotation.NonNull;

/**
 * Created by humnoy on 10/3/59.
 */
public class LineUp {

    private static LineUp ourInstance;

    public static LineUp getInstance() {
        if (ourInstance == null)
            ourInstance = new LineUp();
        return ourInstance;
    }
    private LineUp() {
    }

    public String formatLineUp(String s){
        return s.replaceAll("\n","<n>");
    }

    public String convertLineUp(String s){
        return s.replaceAll("<n>","\n");
    }

    public String converSpace(String s){
        return s.replaceAll("<n>"," ");
    }

    @NonNull
    public String subTopic(@NonNull String s){
        if (!s.contains("``")) return s;//false return string
        return s.substring(0,s.indexOf("``"));
    }

    @NonNull
    public String subDetail(@NonNull String s){
        if (!s.contains("``")) return "";
        return s.substring(s.indexOf("``")+2,s.length());
    }

    public String append(String topic, String detail){
        return topic+"``"+ formatLineUp(detail);
    }

    public Post getPostCollection(String s){
        Post post = new Post();
        post.setTopic(subTopic(s));
        post.setDetail(convertLineUp(subDetail(s)));
        return post;
    }

    public static class Post{
        private String topic;
        private String detail;
        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
}
