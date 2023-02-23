package com.example.final_android_project;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
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

    private Context context;

    private Session session;
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

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom,"zjrh hyob yyky zatc");
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

    public void GetInbox(){
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.timeout", "10000");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, "zjrh hyob yyky zatc");
            }
        });

        try {
            Store store = session.getStore("imaps");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            listener.onSuccess(messages);

            inbox.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }

        return null;

    }
}