package com.doongis.r2.ServerConnect;

import java.io.Serializable;



public class ArticleVO implements Serializable{
    private int article_no;
    private String article_title;
    private String article_date;
    private String article_content;
    private int article_count;
    private int article_heart;
    private String article_url;

    private int board_no;
    private String user_id;
    private String user_name;

    public int getArticle_no() {
        return article_no;
    }
    public void setArticle_no(int article_no) {
        this.article_no = article_no;
    }
    public String getArticle_title() {
        return article_title;
    }
    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }
    public String getArticle_date() {
        return article_date;
    }
    public void setArticle_date(String article_date) {
        this.article_date = article_date;
    }
    public String getArticle_content() {
        return article_content;
    }
    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }
    public int getArticle_count() {
        return article_count;
    }
    public void setArticle_count(int article_count) {
        this.article_count = article_count;
    }
    public int getArticle_heart() {
        return article_heart;
    }
    public void setArticle_heart(int article_heart) {
        this.article_heart = article_heart;
    }
    public String getArticle_url() {
        return article_url;
    }
    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }
    public int getBoard_no() {
        return board_no;
    }
    public void setBoard_no(int board_no) {
        this.board_no = board_no;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "ArticleVO [article_no=" + article_no + ", article_title=" + article_title + ", article_date="
                + article_date + ", article_content=" + article_content + ", article_count=" + article_count
                + ", article_heart=" + article_heart + ", article_url=" + article_url + ", board_no=" + board_no
                + ", user_id=" + user_id + ", user_name=" + user_name+"]";
    }


}
