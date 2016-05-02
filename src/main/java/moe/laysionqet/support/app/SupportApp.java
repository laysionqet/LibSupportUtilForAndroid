package moe.laysionqet.support.app;

import android.app.Activity;
import android.app.Application;
import moe.laysionqet.support.utils.memory.leak.anti.IMMLeaks;
import moe.laysionqet.support.utils.memory.leak.anti.UIMemoryRecycler;
import moe.laysionqet.support.utils.memory.leak.watcher.MemoryLeakWatcher;

/**
 * Created by laysionqet on 16/4/28.
 */
public class SupportApp extends Application {
  @Override public void onCreate() {
    super.onCreate();
    MemoryLeakWatcher.install(this);
    installUIMemoryRecycler();
    IMMLeaks.fixFocusedViewLeak(this);
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
