package moe.laysionqet.support.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import moe.laysionqet.support.utils.memory.bitmap_recycle.BitmapPool;
import moe.laysionqet.support.utils.memory.leak.anti.IMMLeaks;
import moe.laysionqet.support.utils.memory.leak.anti.UIMemoryRecycler;
import moe.laysionqet.support.utils.memory.leak.watcher.MemoryLeakWatcher;

/**
 * Created by laysionqet on 16/4/28.
 */
public class SupportApp extends Application {
  private static Context sContext = null;

  @Override public void onCreate() {
    sContext = this;
    SupportApplicationContext.setup(this);
    super.onCreate();

    MemoryLeakWatcher.install(this);
    installUIMemoryRecycler();
    IMMLeaks.fixFocusedViewLeak(this);

    BitmapPool.get();
  }

  public static Context getContext() {
    return sContext;
  }

  protected void installUIMemoryRecycler() {
    registerActivityLifecycleCallbacks(new ActivityLifeCycleCallbacksAdapter() {
      @Override public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);

        UIMemoryRecycler.onActivityDestroy(activity);
      }
    });
  }
}
