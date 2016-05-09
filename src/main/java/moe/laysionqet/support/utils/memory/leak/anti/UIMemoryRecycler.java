package moe.laysionqet.support.utils.memory.leak.anti;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;
import moe.laysionqet.support.BuildConfig;

/**
 * Created by laysionqet on 16/4/28.
 */
public final class UIMemoryRecycler {
  private UIMemoryRecycler() {
  }

  public interface IViewRecycleFactory {
    void recycle(View view) throws Exception;
    Class getViewClass();
    String getViewClassName();
  }

  private static final Set<IViewRecycleFactory> sViewRecycleFactories = new HashSet<>();

  @MainThread public static void registerViewRecycleFactory(IViewRecycleFactory factory) {
    if (null == factory) {
      return;
    }
    if (null == factory.getViewClass() && TextUtils.isEmpty(factory.getViewClassName())) {
      return;
    }
    sViewRecycleFactories.add(factory);
  }

  @MainThread public static void onActivityDestroy(Activity activity) {
    if (null == activity) {
      return;
    }
    View decorChild = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
    if (BuildConfig.DEBUG) {
      recursiveReleaseView(decorChild);
    } else {
      try {
        recursiveReleaseView(decorChild); // catch all exceptions in release apk
      } catch (Exception ignored){
      }
    }
  }

  @MainThread public static void recursiveReleaseView(View view) {
    if (null == view) {
      return;
    }
    // listeners
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
      view.setOnApplyWindowInsetsListener(null);
    }
    try {
      if (!(view instanceof AdapterView)) {
        view.setOnClickListener(null);
      }
    } catch (Exception ignored) {
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      view.setOnContextClickListener(null);
    }
    view.setOnCreateContextMenuListener(null);
    view.setOnDragListener(null);
    view.setOnFocusChangeListener(null);
    view.setOnGenericMotionListener(null);
    view.setOnHoverListener(null);
    view.setOnKeyListener(null);
    view.setOnLongClickListener(null);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      view.setOnScrollChangeListener(null);
    }
    view.setOnSystemUiVisibilityChangeListener(null);
    view.setOnTouchListener(null);

    if (null != view.getBackground()
        /**
         * @see SwipeRefreshLayout#onDetachedFromWindow()
         */
        && !view.getClass().getName().equals("android.support.v4.widget.CircleImageView")) {
      view.getBackground().setCallback(null);
      view.setBackgroundDrawable(null);
    }

    if (view instanceof ImageView) {
      ImageView image = (ImageView) view;
      if (null != image.getDrawable()) {
        image.getDrawable().setCallback(null);
      }
      image.setImageDrawable(null);
      image.setImageBitmap(null);
    }

    if (view instanceof TextView) {
      ((TextView) view).setCompoundDrawables(null, null, null, null);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        ((TextView) view).setCompoundDrawablesRelative(null, null, null, null);
      }
    }

    try {
      if (view instanceof WebView) {
        try {
          ((WebView) view).stopLoading();
        } catch (Exception ignored) {
        }
        try {
          ((WebView) view).removeAllViews();
        } catch (Exception ignored) {
        }
        try {
          ((WebView) view).setWebChromeClient(null);
        } catch (Exception ignored) {
        }
        try {
          ((WebView) view).setWebViewClient(null);
        } catch (Exception ignored) {
        }
        try {
          ((WebView) view).destroy();
        } catch (Exception ignored) {
        }
        try {
          if (null != view.getParent() && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
          }
        } catch (Exception ignored) {
        }
      }
    } catch (Exception ignored) {
    }

    if (view instanceof SurfaceView) {
      SurfaceView surfaceView = (SurfaceView) view;
      SurfaceHolder holder = surfaceView.getHolder();
      if (null != holder) {
        Surface surface = holder.getSurface();
        if (null != surface) {
          surface.release();
        }
      }
    }

    for (IViewRecycleFactory factory : sViewRecycleFactories) {
      if (factory.getViewClass() == view.getClass()
          || view.getClass().getName().equals(factory.getViewClassName())) {
        try {
          factory.recycle(view);
        } catch (Exception ignored) {
        }
      }
    }

    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        recursiveReleaseView(((ViewGroup) view).getChildAt(i));
      }
    }

    if (view instanceof ListView) {
      ListView listView = (ListView) view;
      listView.setAdapter(null);
      listView.setOnItemClickListener(null);
      listView.setOnLongClickListener(null);
      listView.setOnItemSelectedListener(null);
    }
    if (view instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) view;
      recyclerView.setAdapter(null);
      recyclerView.setChildDrawingOrderCallback(null);
      recyclerView.setOnScrollListener(null);
      recyclerView.setRecyclerListener(null);
    }

    view.destroyDrawingCache();
    view.clearAnimation();
  }

  public static void safeRecycleBitmap(Bitmap bitmap) {
    if (null == bitmap || bitmap.isRecycled()) {
      return;
    }
    bitmap.recycle();
  }
}

