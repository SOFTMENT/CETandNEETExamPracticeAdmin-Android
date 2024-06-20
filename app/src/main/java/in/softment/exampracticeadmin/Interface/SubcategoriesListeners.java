package in.softment.exampracticeadmin.Interface;

import java.util.ArrayList;

import in.softment.exampracticeadmin.Model.SubcategoryModel;


public interface SubcategoriesListeners {

    public void onCallback(ArrayList<SubcategoryModel> subcategoryModels, String error);
}
