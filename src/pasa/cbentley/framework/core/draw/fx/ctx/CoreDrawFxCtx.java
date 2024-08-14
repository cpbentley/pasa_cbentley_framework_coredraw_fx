package pasa.cbentley.framework.core.draw.fx.ctx;

import javafx.application.Platform;
import javafx.scene.text.Font;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IConfigBO;
import pasa.cbentley.core.fx.ctx.FxCoreCtx;
import pasa.cbentley.framework.core.draw.fx.engine.FontCustomizerFx;
import pasa.cbentley.framework.core.draw.fx.engine.FontFactoryFx;
import pasa.cbentley.framework.core.draw.fx.engine.HostDataDrawFx;
import pasa.cbentley.framework.core.draw.fx.engine.HostFeatureDrawFx;
import pasa.cbentley.framework.core.draw.fx.engine.HostServiceDrawFx;
import pasa.cbentley.framework.core.draw.fx.engine.ImageFactoryFx;
import pasa.cbentley.framework.core.draw.fx.engine.ScalerFx;
import pasa.cbentley.framework.core.draw.j2se.ctx.CoreDrawJ2seCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.FontCustomizerJ2se;
import pasa.cbentley.framework.core.draw.j2se.engine.HostDataDrawJ2se;
import pasa.cbentley.framework.core.draw.j2se.engine.HostFeatureDrawJ2se;
import pasa.cbentley.framework.core.draw.j2se.engine.HostServiceDrawJ2se;
import pasa.cbentley.framework.coredraw.src4.interfaces.IFontCustomizer;
import pasa.cbentley.framework.coredraw.src4.interfaces.IFontFactory;
import pasa.cbentley.framework.coredraw.src4.interfaces.IImageFactory;
import pasa.cbentley.framework.coredraw.src4.interfaces.IScaler;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechHostFeatureDraw;

public class CoreDrawFxCtx extends CoreDrawJ2seCtx {

   public static final int         CTX_ID = 487;

   private final IConfigCoreDrawFx configDrawFx;

   private FontFactoryFx           factoryFont;

   private ImageFactoryFx          factoryImage;

   protected final FxCoreCtx           fc;

   private FontCustomizerFx        fontCustomizerFx;

   private HostDataDrawFx          hostDataDrawFx;

   private HostFeatureDrawFx hostFeatureDrawFx;

   private HostServiceDrawFx hostServiceDrawFx;

   private ScalerFx                scaler;

   /**
    * Uses default {@link ConfigCoreDrawFxDef} configuration.
    * @param boc
    */
   public CoreDrawFxCtx(FxCoreCtx fc, BOCtx boc) {
      this(new ConfigCoreDrawFxDef(boc.getUC()), fc, boc);
   }

   public CoreDrawFxCtx(IConfigCoreDrawFx configDraw, FxCoreCtx fc, BOCtx boc) {
      super(configDraw, fc, boc);
      configDrawFx = (IConfigCoreDrawFx) getConfig();
      this.fc = fc;
      factoryFont = new FontFactoryFx(this);
      factoryImage = new ImageFactoryFx(this);
      scaler = new ScalerFx(this);

      hostDataDrawFx = new HostDataDrawFx(this);
      hostFeatureDrawFx = new HostFeatureDrawFx(this);
      hostServiceDrawFx = new HostServiceDrawFx(this);

      if (this.getClass() == CoreDrawFxCtx.class) {
         a_Init();
      }
   }

   /**
    * Call the {@link Runnable} later in the drawing thread.
    * @param run
    */
   public void callSerially(Runnable run) {
      Platform.runLater(run);
   }

   public IConfigCoreDrawFx getConfigCoreDrawFx() {
      return configDrawFx;
   }

   public int getCtxID() {
      return CTX_ID;
   }

   protected String getDefaultFontNamePropSub() {
      return "Courier New";
   }

   protected String getDefaultFontNameSystemSub() {
      return "Courier New";
   }

   public FxCoreCtx getFC() {
      return fc;
   }

   /**
    * Returns {@link IFontCustomizer} for {@link ITechHostFeatureDraw#SUP_ID_06_CUSTOM_FONTS}
    */
   public Object getFeatureObject(int featureID) {
      if (featureID == ITechHostFeatureDraw.SUP_ID_06_CUSTOM_FONTS) {
         return getFontCustomizerFxLazy();
      }
      return null;
   }

   public FontCustomizerFx getFontCustomizerFxLazy() {
      if (fontCustomizerFx == null) {
         fontCustomizerFx = new FontCustomizerFx(this, factoryFont);
      }
      return fontCustomizerFx;
   }

   public FontCustomizerJ2se getFontCustomizerJ2SE() {
      return getFontCustomizerFxLazy();
   }

   public IFontFactory getFontFactory() {
      return factoryFont;
   }

   public HostDataDrawJ2se getHostDataDrawJ2SE() {
      return hostDataDrawFx;
   }

   public HostFeatureDrawJ2se getHostFeatureDrawJ2se() {
      return hostFeatureDrawFx;
   }

   public HostServiceDrawJ2se getHostServiceDrawJ2se() {
      return hostServiceDrawFx;
   }

   public IImageFactory getImageFactory() {
      return factoryImage;
   }

   public IScaler getScaler() {
      return scaler;
   }

   public boolean hasFeatureSupport(int supportID) {
      return super.hasFeatureSupport(supportID);
   }

   public void loadFont(String name) {
      Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/AirstreamNF.ttf"), 20);

   }

   protected void matchConfig(IConfigBO config, ByteObject settings) {

   }

}
