package pasa.cbentley.framework.core.draw.fx.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.framework.core.draw.j2se.ctx.ConfigCoreDrawJ2seDef;
import pasa.cbentley.framework.coredraw.src4.ctx.ConfigCoreDrawDef;
import pasa.cbentley.framework.coredraw.src4.ctx.IConfigCoreDraw;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechGraphics;

public class ConfigCoreDrawFxDef extends ConfigCoreDrawJ2seDef implements IConfigCoreDrawFx {

   public ConfigCoreDrawFxDef(UCtx uc) {
      super(uc);
   }
   

   public int getAliasMode() {
      return ITechGraphics.MODSET_APP_ALIAS_0_BEST;
   }

}
