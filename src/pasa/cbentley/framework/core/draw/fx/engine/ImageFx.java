package pasa.cbentley.framework.core.draw.fx.engine;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.draw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.ImageJ2se;

/**
 * In JavaFX you have to write to a {@link Canvas}.
 * <br>
 * 
 * <br>
 * <br>
 * @author Charles-Philip Bentley
 *
 */
public class ImageFx extends ImageJ2se {

   private Canvas          canvas;

   private CoreDrawFxCtx   cdc;

   /** 
    * Each image object has an associated Graphics object. 
    * 
    * Lazily created.
    */
   private GraphicsFx      graphics;

   private int             h;

   /**
    * Unlike Swing, we don't have a {@link BufferedImage}.
    * 
    * Can be null when drawing on a Canvas.
    */
   private Image           image;

   /** 
    * Can be null. Used for writing on the image
    */
   protected WritableImage imageWritable;

   private int             w;

   /**
    * Constructor for creating an Image from an InputStream of image data.  
    * <br>
    * Image is immutable
    * @param is cannot be null
    */
   public ImageFx(CoreDrawFxCtx cdc, InputStream is) {
      super(cdc);
      this.cdc = cdc;
      //#debug
      cdc.toStringCheckNull(is);

      this.isMutable = false;
      image = new Image(is);
      w = (int) image.getWidth();
      h = (int) image.getHeight();
      try {
      } catch (Exception ex) {
         //#debug
         toDLog().pEx("while loading image stream", null, ImageFx.class, "constructor", ex);
         ex.printStackTrace();
      }
      //#debug
      toStringNameDebug = "ImageSwing InputStream";
   }

   /**
    * 
    * @return
    */
   public Image getImageFx() {
      return image;
   }

   /**
    * Constructor for creating a new Image with a given width and height.
    * <br>
    * Color will be opaque white pixels.
    * @param w
    * @param h
    */
   public ImageFx(CoreDrawFxCtx cdc, int w, int h) {
      this(cdc, w, h, cdc.getColorImageBackgroundDefault());
   }

   public ImageFx(CoreDrawFxCtx cdc, int w, int h, int color) {
      this(cdc, w, h, color, 0);
   }

   public ImageFx(CoreDrawFxCtx cdc, int w, int h, int color, int renderingHints) {
      super(cdc);
      this.cdc = cdc;
      this.isMutable = true;
      this.isEmpty = true;
      this.w = w;
      this.h = h;
      this.colorBackground = color;
      //#debug
      toStringNameDebug = "ImageSwing  w" + w + " h" + h + " color=" + color;
   }

   /**
    * Called when create an Image from RGB values.
    * How can you create a Graphics object from this?
    * <br>
    * Case of mutability without a graphics?
    * @param bi
    * @param cdc
    */
   public ImageFx(CoreDrawFxCtx cdc, WritableImage bi) {
      super(cdc);
      this.cdc = cdc;
      //#debug
      cdc.toStringCheckNull(bi);

      this.imageWritable = bi;
      this.image = bi;
      this.isMutable = true;
      w = (int) bi.getWidth();
      h = (int) bi.getHeight();

      //#debug
      toStringNameDebug = "ImageSwing WritableImage";
   }

   public GraphicsFx getGraphics() {
      if (!isMutable) {
         throw new IllegalStateException();
      }
      if (graphics == null) {
         canvas = new Canvas(w, h);
         GraphicsContext gc = canvas.getGraphicsContext2D();
         graphics = new GraphicsFx(cdc, gc);

         graphics.setClip(0, 0, w, h);
      }
      return graphics;
   }

   public int getHeight() {
      return h;
   }

   /**
    * 
    * @return
    * @throws IllegalStateException if nothing to paint
    */
   public Image getImageToPaint() {
      if (image == null) {
         if (imageWritable == null) {
            if (canvas == null) {
               //nothing to 
               throw new IllegalStateException();
            }
            imageWritable = pixelScaleAwareCanvasSnapshot(canvas, 1.0);
         }
         //what if the image writable was written on it with a canvas?
         return imageWritable;
      } else {
         return image;
      }
   }

   public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
      if (width <= 0 || height <= 0)
         return;
      if (x < 0 || y < 0 || x + width > getWidth() || y + height > getHeight())
         throw new IllegalArgumentException("Specified area exceeds bounds of image");
      if ((scanlength < 0 ? -scanlength : scanlength) < width)
         throw new IllegalArgumentException("abs value of scanlength is less than width");
      if (rgbData == null)
         throw new NullPointerException("null rgbData");
      if (offset < 0 || offset + width > rgbData.length)
         throw new ArrayIndexOutOfBoundsException();
      if (scanlength < 0) {
         if (offset + scanlength * (height - 1) < 0)
            throw new ArrayIndexOutOfBoundsException();
      } else {
         if (offset + scanlength * (height - 1) + width > rgbData.length)
            throw new ArrayIndexOutOfBoundsException();
      }

      Image image = getImageToPaint();
      WritablePixelFormat pf = WritablePixelFormat.getIntArgbPreInstance();
      image.getPixelReader().getPixels(x, y, width, height, pf, rgbData, offset, scanlength);
   }

   public int getWidth() {
      return w;
   }

   /**
    * Must be call within the JavaFX thread.
    * If we are not in the thread.
    * 
    * http://news.kynosarges.org/2014/05/01/simulating-platform-runandwait/
    * 
    * @param canvas
    * @param pixelScale
    * @return
    */
   public WritableImage pixelScaleAwareCanvasSnapshot(Canvas canvas, double pixelScale) {
      CanvasSnapShotTask task = new CanvasSnapShotTask(canvas, 1.0);
      cdc.getFC().runAndWait(task);
      WritableImage imageWritable = task.getImage();
      return imageWritable;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ImageFx.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(graphics, "graphics");

      dc.nl();
      if (canvas == null) {
         dc.append("Canvas is null");
      } else {
         dc.append("Canvas");
         dc.appendVarWithSpace("w", canvas.getWidth());
         dc.appendVarWithSpace("h", canvas.getHeight());
      }

      dc.nl();
      if (image == null) {
         dc.append("javafx.scene.image.Image is null");
      } else {
         dc.append("javafx.scene.image.Image");
         dc.appendVarWithSpace("w", image.getWidth());
         dc.appendVarWithSpace("h", image.getHeight());
      }

      dc.nl();
      if (imageWritable == null) {
         dc.append("javafx.scene.image.WritableImage is null");
      } else {
         dc.append("javafx.scene.image.WritableImage");
         dc.appendVarWithSpace("w", imageWritable.getWidth());
         dc.appendVarWithSpace("h", imageWritable.getHeight());
      }
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("w", w);
      dc.appendVarWithSpace("h", h);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ImageFx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
