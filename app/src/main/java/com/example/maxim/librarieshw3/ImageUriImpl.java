package com.example.maxim.librarieshw3;

import android.net.Uri;

public class ImageUriImpl implements IImageUri {

    private Uri uri;

    public ImageUriImpl(Uri uri){
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}
