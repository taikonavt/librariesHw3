package com.example.maxim.librarieshw3.view;

public interface MainView {

    void pickImage();

    void showConvertingCompletedMessage();

    void showConvertingErrorMessage();

    void showConvertingDialog();

    void dismissConvertingDialog();

    void showConvertingCanceledMessage();
}
