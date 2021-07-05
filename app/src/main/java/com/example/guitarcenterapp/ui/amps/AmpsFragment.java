package com.example.guitarcenterapp.ui.amps;

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
import com.example.guitarcenterapp.databinding.AmpsFragmentBinding;
import com.example.guitarcenterapp.databinding.FragmentHomeBinding;
import com.example.guitarcenterapp.ui.basses.BassesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AmpsFragment extends Fragment {

    private AmpsViewModel mViewModel;

    public static AmpsFragment newInstance() {
        return new AmpsFragment();
    }

    private AmpsFragmentBinding binding;

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private DBSQLiteHelper dbsqLiteHelper;

    private List<Product> products;

    private Adapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(AmpsViewModel.class);

        binding = AmpsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbsqLiteHelper = new DBSQLiteHelper(getContext());

        products = new ArrayList<Product>();

        products = dbsqLiteHelper.getAllProducts("Amps");

        recyclerView = (RecyclerView) binding.rvAmps;

        int numberOfColumn = UtilityHelper.calculateNoOfColumns(getContext(), 160);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumn);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new Adapter(this.getContext(), products);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AmpsViewModel.class);
        // TODO: Use the ViewModel
    }

}