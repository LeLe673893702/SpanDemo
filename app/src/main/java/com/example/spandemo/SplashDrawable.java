package com.example.spandemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by caojianbo on 2018/4/17.
 */

public class SplashDrawable extends Drawable {

  private ColorState mColorState;
  private static Bitmap bitmap;
  Paint paint;
  Matrix matrix;
  int screenWidth = 0;
  int screenHeight = 0;

  public static void release(){
    if (null != bitmap) {
      bitmap.recycle();
      bitmap = null;
    }
  }

  public SplashDrawable() {
    mColorState = new ColorState();
  }

  private void checkHeight() {
    if (screenWidth > 0 && screenHeight > 0) {
      return;
    }
    DisplayMetrics displayMetrics = MyApplication.getContext().getResources().getDisplayMetrics();
    this.screenHeight = displayMetrics.heightPixels;
    this.screenWidth = displayMetrics.widthPixels;
  }
  @Override
  public void draw(@NonNull Canvas canvas) {
    long t1 = System.currentTimeMillis();
    if (getBounds() != null && getBounds().width() > 0 && getBounds().height() > 0) {
      int width = getBounds().width();
      int height = getBounds().height();
      if (null == bitmap) {
        try {
          Options options = new Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.loading, options);
          checkHeight();
          int sampleSize = 1;
          if (options.outWidth / sampleSize > screenWidth || options.outHeight / sampleSize > screenHeight) {
            sampleSize *= 2;
          }

          options = new Options();
          options.inSampleSize = sampleSize;

          bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.loading, options);

        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
      if (null == paint) {
        paint = new Paint();
        paint.setAntiAlias(true);
      }

      if (null != bitmap) {
        if (null == matrix) {
          matrix = new Matrix();
          float scale = (float)bitmap.getWidth() / width;
          int newWidth = (int) (bitmap.getWidth() / scale);
          int newHeight = (int) (bitmap.getHeight() / scale);

          if (newHeight < height){
            scale = (float)bitmap.getHeight() / height;
            newWidth = (int) (bitmap.getWidth() / scale);
            newHeight = (int) (bitmap.getHeight() / scale);
          }
          matrix.postScale(1 / scale, 1 / scale);
          matrix.postTranslate((width - newWidth) / 2, (height - newHeight)/ 2);

        }
        canvas.drawBitmap(bitmap, matrix, paint);
      }
    }
  }

  @Override
  public void setAlpha(int alpha) {

  }

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {

  }

  @Override
  public int getOpacity() {
    return PixelFormat.UNKNOWN;
  }

  // fix bugly: #441332
  // 如果自定义Drawable会当做window的背景（getWindow().setBackgroundDrawable(BlankDrawable.getInstance());），
  // 需要重写此方法，否则在部分机型（华为 NXT-AL10，华为 MHA AL00等）上，在某些情况，调用到BackdropFrameRenderer#onResourcesLoaded中
  // 会导致getConstantState().newDrawable()报NPE。
  @Nullable
  @Override
  public ConstantState getConstantState() {
    return null;
  }

  final static class ColorState extends ConstantState {

    @NonNull
    @Override
    public Drawable newDrawable() {
      return new SplashDrawable();
    }

    @Override
    public int getChangingConfigurations() {
      return 0;
    }
  }

}
