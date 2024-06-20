package in.softment.exampracticeadmin.Interface;

import java.util.ArrayList;

import in.softment.exampracticeadmin.Model.CategoryModel;

public interface CategoriesListener {

    public void onCallback(ArrayList<CategoryModel> categoryModels, String error);
}
