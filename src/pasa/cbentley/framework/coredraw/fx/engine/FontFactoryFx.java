package pasa.cbentley.framework.coredraw.fx.engine;

import java.io.InputStream;
import java.util.List;

import javafx.scene.text.Font;
import pasa.cbentley.framework.coredraw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.coredraw.j2se.engine.FontFactoryJ2SE;
import pasa.cbentley.framework.coredraw.src4.ctx.ToStringStaticCoreDraw;
import pasa.cbentley.framework.coredraw.src4.engine.VisualState;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;

public class FontFactoryFx extends FontFactoryJ2SE {

   protected final CoreDrawFxCtx scc;

   public FontFactoryFx(CoreDrawFxCtx scc) {
      super(scc);
      this.scc = scc;
      //this is parametrize by launch values

      fontPoints = new int[SIZE_X_NUM];
      fontPoints[SIZE_0_DEFAULT] = 12;
      fontPoints[SIZE_1_TINY] = 8;
      fontPoints[SIZE_2_SMALL] = 10;
      fontPoints[SIZE_3_MEDIUM] = 12;
      fontPoints[SIZE_4_LARGE] = 16;
      fontPoints[SIZE_5_HUGE] = 22;
   }

   public String[] getFontFamilies() {
      List<String> families = Font.getFamilies();
      return (String[]) families.toArray();
   }



   public void setFontRatio(int ratio, int etalon) {
      IMFont f = getDefaultFont();
      int h = f.getHeight();
      int div = etalon / h;
      int fontPoints = 0;
      VisualState vs = null;
      if (div > ratio) {
         //font is too small
         vs = fontSizeIncrease();
      } else {
         vs = fontSizeDecrease();
      }
      fontPoints = vs.fontPoints[0];
   }


   public float getFontScale(int size) {
      return 1;
   }

   protected IMFont createFont(int face, int style, int size) {
      return new FontFx(scc, face, style, size);
   }

   public int getScalePixel(int valu, int fun) {
      // TODO Auto-generated method stub
      return 0;
   }


   public int getFontPointExtraShift() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void loadFont(InputStream is, String string) {
      final Font f = Font.loadFont(is, 12);

   }

   public IMFont getFont(String fontFaceName, int style, int fontPoint) {
      // TODO Auto-generated method stub
      return null;
   }

}
