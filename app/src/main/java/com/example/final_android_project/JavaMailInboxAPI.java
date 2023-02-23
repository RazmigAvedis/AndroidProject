package com.example.final_android_project;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

public class JavaMailInboxAPI extends AsyncTask<Void, Void, Void> {

    private OnEmailResultListener listener;

    // Define the listener interface
    public interface OnEmailResultListener {
        void onSuccess(List<MyMessage> messages);

        void onError(String errorMessage);
    }

    private Context context;

    private String email;

    public Message[] inbox;

    public JavaMailInboxAPI(Context context, String email, OnEmailResultListener listener) {
        this.context = context;
        this.email = email;
        this.listener = listener;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.starttls.enable", "true");
        properties.put("mail.imaps.ssl.trust", "imap.gmail.com");

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, "zjrh hyob yyky zatc");
            }
        });

        try {
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", email, "zjrh hyob yyky zatc");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int totalMessages = inbox.getMessageCount();
            int start = Math.max(1, totalMessages - 20); // retrieve the last 100 messages
            Message[] messages = inbox.getMessages(start, totalMessages);

            Arrays.sort(messages, (m1, m2) -> {
                try {
                    Date d1 = m1.getReceivedDate();
                    Date d2 = m2.getReceivedDate();
                    return d2.compareTo(d1);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return 0;
                }
            });

            List<MyMessage> messageList = new ArrayList<>();
            for (Message message : messages) {
                String subject = message.getSubject();
                String formattedDate = null;
                String messageBody = "";
                String messageFrom = "";


                try {// Get the content of the message
                    Object content = message.getContent();

// Check if the content is a string
                    if (content instanceof String) {
                        // The content is already plain text
                        messageBody = (String) content;
                    } else if (content instanceof MimeMultipart) {
                        // The content is a MIME multi-part message
                        MimeMultipart multipart = (MimeMultipart) content;
                        int count = multipart.getCount();
                        for (int i = 0; i < count; i++) {
                            BodyPart bodyPart = multipart.getBodyPart(i);
                            String disposition = bodyPart.getDisposition();
                            if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
                                // do nothing
                            } else {
                                // Get the text content of the body part
                                String bodyPartContent = (String) bodyPart.getContent();
                                if (bodyPart.isMimeType("text/html")) {
                                    // Parse HTML content
//                                    Spanned spanned = Html.fromHtml();
                                    messageBody += Html.fromHtml(bodyPartContent).toString();
                                } else {
                                    messageBody += bodyPartContent;
                                }
                            }
                        }
                    }

                    subject = message.getSubject();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formattedDate = dateFormat.format(message.getReceivedDate());
                    Address[] fromAddresses = message.getFrom();
                    if (fromAddresses != null && fromAddresses.length > 0) {
                        messageFrom = ((InternetAddress) fromAddresses[0]).toString();
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Add message data to a list
                messageList.add(new MyMessage(subject, formattedDate,messageBody,messageFrom));
            }

            listener.onSuccess(messageList);

            inbox.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }

        return null;


    }
}