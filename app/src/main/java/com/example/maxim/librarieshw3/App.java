package com.example.maxim.librarieshw3;

import android.app.Application;

public class App extends Application {

    private static App app;

    public App(){
        app = this;
    }

    public static App getInstance(){
        return app;
    }
}
