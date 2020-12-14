package pasa.cbentley.framework.coredraw.fx.engine;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

public class CanvasSnapShotTask implements Runnable {

   private WritableImage  image;

   protected final Canvas canvas;

   protected final double pixelScale;

   public CanvasSnapShotTask(Canvas canvas, double pixelScale) {
      this.canvas = canvas;
      this.pixelScale = pixelScale;

   }
   
   public WritableImage getImage() {
      return image;
   }

   public WritableImage pixelScaleAwareCanvasSnapshot(Canvas canvas, double pixelScale) {

      int w = (int) Math.rint(pixelScale * canvas.getWidth());
      int h = (int) Math.rint(pixelScale * canvas.getHeight());
      WritableImage writableImage = new WritableImage(w, h);
      SnapshotParameters params = new SnapshotParameters();
      params.setFill(Color.TRANSPARENT);
      params.setTransform(Transform.scale(pixelScale, pixelScale));
      return canvas.snapshot(params, writableImage);
   }

   public void run() {
      image = pixelScaleAwareCanvasSnapshot(canvas, pixelScale);
   }
}
