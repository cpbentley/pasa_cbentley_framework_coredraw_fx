package pasa.cbentley.framework.core.draw.fx.engine;

import pasa.cbentley.framework.core.draw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.HostServiceDrawJ2se;

public class HostServiceDrawFx extends HostServiceDrawJ2se {

   public HostServiceDrawFx(CoreDrawFxCtx cdc) {
      super(cdc);
   }

   public boolean isHostServiceActive(int serviceID) {
      switch (serviceID) {
         default:
            return super.isHostServiceActive(serviceID);
      }
   }

   public boolean isHostServiceSupported(int serviceID) {
      switch (serviceID) {
         default:
            return isHostServiceSupported(serviceID);
      }
   }

   public boolean setHostServiceActive(int serviceID, boolean isActive) {
      switch (serviceID) {
         default:
            return super.setHostServiceActive(serviceID, isActive);
      }
   }
}
