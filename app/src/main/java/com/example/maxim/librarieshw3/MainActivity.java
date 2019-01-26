package com.example.maxim.librarieshw3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.maxim.librarieshw3.presenter.MainPresenter;
import com.example.maxim.librarieshw3.view.MainView;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity
        implements MainView {

    public static final String myTag = "myTag";
    public static final int PICK_IMG_REQUEST_CODE = 47;
    public static final int PERMISSION_REQUEST_CODE = 12;
    public static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private MainPresenter presenter;

    private View mainView;
    private Button button;
    private AlertDialog convertingDialog;

    Disposable buttonDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        initView();
        setListeners();
    }

    private void initView(){
        mainView = findViewById(R.id.mainView);
        button = (Button) findViewById(R.id.btn_convert_file);
    }

    private void setListeners() {
        buttonDisp =
                RxView.clicks(button).subscribe(o -> {
                    presenter.onButtonClicked();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        buttonDisp.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case PICK_IMG_REQUEST_CODE:{
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ImageUriImpl imageUri = new ImageUriImpl(data.getData());
                    presenter.convertImage(imageUri);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImage();
                } else {
                    Snackbar.make(mainView, R.string.permissions_denied, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void pickImage() {
        if (!checkPermission()) {
            askPermission();
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(takeFlags);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMG_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, permissions, 11);
    }

    @Override
    public void showConvertingCompletedMessage() {
        Snackbar.make(mainView, R.string.complete_msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showConvertingErrorMessage() {
        Snackbar.make(mainView, R.string.error_msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showConvertingCanceledMessage() {
        Snackbar.make(mainView, R.string.cancel_msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showConvertingDialog() {
        convertingDialog = new AlertDialog.Builder(this)
                .setNegativeButton(R.string.cancel, (dialog, which) -> presenter.onConvertingCanceled())
                .setMessage(R.string.converting_in_progress)
                .create();
        convertingDialog.show();

    }

    @Override
    public void dismissConvertingDialog() {
        if (convertingDialog != null && convertingDialog.isShowing()){
            convertingDialog.dismiss();
        }
    }
}
