package com.example.guitarcenterapp.ui.amps;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guitarcenterapp.R;
import com.example.guitarcenterapp.ui.basses.BassesViewModel;

public class AmpsFragment extends Fragment {

    private AmpsViewModel mViewModel;

    public static AmpsFragment newInstance() {
        return new AmpsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(AmpsViewModel.class);

        return inflater.inflate(R.layout.amps_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AmpsViewModel.class);
        // TODO: Use the ViewModel
    }

}