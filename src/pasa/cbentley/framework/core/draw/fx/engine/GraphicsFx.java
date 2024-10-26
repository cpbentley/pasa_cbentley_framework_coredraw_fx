package pasa.cbentley.framework.core.draw.fx.engine;

import java.awt.Stroke;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.ColorUtils;
import pasa.cbentley.framework.core.draw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.GraphicsJ2se;
import pasa.cbentley.framework.coredraw.src4.interfaces.IGraphics;
import pasa.cbentley.framework.coredraw.src4.interfaces.IImage;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;

/**
 * J2SE bridge class for the {@link mordan.bridge.javafx.ui.GraphicsFx} class of MIDP 2.0 <br>
 * @author DWTFYW
 *
 */
public class GraphicsFx extends GraphicsJ2se implements IGraphics {

   public static boolean hasCollision(int rx, int ry, int rw, int rh, int x, int y, int w, int h) {
      //by default collision. invalidates with root coordinate
      if (rx + rw <= x)
         return false;
      if (rx >= x + w)
         return false;
      if (ry + rh <= y)
         return false;
      if (ry >= y + h)
         return false;
      return true;
   }

   CoreDrawFxCtx           cdc;

   private int             color;

   /** 
    * MIDP font associated with this graphics context
    */
   public FontFx           fontFx = null;

   private int             fwFlags;

   /** 
    * JavaFx Graphics context.  
    * <br>
    * This is passed in the constructor, but will be created from the getGraphics call on an AWT Image
    */
   private GraphicsContext graphics;

   private Stroke          gStroke;

   private int             stroke;

   private int             translate_x;

   private int             translate_y;

   public GraphicsFx(CoreDrawFxCtx cdc, GraphicsContext g) {
      super(cdc);
      if (g == null) {
         throw new NullPointerException();
      }
      graphics = g;
      fontFx = (FontFx) cdc.getFontFactory().getDefaultFont();
      this.cdc = cdc;
   }

   public GraphicsFx(CoreDrawFxCtx dd, GraphicsContext g, ByteObject tech) {
      super(dd);

   }

   public void clipRect(int x, int y, int width, int height) {
      Node clip = graphics.getCanvas().getClip();
      //#debug
      if (clip != null) {
         int rx = (int) clip.getBoundsInLocal().getMinX();
         int ry = (int) clip.getBoundsInLocal().getMinY();
         int rw = (int) clip.getBoundsInLocal().getWidth();
         int rh = (int) clip.getBoundsInLocal().getHeight();

         //#debug
         String msg = "GraphicsFx#clipRect " + rx + "," + ry + " " + rw + "-" + rh;
         //#debug
         toDLog().pBridge(msg, this, GraphicsFx.class, "clipRect", LVL_05_FINE, true);

         //compute intersection
         int[] r = new int[4];
         clipRectIntersection(rx, ry, rw, rh, x, y, width, height, r);
         if (r != null) {
            setClip(r[0], r[1], r[2], r[3]);
         }
      } else {
         setClip(x, y, width, height);
      }
   }

   /**
    * Intersection of two rectangles. All coordinates are expressed in the coordinate system of the first rectangle. <br>
    * <br>
    * <li>int[0] = x coordinate in canvas system
    * <li>int[1] = y coorindate in canvas system
    * <li>int[2] = width of rectangle
    * <li>int[3] = height of rectangle
    * <br>
    * If there is no intersection, return null. <br>
    * 
    * @param rx x coordinate of the first rectangle
    * @param ry root y coordinate
    * @param rw root x coordinate
    * @param rh root x coordinate
    * @param x coordinate of the intersanct rectangle
    * @param y coordinate of the intersanct rectangle in the canvas coordinate system 
    * @param w width of the intersanct rectangle
    * @param h
    * @param intersect container for output values
    * @return null if no intersection intersect array if there is such
   
    */
   public int[] clipRectIntersection(int rx, int ry, int rw, int rh, int x, int y, int w, int h, int[] intersect) {
      //SystemLog.printDraw("#DrawUtilz#getIntersection x1=" + rx + " y1=" + ry + "w1=" + rw + " h1=" + rh + " x2=" + x + " y2=" + y + " w2=" + w + " h2=" + h);
      if (hasCollision(rx, ry, rw, rh, x, y, w, h)) {
         if (rx < x) {
            intersect[0] = x;
            if (x + w < rx + rw) {
               intersect[2] = w;
            } else {
               intersect[2] = rx + rw - x;
            }
         } else {
            intersect[0] = rx;
            if (rx + rw < x + w) {
               //both are inside
               intersect[2] = rw;
            } else {
               intersect[2] = w - rx + x;
            }
         }
         if (ry < y) {
            intersect[1] = y;
            if (y + h < ry + rh) {
               intersect[3] = h;
            } else {
               intersect[3] = ry + rh - y;
            }
         } else {
            intersect[1] = ry;
            if (ry + rh < y + h) {
               //both are inside
               intersect[3] = rh;
            } else {
               intersect[3] = h - ry + y;
            }
         }
         return intersect;
      }
      return null;
   }

   public void drawArc(int x, int y, int w, int h, int sa, int aa) {
      graphics.strokeArc(x, y, w, h, sa, aa, ArcType.OPEN);
   }

   public void drawChar(char character, int x, int y, int anchor) {
      drawString("" + character, x, y, anchor);
   }

   public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
      drawString(new String(data, offset, length), x, y, anchor);
   }

   public void drawImage(IImage imgx, int x, int y, int anchor) {
      ImageFx img = (ImageFx) imgx;
      // default anchor
      if (anchor == 0) {
         anchor = TOP | LEFT;
      }

      switch (anchor & (TOP | BOTTOM | BASELINE | VCENTER)) {
         case BASELINE:
         case BOTTOM:
            y -= img.getHeight();
            break;
         case VCENTER:
            y -= img.getHeight() >> 1;
            break;
         case TOP:
         default:
            break;

      }

      switch (anchor & (LEFT | RIGHT | HCENTER)) {
         case RIGHT:
            x -= img.getWidth();
            break;
         case HCENTER:
            x -= img.getWidth() >> 1;
            break;
         case LEFT:
         default:
            break;
      }
      Image imgageToPaint = img.getImageToPaint();
      if (imgageToPaint == null) {
         throw new NullPointerException();
      }
      //draw the image requires to get the image
      graphics.drawImage(imgageToPaint, x, y, img.getWidth(), img.getHeight());
   }

   public void drawLine(int x1, int y1, int x2, int y2) {
      graphics.strokeLine(x1, y1, x2, y2);
   }

   public void drawRect(int x, int y, int width, int height) {
      graphics.strokeRect(x, y, width, height);
   }

   public void drawRegion(IImage srcx, int x_src, int y_src, int width, int height, int transform, int x_dst, int y_dst, int anchor) {
      ImageFx src = (ImageFx) srcx;
      // may throw NullPointerException, this is ok
      if (x_src + width > src.getWidth() || y_src + height > src.getHeight() || width < 0 || height < 0 || x_src < 0 || y_src < 0) {
         throw new IllegalArgumentException("Area out of Image");
      }

      javafx.scene.image.Image img = src.getImageFx();

      Affine t = new Affine();

      int dW = width;
      int dH = height;
      int rotPivotX = x_src;
      int rotPixotY = y_src;
      switch (transform) {
         case TRANSFORM_0_NONE: {
            break;
         }
         case TRANSFORM_1_FLIP_H_MIRROR_ROT180: {
            t.prependScale(-1, 1);
            t.prependTranslation(width, 0);
            t.prependRotation(180, rotPivotX, rotPixotY);
            t.prependTranslation(width, height);
            break;
         }
         case TRANSFORM_2_FLIP_V_MIRROR: {
            t.prependScale(-1, 1);
            t.prependTranslation(width, 0);
            break;
         }
         case TRANSFORM_3_ROT_180: {
            t.prependRotation(180, rotPivotX, rotPixotY);
            t.prependTranslation(width, height);
            break;
         }
         case TRANSFORM_4_MIRROR_ROT270: {
            t.prependRotation(270, rotPivotX, rotPixotY);
            t.prependScale(1, -1);
            dW = height;
            dH = width;
            break;
         }
         case TRANSFORM_5_ROT_90: {
            t.prependRotation(90, rotPivotX, rotPixotY);
            t.prependTranslation(height, 0);
            dW = height;
            dH = width;
            break;
         }
         case TRANSFORM_6_ROT_270: {
            t.prependRotation(270, rotPivotX, rotPixotY);
            t.prependTranslation(0, width);
            dW = height;
            dH = width;
            break;
         }
         case TRANSFORM_7_MIRROR_ROT90: {
            t.prependRotation(90, rotPivotX, rotPixotY);
            t.prependTranslation(height, 0);
            t.appendScale(-1, 1);
            t.prependTranslation(0, width);
            dW = height;
            dH = width;
            break;
         }

         default:
            throw new IllegalArgumentException("Bad transform");
      }

      // process anchor and correct x and y _dest
      // vertical
      boolean badAnchor = false;

      if (anchor == 0) {
         anchor = TOP | LEFT;
      }

      if ((anchor & 0x7f) != anchor || (anchor & BASELINE) != 0)
         badAnchor = true;

      if ((anchor & TOP) != 0) {
         if ((anchor & (VCENTER | BOTTOM)) != 0)
            badAnchor = true;
      } else if ((anchor & BOTTOM) != 0) {
         if ((anchor & VCENTER) != 0)
            badAnchor = true;
         else {
            y_dst -= dH - 1;
         }
      } else if ((anchor & VCENTER) != 0) {
         y_dst -= (dH - 1) >>> 1;
      } else {
         // no vertical anchor
         badAnchor = true;
      }

      // horizontal
      if ((anchor & LEFT) != 0) {
         if ((anchor & (HCENTER | RIGHT)) != 0)
            badAnchor = true;
      } else if ((anchor & RIGHT) != 0) {
         if ((anchor & HCENTER) != 0)
            badAnchor = true;
         else {
            x_dst -= dW - 1;
         }
      } else if ((anchor & HCENTER) != 0) {
         x_dst -= (dW - 1) >>> 1;
      } else {
         // no horizontal anchor
         badAnchor = true;
      }

      if (badAnchor)
         throw new IllegalArgumentException("Bad Anchor");

      graphics.save();
      graphics.translate(x_dst, y_dst);
      graphics.transform(t);
      graphics.drawImage(img, 0, 0, width, height, x_src, y_src, x_src + width, y_src + height);
      // return to saved
      graphics.restore();
   }

   public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
      if (rgbData == null)
         throw new NullPointerException();
      if (width == 0 || height == 0) {
         return;
      }
      int l = rgbData.length;
      if (width < 0 || height < 0 || offset < 0 || offset >= l || (scanlength < 0 && scanlength * (height - 1) < 0) || (scanlength >= 0 && scanlength * (height - 1) + width - 1 >= l))
         throw new ArrayIndexOutOfBoundsException();
      PixelFormat pf = PixelFormat.getIntArgbInstance();
      if (!processAlpha) {
         pf = PixelFormat.getIntArgbPreInstance();
      }
      WritableImage wi = new WritableImage(width, height);
      wi.getPixelWriter().setPixels(0, 0, width, height, pf, rgbData, offset, scanlength);
      graphics.drawImage(wi, x, y, width, height);
   }

   public void drawRoundRect(int x, int y, int w, int h, int r1, int r2) {
      graphics.strokeRoundRect(x, y, w, h, r1, r2);
   }

   /**
    * Text positioning is problematic
    * @param str
    * @param x
    * @param y
    * @param anchor
    */
   public void drawString(String str, int x, int y, int anchor) {
      // default anchor
      if (anchor == 0) {
         anchor = TOP | LEFT;
      }
      y = super.getFontY_Baseline(fontFx, anchor, y);
      x = super.getFontX(fontFx, anchor, x, str);

      graphics.fillText(str, x, y);
   }

   public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
      drawString(str.substring(offset, offset + len), x, y, anchor);
   }

   public boolean featureEnable(int featureID, boolean enable) {
      // TODO Auto-generated method stub
      throw new RuntimeException();
      //return false;
   }

   public void fillArc(int x, int y, int w, int h, int sa, int aa) {
      graphics.fillArc(x, y, w, h, sa, aa, ArcType.OPEN);
   }

   public void fillRect(int x, int y, int width, int height) {
      graphics.fillRect(x, y, width, height);
   }

   public void fillRoundRect(int x, int y, int w, int h, int r1, int r2) {
      graphics.fillRoundRect(x, y, w, h, r1, r2);
   }

   public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
      double[] xPoints = new double[] { x1, x2, x3 };
      double[] yPoints = new double[] { y1, y2, y3 };
      graphics.fillPolygon(xPoints, yPoints, 3);
   }

   public int getBlueComponent() {
      return color & 0xFF;
   }

   public int getClipHeight() {
      Canvas s = graphics.getCanvas();
      Node c = s.getClip();
      if (c == null) {
         return (int) s.getHeight();
      } else {
         Bounds b = c.getBoundsInLocal();
         return (int) b.getHeight();
      }
   }

   /**
    * 
    */
   public int getClipWidth() {
      Canvas s = graphics.getCanvas();
      Node c = s.getClip();
      if (c == null) {
         return (int) s.getWidth();
      } else {
         Bounds b = c.getBoundsInLocal();
         return (int) b.getWidth();
      }
   }

   public int getClipX() {
      Canvas s = graphics.getCanvas();
      Node c = s.getClip();
      if (c == null) {
         return 0;
      } else {
         Bounds b = c.getBoundsInLocal();
         return (int) b.getMinX();
      }
   }

   public int getClipY() {
      Canvas s = graphics.getCanvas();
      Node c = s.getClip();
      if (c == null) {
         return 0;
      } else {
         Bounds b = c.getBoundsInLocal();
         return (int) b.getMinY();
      }
   }

   public int getColor() {
      return color;
      //return graphics.getFill().getRGB();
   }

   /**
    * Gets the color that will be displayed if the specified color is requested. <br>
    * This method enables the developer to check the manner in which RGB values are mapped 
    * to the set of distinct colors that the device can actually display. 
    * <br>
    * <br>
    * For example, with a monochrome device, 
    * this method will return either 0xFFFFFF (white) or 0x000000 (black) depending on the brightness of the specified color.
    * @param color the desired color (in 0x00RRGGBB format, the high-order byte is ignored) 
    * @return the corresponding color that will be displayed on the device's screen (in 0x00RRGGBB format)
    */
   public int getDisplayColor(int color) {
      //in the 
      return color;
   }

   public IMFont getFont() {
      return fontFx;
   }

   public int getGrayScale() {
      //TODO implement grayscaling
      return color;
   }

   public int getGreenComponent() {
      return (color >> 8) & 0xFF;
   }

   public int getRedComponent() {
      return (color >> 16) & 0xFF;
   }

   public int getStrokeStyle() {
      return stroke;
   }

   public int getTranslateX() {
      return translate_x;
   }

   public int getTranslateY() {
      return translate_y;
   }

   public boolean hasFeatureEnabled(int featureID) {
      throw new RuntimeException();
      //return false;
   }

   public boolean hasImplementationFlag(int flag) {
      return BitUtils.hasFlag(fwFlags, flag);
   }

   public void setClip(int x, int y, int width, int height) {
      graphics.beginPath();
      graphics.rect(x, y, width, height);
      //      graphics.moveTo(x, y);
      //      graphics.lineTo(x + width, y);
      //      graphics.lineTo(x + width, y + height);
      //      graphics.lineTo(x, y + height);
      //      graphics.lineTo(x, y);
      graphics.closePath();
      //graphics.clip();
      Rectangle rect = new Rectangle(width, height);
      rect.setX(x);
      rect.setY(y);
      graphics.getCanvas().setClip(rect);
   }

   public void setColor(int RGB) {
      this.setColor((RGB >> 16) & 0xFF, (RGB >> 8) & 0xFF, (RGB >> 0) & 0xFF);
   }

   public void setColor(int red, int green, int blue) {
      color = ColorUtils.getRGBInt(red, green, blue);
      Color c = Color.rgb(red, green, blue, 1.0);
      graphics.setFill(c);
      graphics.setStroke(c);

   }

   public void setFont(IMFont font) {
      this.fontFx = (FontFx) font;

      if (this.fontFx == null) {
         this.fontFx = (FontFx) cdc.getFontFactory().getDefaultFont();
      }

      graphics.setFont(this.fontFx.getFontFx());
   }

   public void setGrayScale(int v) {

   }

   /**
    * Stroke style for line drawing isn't something AWT supports.
    * 
    */
   public void setStrokeStyle(int style) {
      //      Stroke str = graphics.getStroke();
      //      //System.out.println(((BasicStroke) str).getLineWidth());
      //      if (style == Graphics.DOTTED) {
      //         gStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1.0f, new float[] { 2.0f }, 1.0f);
      //      } else {
      //         gStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
      //      }
      //      graphics.setStroke(gStroke);
      stroke = style;
   }

   /**
    * translate needs to remember the current translation, since AWT doesn't provide getTranslate methods.
    */
   public void setTranslate(int x, int y) {
      graphics.translate(-translate_x, -translate_y);
      translate_x += x;
      translate_y += y;
      graphics.translate(translate_x, translate_y);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, GraphicsFx.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(fontFx, "FontFx");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, GraphicsFx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("color", color);
      dc.appendVarWithSpace("translate_x", translate_x);
      dc.appendVarWithSpace("translate_y", translate_y);
   }

   //#enddebug

}
