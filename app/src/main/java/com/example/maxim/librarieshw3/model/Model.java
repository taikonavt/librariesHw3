package com.example.maxim.librarieshw3.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.example.maxim.librarieshw3.App;
import com.example.maxim.librarieshw3.ImageUriImpl;
import com.example.maxim.librarieshw3.presenter.MainPresenter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Model {

    private MainPresenter presenter;
    private Disposable disposable;

    public Model(MainPresenter presenter){
        this.presenter = presenter;
    }

    public void convertJpgToPng(ImageUriImpl imageUri){
        Completable completable = Completable.fromAction(() -> convert(imageUri));
        presenter.convertingStarted();
        completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onComplete() {
                        presenter.convertingCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        presenter.convertingError();
                    }
                });
    }

    private void convert(ImageUriImpl imageUri) throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        InputStream inputStream =
                App.getInstance()
                        .getContentResolver()
                        .openInputStream(imageUri.getUri());

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        String fileName = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "result.png";
        Uri resultUri = Uri.fromFile(new File(fileName));

        OutputStream outputStream =
                        App.getInstance()
                                .getContentResolver()
                                .openOutputStream(resultUri);

        boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
    }

    public void cancelConverting() {
        disposable.dispose();
    }
}
