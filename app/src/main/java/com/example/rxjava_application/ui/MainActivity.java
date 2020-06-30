package com.example.rxjava_application.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rxjava_application.R;
import com.example.rxjava_application.pojo.Login;
import com.example.rxjava_application.retrofit.APIClient;
import com.example.rxjava_application.retrofit.APIInterface;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private EditText id_name_ed_text2, id_name_ed_text3;
    private Button   id_loginBtn;
    private String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        id_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = id_name_ed_text2.getText().toString();
                String pass  = id_name_ed_text3.getText().toString();

                if(email.isEmpty()){
                    id_name_ed_text2.requestFocus();
                    id_name_ed_text2.setError("Enter email");
                }else if(!email.matches(regex)){
                    id_name_ed_text2.requestFocus();
                    id_name_ed_text2.setError("Enter valid email");
                }else if(pass.isEmpty()) {
                    id_name_ed_text3.requestFocus();
                    id_name_ed_text3.setError("Enter password");
                }else {
                    login(email, pass);
                }

            }
        });



    }

    @SuppressLint("CheckResult")
    private void login(String email, String pass) {

        showDialog();

        apiInterface.LOGIN_OBSERVABLE(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

    }

    @Override
    protected void onDestroy() {

        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }

        super.onDestroy();
    }

    private void init() {

        id_name_ed_text2 = findViewById(R.id.id_name_ed_text2);
        id_name_ed_text3 = findViewById(R.id.id_name_ed_text3);
        id_loginBtn = findViewById(R.id.id_loginBtn);

        progressDialog = new ProgressDialog(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }


    public void showDialog(){

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissDialog(){

        progressDialog.dismiss();
    }

    private void handleResults(Login login){

        dismissDialog();

        Toast.makeText(this, "LOGIN : "+login.getMessage(),
                Toast.LENGTH_LONG).show();


        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("userID", login.getData().getUserId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private void handleError(Throwable t){

        dismissDialog();

        Log.e("Observer", ""+ t.toString());
        Toast.makeText(this, "ERROR IN GETTING RESPONSE",
                Toast.LENGTH_LONG).show();
    }
}
