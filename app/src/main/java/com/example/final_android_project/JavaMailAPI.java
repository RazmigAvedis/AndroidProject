package com.example.final_android_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private OnEmailResultListener listener;

    // Define the listener interface
    public interface OnEmailResultListener {
        void onSuccess();
        void onError(String errorMessage);
    }

    private ProgressDialog progressDialog;
    private Context context;

    private String email, subject, message, emailFrom;

    public JavaMailAPI(Context context, String email, String subject, String message, String emailFrom, OnEmailResultListener listener) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.emailFrom = emailFrom;
        this.listener = listener;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom,AppConstants.USER_PASSWORD);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(emailFrom));
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            listener.onSuccess();
        } catch (MessagingException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }



        return null;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
//        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }
}