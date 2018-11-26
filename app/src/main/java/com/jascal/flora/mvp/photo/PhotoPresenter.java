package com.jascal.flora.mvp.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.jascal.flora.base.BasePresenter;
import com.jascal.flora.cache.file.StorageHelper;
import com.jascal.flora.mvp.photo.model.TensorModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PhotoPresenter extends BasePresenter implements PhotoContract.Presenter {
    private PhotoContract.View view;

    public static final int DRAK_MODEL = 0;

    public PhotoPresenter(PhotoContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void convert(Uri uri, final Context context, int model) {
        switch (model) {
            case DRAK_MODEL:
                TensorModel tensorModel = new TensorModel(context);
                tensorModel.setCallback(new TensorModel.Callback() {
                    @Override
                    public void onSuccess(Bitmap result) {
                        view.setPhoto(StorageHelper.saveBitmap(context, result, getRandomFileName()+".jpg"));
                    }

                    @Override
                    public void onFailure(Uri uri, Throwable throwable) {
                        view.errorMsg(throwable.getMessage());
                    }

                    @Override
                    public void onCancel(Uri uri) {
                        view.errorMsg("canceled.");
                    }
                });
                tensorModel.convert(uri);
                break;
        }
    }

    public String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return rannum + str;// 当前时间
    }
}
