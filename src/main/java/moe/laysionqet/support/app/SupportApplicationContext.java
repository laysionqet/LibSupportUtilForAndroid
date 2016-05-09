package moe.laysionqet.support.app;

import android.content.Context;

/**
 * Created by laysionqet on 16/5/9.
 */
public final class SupportApplicationContext {
  private static volatile Context appContext;

  public static void setup(Context context) {
    if (null == context) {
      return;
    }
    appContext = context.getApplicationContext();
  }

  public static Context get() {
    return appContext;
  }
}
