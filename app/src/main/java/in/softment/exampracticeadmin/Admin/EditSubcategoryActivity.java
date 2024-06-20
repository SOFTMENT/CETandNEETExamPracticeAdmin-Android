package in.softment.exampracticeadmin.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import in.softment.exampracticeadmin.Model.SubcategoryModel;
import in.softment.exampracticeadmin.R;
import in.softment.exampracticeadmin.Util.ProgressHud;
import in.softment.exampracticeadmin.Util.Services;


public class EditSubcategoryActivity extends AppCompatActivity {

    private SubcategoryModel subcategoryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subcategory);

        subcategoryModel = (SubcategoryModel) getIntent().getSerializableExtra("subcategory");

        if (subcategoryModel == null) {
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

        subcategoryName.setText(subcategoryModel.getName());
        questionQuantity.setText(subcategoryModel.getTotalQuestions()+"");

        findViewById(R.id.addSubcategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sSubCategory = subcategoryName.getText().toString().trim();
                String sQuestionQuantity =  questionQuantity.getText().toString().trim();
                if (sSubCategory.isEmpty()) {
                    Services.showCenterToast(EditSubcategoryActivity.this,"Enter Subcategory Name");
                }
                else if (sQuestionQuantity.isEmpty()) {
                    Services.showCenterToast(EditSubcategoryActivity.this,"Enter Questions Quantity");
                }
                else {

                    subcategoryModel.name = sSubCategory;
                    subcategoryModel.totalQuestions = Integer.parseInt(sQuestionQuantity);
                    updateDataOnFirebase(subcategoryModel);
                }

            }
        });

        //DELETESUBCATEGORY
        findViewById(R.id.deleteSubCat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditSubcategoryActivity.this,R.style.AlertDialogTheme);
                builder.setTitle("DELETE");
                builder.setMessage("Are you sure you want to delete this subcategory?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteSubcategory();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    private void updateDataOnFirebase(SubcategoryModel subcategoryModel){

        FirebaseFirestore.getInstance().collection("Categories").document(subcategoryModel.getCatId())
                .collection("Subcategories")
                .document(subcategoryModel.getId()).set(subcategoryModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    Services.showCenterToast(EditSubcategoryActivity.this,"Subcategory Edited");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2000);

                }
                else {
                    Services.showDialog(EditSubcategoryActivity.this,"ERROR",task.getException().getLocalizedMessage());
                }
            }
        });

    }

    private void deleteSubcategory() {
            ProgressHud.show(this,"Deleting...");
            FirebaseFirestore.getInstance().collection("Categories").document(subcategoryModel.getCatId()).collection("Subcategories").document(subcategoryModel.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ProgressHud.dialog.dismiss();
                    if (task.isSuccessful()) {
                        Services.showCenterToast(EditSubcategoryActivity.this,"Deleted");
                        updateCategoryQuizCount();
                    }
                    else {
                        Services.showDialog(EditSubcategoryActivity.this,"ERROR",task.getException().getLocalizedMessage());
                    }
                }
            });
    }



    private void updateCategoryQuizCount(){
        final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection("Categories").document(subcategoryModel.getCatId());
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);

                        // Note: this could be done without a transaction
                        //       by updating the population using FieldValue.increment()
                        int newPopulation = (int) (snapshot.getLong("totalQuiz") - 1);
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
