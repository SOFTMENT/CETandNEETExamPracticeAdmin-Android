package in.softment.exampracticeadmin.Model;


;


public class QuestionModel {

    
    public String id;

    public String catId;

    public String questionId;
  
    public String question;
   
    public Boolean questionImage;
   
    public String optionA;
    
    public String optionB;
  
    public String optionC;
    
    public String optionD;
   
    public Boolean answerImage;
    
    public String answer;
   
    public String difficulty;
    
    public String hint;


    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(Boolean questionImage) {
        this.questionImage = questionImage;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public Boolean getAnswerImage() {
        return answerImage;
    }

    public void setAnswerImage(Boolean answerImage) {
        this.answerImage = answerImage;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
