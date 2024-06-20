package in.softment.exampracticeadmin.Fragment.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.softment.exampracticeadmin.Adapter.AdminCategoryAdapter;
import in.softment.exampracticeadmin.Admin.AddCategoryActivity;
import in.softment.exampracticeadmin.Interface.CategoriesListener;
import in.softment.exampracticeadmin.Model.CategoryModel;
import in.softment.exampracticeadmin.R;
import in.softment.exampracticeadmin.Util.ProgressHud;
import in.softment.exampracticeadmin.Util.Services;


public class AddFragment extends Fragment {

    private ArrayList<CategoryModel> categoryModels;
    private TextView no_categories_available;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        categoryModels = new ArrayList<>();
        AdminCategoryAdapter adminCategoryAdapter = new AdminCategoryAdapter(getContext(), categoryModels);
        recyclerView.setAdapter(adminCategoryAdapter);

        no_categories_available = view.findViewById(R.id.no_categories_available);


        //ADDCATEGORY
        view.findViewById(R.id.addCatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddCategoryActivity.class));
            }
        });
        ProgressHud.show(getContext(),"Loading...");
        Services.getAllCategory(false,new CategoriesListener() {
            @Override
            public void onCallback(ArrayList<CategoryModel> catModels, String error) {
                ProgressHud.dialog.dismiss();
                if (error == null) {
                    categoryModels.clear();
                    categoryModels.addAll(catModels);
                    adminCategoryAdapter.notifyDataSetChanged();

                    if (categoryModels.size() > 0) {
                        no_categories_available.setVisibility(View.GONE);
                    }
                    else {
                        no_categories_available.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Services.showDialog(getContext(),"ERROR",error);
                }
            }
        });



        return view;
    }


}
