package com.example.final_android_project.ui.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_android_project.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel ProfileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProfileViewModel.loadDataForUser(getContext());

        final TextView userNameTextView = binding.tvName;
        final TextView emailTextView = binding.email;
        final TextView phoneTextView = binding.phone;
        ProfileViewModel.getUsername().observe(getViewLifecycleOwner(), userNameTextView::setText);
        ProfileViewModel.getEmail().observe(getViewLifecycleOwner(), emailTextView::setText);
        ProfileViewModel.getPhone().observe(getViewLifecycleOwner(), phoneTextView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}