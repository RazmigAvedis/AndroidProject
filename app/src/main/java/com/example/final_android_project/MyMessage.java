package com.example.final_android_project;

public class MyMessage {

    public String emailFrom;
    public String Subject;
    public String receivedDate;
    public String Message;
    public MyMessage(String subject,String receivedDate, String message,String emailFrom){
        this.Subject = subject;
        this.emailFrom = emailFrom;
        this.receivedDate = receivedDate;
        this.Message = message;
    }
}
