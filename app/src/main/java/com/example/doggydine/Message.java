package com.example.doggydine;
import android.widget.ImageView;
public class Message {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT="bot";
    String message;
    String sentBy;
    ImageView imageView;
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getSentBy(){
        return sentBy;
    }
    public void setSentBy(String sentBy){
        this.sentBy = sentBy;
    }
    public ImageView getImageView() { return imageView; }
    public void setImageView(ImageView imageView) { this.imageView = imageView; }

    public Message(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }
    public Message(String message, String sentBy, ImageView imageView) {
        this.message = message;
        this.sentBy = sentBy;
        this.imageView = imageView;
    }
}
