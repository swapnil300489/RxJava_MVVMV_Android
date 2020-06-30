package com.example.rxjava_application.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.rxjava_application.R;
import com.example.rxjava_application.adapter.PolicyAdapter;
import com.example.rxjava_application.pojo.PastPolicy;
import com.example.rxjava_application.retrofit.APIClient;
import com.example.rxjava_application.retrofit.APIInterface;
import com.example.rxjava_application.viewmodel.RxJava_ViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ListActivity extends AppCompatActivity {

    private RecyclerView id_RCList;
    private String userID = "";
    private ProgressDialog progressDialog;
    private APIInterface apiInterface;
    private CompositeDisposable compositeDisposable;
    private RxJava_ViewModel rxJava_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        init();

    }

    private void init() {

        userID = getIntent().getStringExtra("userID");

        id_RCList = findViewById(R.id.id_RCList);
        id_RCList.setLayoutManager(new LinearLayoutManager(this));

        rxJava_viewModel = ViewModelProviders.of(this).get(RxJava_ViewModel.class);

        apiInterface   = APIClient.getClient().create(APIInterface.class);
        progressDialog = new ProgressDialog(this);
        getList(userID);


    }

    private void getList(String userID) {

        showDialog();
        rxJava_viewModel.getPolicyList(apiInterface, userID).observe(this, new Observer<PastPolicy>() {
            @Override
            public void onChanged(PastPolicy pastPolicy) {

                dismissDialog();

                if (pastPolicy.getMessageCode() == 1){

                    List<PastPolicy.Datum> data = pastPolicy.getData();

                    PolicyAdapter policyAdapter = new PolicyAdapter(ListActivity.this, data);
                    id_RCList.setAdapter(policyAdapter);
                }


            }
        });

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
}
