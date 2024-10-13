package pasa.cbentley.framework.core.draw.fx.engine;

import com.sun.javafx.tk.Toolkit;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.framework.core.draw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.FontJ2se;

/**
 * J2SE bridge class for the {@link javax.microedition.lcdui.FontSwing} class of MIDP 2.0 <br>
 * 
 * TODO how to provide antialiased font drawing in this bridge?
 * 
 * <br>
 * Font class is final in J2ME...
 * 
 * 
 * @author
 *
 */
public class FontFx extends FontJ2se {

   protected final CoreDrawFxCtx         cdc;


   private com.sun.javafx.tk.FontMetrics fm;

   /** 
    */
   private javafx.scene.text.Font        font;

   private int                           heightCache;

   /**
    * 
    */
   public FontFx(CoreDrawFxCtx cdc, int face, int style, int size) {
      super(cdc, face, style, size);
      this.cdc = cdc;

      FontWeight fw = FontWeight.NORMAL;
      FontPosture posture = FontPosture.REGULAR;
      switch (style) {
         case STYLE_1_BOLD:
            fw = FontWeight.BOLD;
            break;
         case STYLE_2_ITALIC:
            posture = FontPosture.ITALIC;
            break;
         // Doesn't seem to be any underlined font support in plain old AWT, never mind.
      }

      this.font = javafx.scene.text.Font.font(fontNameInit, fw, posture, points);
      fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
   }

   public int charsWidth(char[] c, int ofs, int len) {
      return stringWidth(new String(c, ofs, len));
   }

   public int charWidth(char c) {
      return (int) fm.computeStringWidth(String.valueOf(c));
   }

   public int getAscent() {
      return (int) fm.getAscent();
   }

   public int getDescent() {
      return (int) fm.getDescent();
   }

   public javafx.scene.text.Font getFontFx() {
      return font;
   }

   public int getHeight() {
      //potentially costly.. so we are going to cache it locally
      if (heightCache == 0) {
         float height = fm.getLineHeight();
         heightCache = (int) height;
      }
      return heightCache;

      //this code below does not work in practice
      //final Text text = new Text("P");
      //text.applyCss();
      //return (int) text.getBoundsInLocal().getHeight();
      //return (int) fm.getLineHeight();
   }

   public String getName() {
      return font.getName();
   }

   public int getWidthWeigh() {
      return stringWidth("m");
   }

   public boolean isSupported(int flag) {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * Width depends on the style
    */
   public int stringWidth(String s) {
      //this does not work as JavaFX people advertised on SOF
      //final Text text = new Text(s);
      //text.applyCss();
      //return (int) text.getBoundsInLocal().getWidth();

      //float width = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth("", font);
      //return (int) width;
      //works better 28/03/2017 for input demo.. why using Text?
      return (int) fm.computeStringWidth(s);

   }

   public int substringWidth(String s, int offset, int length) {
      return stringWidth(s.substring(offset, offset + length));
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, FontFx.class, 155);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nl();
      dc.root(font, "javafx.scene.text.Font");
      dc.appendVarWithSpace("name", font.getName());
      dc.appendVarWithSpace("Family", font.getFamily());
      dc.nl();
      dc.appendVarWithSpace("Size", font.getSize());
      dc.appendVarWithSpace("Style", font.getStyle());

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, FontFx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
