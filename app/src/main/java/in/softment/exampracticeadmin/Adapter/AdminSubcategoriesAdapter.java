package in.softment.exampracticeadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.softment.exampracticeadmin.Admin.EditSubcategoryActivity;
import in.softment.exampracticeadmin.Model.SubcategoryModel;
import in.softment.exampracticeadmin.R;

public class AdminSubcategoriesAdapter extends RecyclerView.Adapter<AdminSubcategoriesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SubcategoryModel> subcategoryModels;
    public AdminSubcategoriesAdapter(Context context,ArrayList<SubcategoryModel> subcategoryModels ) {
        this.context = context;
        this.subcategoryModels = subcategoryModels;
    }
    @NonNull
    @Override
    public AdminSubcategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_categories_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSubcategoriesAdapter.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            SubcategoryModel subcategoryModel = subcategoryModels.get(position);
            holder.name.setText(subcategoryModel.getName());
            holder.questionCount.setText(subcategoryModel.getTotalQuestions()+" Questions");
            holder.editSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, EditSubcategoryActivity.class);
                    intent.putExtra("subcategory",subcategoryModel);
                    context.startActivity(intent);

                }
            });
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, AddXLSXFileActivity.class);
//                    intent.putExtra("catId",subcategoryModel.getCatId());
//                    intent.putExtra("subCatId",subcategoryModel.getId());
//                    context.startActivity(intent);
//                }
//            });
    }

    @Override
    public int getItemCount() {
        return subcategoryModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, questionCount;
        ImageView editSubcategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            questionCount = itemView.findViewById(R.id.questionsCount);
            editSubcategory = itemView.findViewById(R.id.editSubcategory);
        }
    }
}
