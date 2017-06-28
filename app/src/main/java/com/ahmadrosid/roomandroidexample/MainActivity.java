package com.ahmadrosid.roomandroidexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private Spinner spinnerLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Message");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        spinnerLevel = (Spinner) findViewById(R.id.spinnerLevel);

        ApiLogin.getSpinnerData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseSpinnerData>() {

                    @Override public void accept(ResponseSpinnerData responseSpinnerData) throws Exception {
                        List<String> mData = new ArrayList<String>();
                        for (ResponseSpinnerData.DataBean bean : responseSpinnerData.getData()) {
                            mData.add(bean.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                android.R.layout.simple_spinner_item, mData);
                        spinnerLevel.setAdapter(adapter);
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        hideLoading();
                    }
                });

    }
    private void showLoading() {
        progressDialog.show();
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }


}
