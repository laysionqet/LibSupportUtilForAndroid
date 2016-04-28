package moe.laysionqet.support;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import moe.laysionqet.support.utils.antileak.IMMLeaks;
import moe.laysionqet.support.utils.antileak.UIMemoryRecycler;

/**
 * Created by laysionqet on 16/4/28.
 */
public class SupportApp extends Application {
  @Override public void onCreate() {
    super.onCreate();

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

  public static class ActivityLifeCycleCallbacksAdapter implements ActivityLifecycleCallbacks {
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override public void onActivityStarted(Activity activity) {

    }

    @Override public void onActivityResumed(Activity activity) {

    }

    @Override public void onActivityPaused(Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {

    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override public void onActivityDestroyed(Activity activity) {

    }
  }
}
