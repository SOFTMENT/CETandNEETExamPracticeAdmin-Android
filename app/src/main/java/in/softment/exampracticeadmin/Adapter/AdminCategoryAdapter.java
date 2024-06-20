package in.softment.exampracticeadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.softment.exampracticeadmin.Admin.EditCategoryActivity;
import in.softment.exampracticeadmin.Admin.ShowAllSubcategoriesActivity;
import in.softment.exampracticeadmin.Model.CategoryModel;
import in.softment.exampracticeadmin.R;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoryModel> categoryModels;
    public AdminCategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public AdminCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_category_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        CategoryModel categoryModel = categoryModels.get(position);
        holder.checkBox.setChecked(categoryModel.isAvailable());
        if (!categoryModel.getThumbnail().isEmpty()) {
            Glide.with(context).load(categoryModel.getThumbnail()).placeholder(R.drawable.placeholder).into(holder.thumbnail);
        }
        holder.catName.setText(categoryModel.getName());
        holder.catQuiz.setText(categoryModel.getTotalQuiz()+" Quiz");

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, Boolean> map = new HashMap<>();
                if (isChecked) {
                    map.put("available",true);
                    FirebaseFirestore.getInstance().collection("Categories").document(categoryModel.getId()).set(map, SetOptions.merge());
                }
                else {
                    map.put("available",false);
                    FirebaseFirestore.getInstance().collection("Categories").document(categoryModel.getId()).set(map, SetOptions.merge());
                }
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCategoryActivity.class);
                intent.putExtra("category",categoryModel);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowAllSubcategoriesActivity.class);
                intent.putExtra("category",categoryModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       CheckBox checkBox;
       ImageView thumbnail;
       TextView catName, catQuiz, edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.isEnabled);
            thumbnail = itemView.findViewById(R.id.catImage);
            catName = itemView.findViewById(R.id.catName);
            catQuiz = itemView.findViewById(R.id.catQuiz);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}
