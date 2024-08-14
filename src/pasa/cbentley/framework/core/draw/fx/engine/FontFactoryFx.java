package pasa.cbentley.framework.core.draw.fx.engine;

import java.io.InputStream;
import java.util.List;

import javafx.scene.text.Font;
import pasa.cbentley.framework.core.draw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.FontFactoryJ2se;
import pasa.cbentley.framework.coredraw.src4.engine.VisualState;
import pasa.cbentley.framework.coredraw.src4.interfaces.IMFont;

public class FontFactoryFx extends FontFactoryJ2se {

   protected final CoreDrawFxCtx scc;

   public FontFactoryFx(CoreDrawFxCtx cdc) {
      super(cdc);
      this.scc = cdc;
   }

   protected IMFont createFont(int face, int style, int size) {
      return new FontFx(scc, face, style, size);
   }

   public IMFont getFont(String fontFaceName, int style, int fontPoint) {
      // TODO Auto-generated method stub
      return null;
   }

   public String[] getFontFamilies() {
      List<String> families = Font.getFamilies();
      return (String[]) families.toArray();
   }

   public float getFontScale(int size) {
      return 1;
   }

   public int getScalePixel(int valu, int fun) {
      // TODO Auto-generated method stub
      return 0;
   }

   public void loadFont(InputStream is, String string) {
      final Font f = Font.loadFont(is, 12);

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

}
