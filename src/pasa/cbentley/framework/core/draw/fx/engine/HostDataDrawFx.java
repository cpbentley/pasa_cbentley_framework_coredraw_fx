package pasa.cbentley.framework.core.draw.fx.engine;

import pasa.cbentley.core.src4.interfaces.IHostData;
import pasa.cbentley.framework.core.draw.fx.ctx.CoreDrawFxCtx;
import pasa.cbentley.framework.core.draw.j2se.engine.HostDataDrawJ2se;
import pasa.cbentley.framework.coredraw.src4.interfaces.ITechHostDataDraw;

public class HostDataDrawFx extends HostDataDrawJ2se implements IHostData, ITechHostDataDraw {

   public HostDataDrawFx(CoreDrawFxCtx cdc) {
      super(cdc);
   }

   public float getHostDataFloat(int dataID) {
      switch (dataID) {
         default:
            return super.getHostDataFloat(dataID);
      }
   }

   public int getHostDataInt(int dataID) {
      switch (dataID) {
         default:
            return super.getHostDataInt(dataID);
      }
   }

   public Object getHostDataObject(int dataID) {
      switch (dataID) {
         default:
            return super.getHostDataObject(dataID);
      }
   }

   public String getHostDataString(int dataID) {
      switch (dataID) {
         case DATA_ID_STR_05_HOSTNAME:
            return "JavaFX";
         default:
            return super.getHostDataString(dataID);
      }
   }

}
