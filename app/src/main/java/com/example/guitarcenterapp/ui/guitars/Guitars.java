package com.example.guitarcenterapp.ui.guitars;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guitarcenterapp.Adapters.Adapter;
import com.example.guitarcenterapp.Helpers.DBSQLiteHelper;
import com.example.guitarcenterapp.Helpers.UtilityHelper;
import com.example.guitarcenterapp.Models.Product;
import com.example.guitarcenterapp.R;
import com.example.guitarcenterapp.databinding.FragmentHomeBinding;
import com.example.guitarcenterapp.databinding.GuitarsFragmentBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Guitars extends Fragment {

    public static Guitars newInstance() {
        return new Guitars();
    }

    private GuitarsViewModel mViewModel;

    private GuitarsFragmentBinding binding;

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private DBSQLiteHelper dbsqLiteHelper;

    private List<Product> products;

    private Adapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(GuitarsViewModel.class);

        binding = GuitarsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbsqLiteHelper = new DBSQLiteHelper(getContext());

        products = new ArrayList<Product>();

        products = dbsqLiteHelper.getAllProducts("Guitar");

        recyclerView = (RecyclerView) binding.rvGuitars;

        int numberOfColumn = UtilityHelper.calculateNoOfColumns(getContext(), 160);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumn);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new Adapter(products);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return root;
    }

}