package com.example.maxim.librarieshw3.presenter;

import com.example.maxim.librarieshw3.ImageUriImpl;
import com.example.maxim.librarieshw3.model.Model;
import com.example.maxim.librarieshw3.view.MainView;

public class MainPresenter {
    private MainView mainView;
    private Model model;

    public MainPresenter(MainView mainView){
        this.mainView = mainView;
        model = new Model(this);
    }

    public void onButtonClicked() {
        mainView.pickImage();
    }

    public void convertImage(ImageUriImpl imageUri){
        model.convertJpgToPng(imageUri);
    }

    public void convertingCompleted() {
        mainView.dismissConvertingDialog();
        mainView.showConvertingCompletedMessage();
    }

    public void convertingError() {
        mainView.dismissConvertingDialog();
        mainView.showConvertingErrorMessage();
    }

    public void convertingStarted() {
        mainView.showConvertingDialog();
    }

    public void onConvertingCanceled() {
        model.cancelConverting();
        mainView.dismissConvertingDialog();
        mainView.showConvertingCanceledMessage();
    }
}
