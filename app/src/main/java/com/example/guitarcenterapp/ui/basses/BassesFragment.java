package com.example.guitarcenterapp.ui.basses;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guitarcenterapp.R;
import com.example.guitarcenterapp.ui.guitars.GuitarsViewModel;

public class BassesFragment extends Fragment {

    private BassesViewModel mViewModel;

    public static BassesFragment newInstance() {
        return new BassesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(BassesViewModel.class);

        return inflater.inflate(R.layout.basses_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BassesViewModel.class);
        // TODO: Use the ViewModel
    }

}