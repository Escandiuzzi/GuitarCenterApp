package com.example.guitarcenterapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarcenterapp.Adapters.Adapter;
import com.example.guitarcenterapp.AddProductActivity;
import com.example.guitarcenterapp.Helpers.DBSQLiteHelper;
import com.example.guitarcenterapp.Helpers.UtilityHelper;
import com.example.guitarcenterapp.Models.Product;
import com.example.guitarcenterapp.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private DBSQLiteHelper dbsqLiteHelper;

    private List<Product> products;

    private Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbsqLiteHelper = new DBSQLiteHelper(getContext());

        products = new ArrayList<Product>();

        products = dbsqLiteHelper.getAllProducts();

        recyclerView = (RecyclerView) binding.rvGuitars;
        floatingActionButton = (FloatingActionButton) binding.fab;

        int numberOfColumn = UtilityHelper.calculateNoOfColumns(getContext(), 160);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumn);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new Adapter(products);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}