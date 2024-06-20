package in.softment.exampracticeadmin.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.softment.exampracticeadmin.Adapter.AdminSubcategoriesAdapter;
import in.softment.exampracticeadmin.Interface.SubcategoriesListeners;
import in.softment.exampracticeadmin.Model.CategoryModel;
import in.softment.exampracticeadmin.Model.SubcategoryModel;
import in.softment.exampracticeadmin.R;
import in.softment.exampracticeadmin.Util.ProgressHud;
import in.softment.exampracticeadmin.Util.Services;

public class ShowAllSubcategoriesActivity extends AppCompatActivity {


    private CategoryModel categoryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_subcategories);

        categoryModel = (CategoryModel) getIntent().getSerializableExtra("category");

        if (categoryModel == null) {
            finish();
        }


        //BackBtnClicked
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView categoryName = findViewById(R.id.categoryName);
        TextView no_subcategories_available = findViewById(R.id.no_subcategories_available);

        categoryName.setText(categoryModel.getName());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SubcategoryModel> subcategoryModels = new ArrayList<>();
        AdminSubcategoriesAdapter adminSubcategoriesAdapter = new AdminSubcategoriesAdapter(this,subcategoryModels);
        recyclerView.setAdapter(adminSubcategoriesAdapter);

        findViewById(R.id.addSubCatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAllSubcategoriesActivity.this,AddSubcategoryActivity.class);
                intent.putExtra("catId",categoryModel.getId());
                startActivity(intent);
            }
        });

        findViewById(R.id.addQuestions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAllSubcategoriesActivity.this,AddXLSXFileActivity.class);
                intent.putExtra("catId",categoryModel.getId());
                startActivity(intent);
            }
        });

        ProgressHud.show(this,"");

        Services.getAllSubcategory(categoryModel.getId(), new SubcategoriesListeners() {
            @Override
            public void onCallback(ArrayList<SubcategoryModel> subCatModels, String error) {
                ProgressHud.dialog.dismiss();
                if (error == null) {
                    subcategoryModels.clear();
                    subcategoryModels.addAll(subCatModels);
                    adminSubcategoriesAdapter.notifyDataSetChanged();

                    if (subcategoryModels.size() > 0) {
                        no_subcategories_available.setVisibility(View.GONE);
                    }
                    else {
                        no_subcategories_available.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Services.showDialog(ShowAllSubcategoriesActivity.this,"ERROR",error);
                }
            }
        });
    }
}
