package ter.lab.factory;

import ter.lab.components.Component;
import ter.lab.components.ComponentHeatingFloor;

public class HeatingFloor extends Factory {
   private String name = "Пол с подогревом";

   @Override
   public Component createComponent() {
      return new ComponentHeatingFloor(name);
   }

   @Override
   public Component createComponent(String _name) {
      name = _name;
      return new ComponentHeatingFloor(_name);
   }

   @Override
   public String getCompName() {
      return name;
   }
}
