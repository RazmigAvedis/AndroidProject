package com.example.final_android_project;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Bundle bundle = getIntent().getExtras();
        MyMessage message = new MyMessage(bundle.getString("subject"),bundle.getString("receivedDate"),bundle.getString("message"),bundle.getString("emailFrom"));

        TextView subjectTextView = (TextView) findViewById(R.id.inbox_edit_subject_email);
        TextView emailFromTextView = (TextView) findViewById(R.id.inbox_edit_sender_email);
        WebView bodyTextView = (WebView) findViewById(R.id.inbox_edit_message_email);
        subjectTextView.setText(message.Subject);
        emailFromTextView.setText(message.emailFrom);
        String htmlContent =  message.Message;
        bodyTextView.getSettings().setJavaScriptEnabled(true);
        bodyTextView.getSettings().setLoadWithOverviewMode(true);
        bodyTextView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
    }
}