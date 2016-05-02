package moe.laysionqet.support.utils.memory.bitmap_recycle;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import moe.laysionqet.support.app.SupportApp;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * Created by laysionqet on 16/5/3.
 */
public final class BitmapPool implements com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool {
  @Override public int getMaxSize() {
    return internal.getMaxSize();
  }

  @Override public void setSizeMultiplier(float sizeMultiplier) {
    internal.setSizeMultiplier(sizeMultiplier);
  }

  @Override public boolean put(Bitmap bitmap) {
    return internal.put(bitmap);
  }

  @Override public Bitmap get(int width, int height, Bitmap.Config config) {
    return internal.get(width, height, config);
  }

  @Override public Bitmap getDirty(int width, int height, Bitmap.Config config) {
    return internal.getDirty(width, height, config);
  }

  @Override public void clearMemory() {
    internal.clearMemory();
  }

  @Override public void trimMemory(int level) {
    internal.trimMemory(level);
  }

  private static final class SINGLETON {
    private static final BitmapPool INSTANCE = new BitmapPool(SupportApp.getContext());
  }

  public static BitmapPool get() {
    return SINGLETON.INSTANCE;
  }

  private final com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool internal;
  private final MemorySizeCalculator memorySizeCalculator;

  private BitmapPool(Context context) {
    context = context.getApplicationContext();
    this.memorySizeCalculator = new MemorySizeCalculator(context);

    if (SDK_INT >= HONEYCOMB) {
      int size = memorySizeCalculator.getBitmapPoolSize();
      internal = new LruBitmapPool(size);
    } else {
      internal = new BitmapPoolAdapter();
    }
  }

  public final MemorySizeCalculator getMemorySizeCalculator() {
    return memorySizeCalculator;
  }
}
