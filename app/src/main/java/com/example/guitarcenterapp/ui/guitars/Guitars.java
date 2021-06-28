package com.example.guitarcenterapp.ui.guitars;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guitarcenterapp.R;

public class Guitars extends Fragment {

    public static Guitars newInstance() {
        return new Guitars();
    }

    private GuitarsViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(GuitarsViewModel.class);

        return inflater.inflate(R.layout.guitars_fragment, container, false);
    }

}