package com.example.final_android_project.ui.Email;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_android_project.EmailActivity;
import com.example.final_android_project.JavaMailAPI;
import com.example.final_android_project.MyMessage;
import com.example.final_android_project.R;
import com.example.final_android_project.databinding.FragmentEmailBinding;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmailFragment extends Fragment {

    private FragmentEmailBinding binding;
    private TextView emailFrom;
    private EditText email, subject, message;
    private Button button;

    public List<MyMessage> messages;
    private EmailViewModel EmailViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmailViewModel =
                new ViewModelProvider(this).get(EmailViewModel.class);

        binding = FragmentEmailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        email = binding.editRecipientEmail;
        subject = binding.editEmailSubject;
        message = binding.editEmailBody;
        emailFrom = binding.editSenderEmail;
        button = binding.buttonSend;

        EmailViewModel.setUserEmail(getContext());
        EmailViewModel.getuserEmail().observe(getViewLifecycleOwner(), From -> {
            EmailViewModel.getInbox(getContext(), From);
            EmailViewModel.getuserInbox().observe(getViewLifecycleOwner(), Inbox -> {
                messages = Inbox;
                ArrayAdapter<MyMessage> adapter = new ArrayAdapter<MyMessage>(getContext(), R.layout.single_email_list_view, Inbox) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            convertView = inflater.inflate(R.layout.single_email_list_view, parent, false);
                        }
                        TextView textViewSubject = (TextView) convertView.findViewById(R.id.inbox_email_subject);
                        TextView textViewDate = (TextView) convertView.findViewById(R.id.inbox_email_date);

                        MyMessage message = Inbox.get(position);
                        String subject = null;
                        String formattedDate = null;
                        subject = message.Subject;
                        formattedDate = message.receivedDate;


                        textViewSubject.setText(subject);
                        textViewDate.setText(formattedDate);

                        return convertView;
                    }
                };
                binding.listInbox.setAdapter(adapter);
            });
            emailFrom.setText(From);
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        binding.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmailFrom = emailFrom.getText().toString();
                EmailViewModel.getInbox(getContext(), mEmailFrom);
            }
        });
        EmailViewModel.getBusy().observe(getViewLifecycleOwner(), busy -> {
            if (busy) {
                binding.refreshButton.setEnabled(false);

                RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(1000);
                rotateAnimation.setRepeatCount(Animation.INFINITE);

// Start the animation
                binding.refreshButton.startAnimation(rotateAnimation);
            } else {
                binding.refreshButton.setEnabled(true);
                binding.refreshButton.getAnimation().cancel();
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMessage message = (MyMessage) messages.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("subject", message.Subject);
                bundle.putString("receivedDate", message.receivedDate);
                bundle.putString("message", message.Message);
                bundle.putString("emailFrom", message.emailFrom);

                Intent intent = new Intent(getActivity(), EmailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendEmail() {
        String mEmail = email.getText().toString();
        String mSubject = subject.getText().toString();
        String mMessage = message.getText().toString();
        String mEmailFrom = emailFrom.getText().toString();


        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), mEmail, mSubject, mMessage, mEmailFrom, new JavaMailAPI.OnEmailResultListener() {
            @Override
            public void onSuccess() {
                Snackbar.make(binding.getRoot(), "Email sent successfully!", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });

        javaMailAPI.execute();


    }
}