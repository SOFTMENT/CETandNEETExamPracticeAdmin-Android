package in.softment.exampracticeadmin.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.softment.exampracticeadmin.Model.QuestionModel;
import in.softment.exampracticeadmin.R;
import in.softment.exampracticeadmin.Util.ProgressHud;
import in.softment.exampracticeadmin.Util.Services;

public class AddXLSXFileActivity extends AppCompatActivity {

    private AppCompatButton selectFileBtn;
    private String catId  = null;
    List<QuestionModel> questionModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xlsxfile);

        catId = getIntent().getStringExtra("catId");



        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Categories").document(catId).collection("Questions");

        selectFileBtn = findViewById(R.id.selectFile);

        selectFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 7);
            }
        });

        findViewById(R.id.uploadFile).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (questionModels == null || questionModels.isEmpty()){
                    Services.showCenterToast(AddXLSXFileActivity.this,"Add XLSX FILE");
                    return;
                }

                ProgressHud.show(AddXLSXFileActivity.this,"Uploading...");
                int x = (questionModels.size() / 500) + (questionModels.size() % 500 == 0 ? 0 : 1) ;
                int count = 0;
                for (int i = 0 ; i < x ; i++) {



                        int nextLoopCount = Math.min((questionModels.size() - count), 500);

                       WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
                        for (int y = 0; y < nextLoopCount; y++) {

                            QuestionModel questionModel = questionModels.get(count);
                            questionModel.setCatId(catId);
                            questionModel.id = collectionReference.document().getId();
                            writeBatch.set(collectionReference.document(questionModel.id),questionModel);
                            count++;

                        }
                    int finalI = i;
                    writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                    if (finalI == x - 1) {
                                        ProgressHud.dialog.dismiss();
                                    }
                                    if (task.isSuccessful()) {
                                        if (finalI == x - 1) {
                                            Services.showCenterToast(AddXLSXFileActivity.this,"Data Successfully Uploaded :)");
                                        }

                                    }
                                    else {
                                        Services.showDialog(AddXLSXFileActivity.this,"ERROR",task.getException().getLocalizedMessage());
                                    }
                            }
                        });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    selectFileBtn.setText("FILE SELECTED");
                    selectFileBtn.setBackgroundResource(R.drawable.selected_xlsx_file_back);
                    selectFileBtn.setTextColor(getResources().getColor(R.color.white));



                    String filename = data.getData().getPath().substring(data.getData().getPath().lastIndexOf(":")+1);

                    File TEST = new File(Environment.getExternalStorageDirectory(), filename);
                    TEST.mkdir(); // make directory may want to check return value
                    String path = TEST.getAbsolutePath(); // get absolute path

                    try {
                        InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
                        questionModels= readAssetExcel(inputStream);
                        Services.showCenterToast(AddXLSXFileActivity.this,"File Selected");
                    } catch (FileNotFoundException e) {
                       Services.showDialog(AddXLSXFileActivity.this,"ERROR",e.getLocalizedMessage());
                    }
                }
                break;

        }
    }

//create pojo class based on excel sheet columns

        public List<QuestionModel> readAssetExcel(
                final InputStream inputStream) {
            List<QuestionModel> cellValues = new ArrayList<QuestionModel>();

            XSSFWorkbook workbook;
            try {

                workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                cellValues = damcellValues(sheet, cellValues);
                return cellValues;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return cellValues;

        }

        private List<QuestionModel> damcellValues(XSSFSheet sheet,
                                                  List<QuestionModel> cellValues) {

            int num = sheet.getPhysicalNumberOfRows();


            for (int i = 1; i < num; i++) {

                XSSFRow column = sheet.getRow(i);


                if (column.getCell(0) == null) {
                    break;
                }

                QuestionModel object = new QuestionModel();

                int str1 = (int) column.getCell(0).getNumericCellValue();
                object.setQuestionId(String.valueOf(str1));

                String str2 = column.getCell(1).getStringCellValue();
                object.setQuestion(str2);

                Boolean str3 = column.getCell(2).getBooleanCellValue();
                object.setQuestionImage(str3);

                String str4 = column.getCell(3).getStringCellValue();
                object.setOptionA(str4);

                String str5 = column.getCell(4).getStringCellValue();
                object.setOptionB(str5);

                String str6= column.getCell(5).getStringCellValue();
                object.setOptionC(str6);

                String str7 = column.getCell(6).getStringCellValue();
                object.setOptionD(str7);

                Boolean str8= column.getCell(7).getBooleanCellValue();
                object.setAnswerImage(str8);

                String str9 = column.getCell(8).getStringCellValue();
                object.setAnswer(str9);

                String str10 = column.getCell(9).getStringCellValue();
                object.setDifficulty(str10);

                String str11 = column.getCell(10).getStringCellValue();
                object.setHint(str11);

                cellValues.add(object);
            }

            return cellValues;

        }


}
