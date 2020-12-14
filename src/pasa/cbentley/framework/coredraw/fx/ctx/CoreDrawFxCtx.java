package pasa.cbentley.framework.coredraw.fx.ctx;

import javafx.application.Platform;
import javafx.scene.text.Font;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IConfigBO;
import pasa.cbentley.core.fx.ctx.FxCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.framework.coredraw.fx.engine.FontCustomizerFx;
import pasa.cbentley.framework.coredraw.fx.engine.FontFactoryFx;
import pasa.cbentley.framework.coredraw.fx.engine.ImageFactoryFx;
import pasa.cbentley.framework.coredraw.fx.engine.ScalerFx;
import pasa.cbentley.framework.coredraw.j2se.ctx.CoreDrawJ2seCtx;
import pasa.cbentley.framework.coredraw.j2se.ctx.IConfigCoreDrawJ2se;
import pasa.cbentley.framework.coredraw.j2se.engine.FontCustomizerJ2SE;
import pasa.cbentley.framework.coredraw.src4.ctx.IConfigCoreDraw;
import pasa.cbentley.framework.coredraw.src4.interfaces.IFontFactory;
import pasa.cbentley.framework.coredraw.src4.interfaces.IImageFactory;
import pasa.cbentley.framework.coredraw.src4.interfaces.IScaler;

public class CoreDrawFxCtx extends CoreDrawJ2seCtx {

   private FontFactoryFx           factoryFont;

   public static final int         CTX_ID = 487;

   private ImageFactoryFx          factoryImage;

   private ScalerFx                scaler;

   protected final FxCtx           fc;

   private final IConfigCoreDrawFx configDrawFx;

   private FontCustomizerFx        fontCustomizerFx;

   /**
    * Uses default {@link ConfigCoreDrawFxDef} configuration.
    * @param boc
    */
   public CoreDrawFxCtx(FxCtx fc, BOCtx boc) {
      this(null, fc, boc);
   }

   public CoreDrawFxCtx(IConfigCoreDrawFx configDraw, FxCtx fc, BOCtx boc) {
      super((configDraw == null) ? new ConfigCoreDrawFxDef(boc.getUCtx()) : configDraw, boc);
      configDrawFx = (IConfigCoreDrawFx) getConfig();
      this.fc = fc;
      factoryFont = new FontFactoryFx(this);
      factoryImage = new ImageFactoryFx(this);
      scaler = new ScalerFx(this);

      if (this.getClass() == CoreDrawFxCtx.class) {
         a_Init();
      }
   }

   public IConfigCoreDrawFx getConfigCoreDrawFx() {
      return configDrawFx;
   }

   public FontCustomizerJ2SE getFontCustomizerJ2SE() {
      return getFontCustomizerFxLazy();
   }

   public FontCustomizerFx getFontCustomizerFxLazy() {
      if (fontCustomizerFx == null) {
         fontCustomizerFx = new FontCustomizerFx(this, factoryFont);
      }
      return fontCustomizerFx;
   }

   public FxCtx getFC() {
      return fc;
   }

   public void loadFont(String name) {
      Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/AirstreamNF.ttf"), 20);

   }

   protected void matchConfig(IConfigBO config, ByteObject settings) {

   }

   public int getCtxID() {
      return CTX_ID;
   }

   /**
    * Call the {@link Runnable} later in the drawing thread.
    * @param run
    */
   public void callSerially(Runnable run) {
      Platform.runLater(run);
   }

   public IImageFactory getImageFactory() {
      return factoryImage;
   }

   public IFontFactory getFontFactory() {
      return factoryFont;
   }

   public boolean hasFeatureSupport(int supportID) {
      return super.hasFeatureSupport(supportID);
   }

   protected String getDefaultFontNameMonoSub() {
      String[] availableFontFamilyNames = factoryFont.getAvailableFontFamilyNames();

      return availableFontFamilyNames[0];
   }

   protected String getDefaultFontNameSystemSub() {
      return "Courier New";
   }

   protected String getDefaultFontNamePropSub() {
      return "Courier New";
   }

   public IScaler getScaler() {
      return scaler;
   }

}