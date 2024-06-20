package in.softment.exampracticeadmin.Interface;

import java.util.ArrayList;

import in.softment.exampracticeadmin.Model.QuestionModel;

public interface QuestionsListener {
  void onCallback(ArrayList<QuestionModel>  qusModels, String error);
}
