package in.softment.exampracticeadmin.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import in.softment.exampracticeadmin.Admin.AdminDashboardActivity;
import in.softment.exampracticeadmin.Interface.CategoriesListener;
import in.softment.exampracticeadmin.Interface.QuestionsListener;
import in.softment.exampracticeadmin.Interface.SubcategoriesListeners;
import in.softment.exampracticeadmin.Model.CategoryModel;
import in.softment.exampracticeadmin.Model.QuestionModel;
import in.softment.exampracticeadmin.Model.SubcategoryModel;
import in.softment.exampracticeadmin.Model.UserModel;
import in.softment.exampracticeadmin.R;



public class Services {




    public static  String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
    public static boolean isPromoting(Date date){
        Date currentDate = new Date();
        if (currentDate.compareTo(date) < 0) {
            return true;
        }
        else {
            return false;
        }
    }



    public static Date convertTimeToDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        return cal.getTime();
    }

    public static void sentPushNotificationToAdmin(Context context, String title, String message) {
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAqBzSPAk:APA91bEGVz80gPuqdqNp0CzMZ5tdmRui4XFfSj6bPDnQ9AStQM-NhkRHeCNXrx8USkxdb97U9BLWv8U2Ri1UITA-TID3RPc4BXYne5nmF3GeB9p4XkbiIVjIQdo7G7alXkBK9hpEAI5T";
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);
            notification.put("to", "/topics/vipnashville");
            notification.put("data", notifcationBody);
        } catch (JSONException ignored) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    public static void sentPushNotification(Context context,String title, String message, String token) {
        final String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAqBzSPAk:APA91bEGVz80gPuqdqNp0CzMZ5tdmRui4XFfSj6bPDnQ9AStQM-NhkRHeCNXrx8USkxdb97U9BLWv8U2Ri1UITA-TID3RPc4BXYne5nmF3GeB9p4XkbiIVjIQdo7G7alXkBK9hpEAI5T";
        final String contentType = "application/json";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);
            notification.put("to", token);
            notification.put("data", notifcationBody);
        } catch (JSONException ignored) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    public static  String convertDateToString(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd-MMM-yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static  String convertDateToStringWithoutDash(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd MMM yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static  String convertDateToTimeString(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd-MMM-yyyy, hh:mm a";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static void showCenterToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }



    public static String toUpperCase(String str) {
        if (str.isEmpty()){
            return "";
        }
        String[] names = str.trim().split(" ");
        str = "";
        for (String name : names) {
            try {
                str += name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + " ";
            }
            catch (Exception ignored){

            }
        }
        return str;
    }
    public static void showDialog(Context context,String title,String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        Activity activity = (Activity) context;
        View view = activity.getLayoutInflater().inflate(R.layout.error_message_layout, null);
        TextView titleView = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        titleView.setText(title);
        msg.setText(message);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });

        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();

        }

    }

    public static void sentEmailVerificationLink(Context context){

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ProgressHud.show(context,"");
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ProgressHud.dialog.dismiss();

                    if (task.isSuccessful()) {
                        showDialog(context,"VERIFY YOUR EMAIL","We have sent verification link on your mail address.");
                    }
                    else {
                        showDialog(context,"ERROR",task.getException().getLocalizedMessage());
                    }
                }
            });
        }
        else {
            ProgressHud.dialog.dismiss();
        }

    }








    public  static Date getDateFromTimestamp(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void getQuestions(String catId, String subCatId, String limit){

    }
    public static void getAllQuestion(String catId, int limit, QuestionsListener questionsListener){
        ArrayList<QuestionModel> questionModels =  new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Categories").document(catId).collection("Questions").limit(limit).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    questionModels.clear();
                    if (value != null && !value.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            QuestionModel questionModel = documentSnapshot.toObject(QuestionModel.class);
                            questionModels.add(questionModel);
                        }

                    }
                    questionsListener.onCallback(questionModels,null);

                }
                else {
                   questionsListener.onCallback(questionModels, error.getLocalizedMessage());
                }
            }
        });

    }
    public static void getAllSubcategory(String catId,SubcategoriesListeners subcategoriesListeners){
        ArrayList<SubcategoryModel> subcategoryModels =  new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Categories").document(catId).collection("Subcategories").orderBy("name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    subcategoryModels.clear();
                    if (value != null && !value.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            SubcategoryModel subcategoryModel = documentSnapshot.toObject(SubcategoryModel.class);
                            subcategoryModels.add(subcategoryModel);
                        }

                    }
                    subcategoriesListeners.onCallback(subcategoryModels,null);

                }
                else {
                    subcategoriesListeners.onCallback(subcategoryModels, error.getLocalizedMessage());
                }
            }
        });

    }
    public static void getAllCategory(boolean withAvailableFilter, CategoriesListener categoriesListener){
        ArrayList<CategoryModel> categoryModels =  new ArrayList<>();
        Query query  = FirebaseFirestore.getInstance().collection("Categories").orderBy("name");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     if (error == null) {
                         categoryModels.clear();
                         if (value != null && !value.isEmpty()) {
                             for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                 CategoryModel categoryModel = documentSnapshot.toObject(CategoryModel.class);
                                 categoryModels.add(categoryModel);
                             }

                         }
                         categoriesListener.onCallback(categoryModels,null);

                     }
                     else {
                         categoriesListener.onCallback(categoryModels, error.getLocalizedMessage());
                     }
            }
        });

    }





}
