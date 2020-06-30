package com.example.rxjava_application.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rxjava_application.pojo.Login;
import com.example.rxjava_application.pojo.PastPolicy;
import com.example.rxjava_application.retrofit.APIInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxJava_ViewModel extends ViewModel {

    private MutableLiveData<PastPolicy> pastPolicyMutableLiveData;

    public LiveData<PastPolicy> getPolicyList(APIInterface apiInterface, String userId){

        if (pastPolicyMutableLiveData == null){

            pastPolicyMutableLiveData = new MutableLiveData<PastPolicy>();

            getPolicyDetails(apiInterface, userId);

        }

        return pastPolicyMutableLiveData;
    }

    private void getPolicyDetails(APIInterface apiInterface, String userId) {


            apiInterface.PAST_POLICY_OBSERVABLE(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handlePolicyResults, this::handlePolicyError);

    }

    private void handlePolicyResults(PastPolicy pastPolicy){


        pastPolicyMutableLiveData.setValue(pastPolicy);


    }
    private void handlePolicyError(Throwable t){

        Log.e("Observer", ""+ t.toString());

    }


}
