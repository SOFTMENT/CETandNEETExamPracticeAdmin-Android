package in.softment.exampracticeadmin.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import in.softment.exampracticeadmin.Model.SubcategoryModel;
import in.softment.exampracticeadmin.R;
import in.softment.exampracticeadmin.Util.ProgressHud;
import in.softment.exampracticeadmin.Util.Services;

public class AddSubcategoryActivity extends AppCompatActivity {


    String catId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subcategories);

        catId = getIntent().getStringExtra("catId");
        if (catId == null || catId.isEmpty()) {
            finish();
        }


        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        EditText subcategoryName = findViewById(R.id.subcategoryNameET);
        EditText questionQuantity = findViewById(R.id.qusQuantityET);

        findViewById(R.id.addSubcategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sSubCategory = subcategoryName.getText().toString().trim();
                String sQuestionQuantity =  questionQuantity.getText().toString().trim();
                if (sSubCategory.isEmpty()) {
                    Services.showCenterToast(AddSubcategoryActivity.this,"Enter Subcategory Name");
                }
                else if (sQuestionQuantity.isEmpty()) {
                    Services.showCenterToast(AddSubcategoryActivity.this,"Enter Questions Quantity");
                }
                else {
                    SubcategoryModel subcategoryModel = new SubcategoryModel();
                    subcategoryModel.name = sSubCategory;
                    subcategoryModel.catId = catId;
                    subcategoryModel.totalQuestions = Integer.parseInt(sQuestionQuantity);
                    subcategoryModel.id = FirebaseFirestore.getInstance().collection("Subcategories").document().getId();
                    updateDataOnFirebase(subcategoryModel);
                }

            }
        });
    }
    private void updateDataOnFirebase(SubcategoryModel subcategoryModel){

        FirebaseFirestore.getInstance().collection("Categories").document(catId).collection("Subcategories").document(subcategoryModel.getId()).set(subcategoryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    Services.showCenterToast(AddSubcategoryActivity.this,"Subcategory Added");
                    updateCategoryQuizCount();
                }
                else {
                    Services.showDialog(AddSubcategoryActivity.this,"ERROR",task.getException().getLocalizedMessage());
                }
            }
        });

    }

    private void updateCategoryQuizCount(){
        final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection("Categories").document(catId);
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        // Note: this could be done without a transaction
                        //       by updating the population using FieldValue.increment()
                        int newPopulation = (int) (snapshot.getLong("totalQuiz") + 1);
                        transaction.update(sfDocRef, "totalQuiz", newPopulation);
                        ProgressHud.dialog.dismiss();
                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },2000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
