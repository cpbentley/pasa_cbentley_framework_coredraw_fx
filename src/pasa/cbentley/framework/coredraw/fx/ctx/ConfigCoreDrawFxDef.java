package pasa.cbentley.framework.coredraw.fx.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.framework.coredraw.j2se.ctx.ConfigCoreDrawJ2seDef;
import pasa.cbentley.framework.coredraw.src4.ctx.ConfigCoreDrawDef;
import pasa.cbentley.framework.coredraw.src4.ctx.IConfigCoreDraw;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechDrawer;

public class ConfigCoreDrawFxDef extends ConfigCoreDrawJ2seDef implements IConfigCoreDrawFx {

   public ConfigCoreDrawFxDef(UCtx uc) {
      super(uc);
   }
   
   public int[] getFontPoints() {
      // TODO Auto-generated method stub
      return null;
   }

   public int getAliasMode() {
      return ITechDrawer.MODSET_APP_ALIAS_0_BEST;
   }

}
