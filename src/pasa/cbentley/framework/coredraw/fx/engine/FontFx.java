package pasa.cbentley.framework.coredraw.fx.engine;

import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;

import com.sun.javafx.tk.Toolkit;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.framework.coredraw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.coredraw.j2se.engine.FontJ2SE;

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
public class FontFx extends FontJ2SE {

   protected final CoreDrawFxCtx         cdc;

   private int                           face, style, size;

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
         case STYLE_BOLD:
            fw = FontWeight.BOLD;
            break;
         case STYLE_ITALIC:
            posture = FontPosture.ITALIC;
            break;
         // Doesn't seem to be any underlined font support in plain old AWT, never mind.
      }

      this.font = javafx.scene.text.Font.font(fontName, fw, posture, points);
      fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
   }

   public javafx.scene.text.Font getFontFx() {
      return font;
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

   public int getBaselinePosition() {
      return 0;
   }

   public int getDescent() {
      return (int) fm.getDescent();
   }

   public int getFace() {
      return face;
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

   public int getSize() {
      return size;
   }

   public int getStyle() {
      return style;
   }

   public int getWidthWeigh() {
      return stringWidth("m");
   }

   public boolean isBold() {
      return (style & STYLE_BOLD) != 0;
   }

   public boolean isItalic() {
      return (style & STYLE_ITALIC) != 0;
   }

   public boolean isPLAIN() {
      return style == 0;
   }

   public boolean isSupported(int flag) {
      // TODO Auto-generated method stub
      return false;
   }

   public boolean isUnderlined() {
      return (style & STYLE_UNDERLINED) != 0;
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
      dc.root(this, FontFx.class, "@line5");
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

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, FontFx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
