package com.doongis.r2.ServerConnect;

import java.io.Serializable;

public class CommentVO implements Serializable {

    private int comment_no;
    private String comment_comment;
    private String comment_date;
    private int comment_heart;
    private String user_id;
    private int article_no;
    public int getComment_no() {
        return comment_no;
    }
    public void setComment_no(int comment_no) {
        this.comment_no = comment_no;
    }
    public String getComment_comment() {
        return comment_comment;
    }
    public void setComment_comment(String comment_comment) {
        this.comment_comment = comment_comment;
    }
    public String getComment_date() {
        return comment_date;
    }
    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }
    public int getComment_heart() {
        return comment_heart;
    }
    public void setComment_heart(int comment_heart) {
        this.comment_heart = comment_heart;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public int getArticle_no() {
        return article_no;
    }
    public void setArticle_no(int article_no) {
        this.article_no = article_no;
    }
    @Override
    public String toString() {
        return "CommentVO [comment_no=" + comment_no + ", comment_comment=" + comment_comment + ", comment_date="
                + comment_date + ", comment_heart=" + comment_heart + ", user_id=" + user_id + ", article_no="
                + article_no + "]";
    }

}
