package moe.laysionqet.support.utils.memory.leak.watcher;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by laysionqet on 16/4/29.
 */
public final class MemoryLeakWatcher {
  private MemoryLeakWatcher() {}

  private static RefWatcher sRefWatcher;

  public static void install(Application app) {
    sRefWatcher = LeakCanary.install(app);
  }

  public static void watch(Object obj) {
    if (null != sRefWatcher) {
      sRefWatcher.watch(obj);
    }
  }

}
