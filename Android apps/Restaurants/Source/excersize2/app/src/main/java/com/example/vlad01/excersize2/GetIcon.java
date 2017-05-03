package com.example.vlad01.excersize2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.BufferedInputStream;

public  class GetIcon extends GetJson
{
    ImageView myImageView = null;

    public GetIcon(ImageView imageView, FragmentActivity activity)
    {
        super(activity);
        myImageView = imageView;
    }

    @Override
    protected Object getDataFromStream(BufferedInputStream in) {
        //BufferedInputStream bis = new BufferedInputStream(in);
        Bitmap bm = BitmapFactory.decodeStream(in);
        return bm;
    }

    @Override
    protected void onPostExecuteImpl(Object o) throws Exception {
        Bitmap result;
        if (o instanceof Bitmap) {
            result = (Bitmap) o;
            myImageView.setImageBitmap(result);
        }
        else if (o instanceof Exception)
            throw (Exception) o;
        else {
            result = null; //possibly we should log an error?
        }

    }
}
