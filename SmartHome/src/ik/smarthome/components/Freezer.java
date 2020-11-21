package ter.lab.factory;

import ter.lab.components.Component;
import ter.lab.components.ComponentFreezer;

public class Freezer extends Factory {
   private String name = "Морозильная камера";

   @Override
   public Component createComponent() {
      return new ComponentFreezer(name);
   }

   @Override
   public Component createComponent(String _name) {
      name = _name;
      return new ComponentFreezer(_name);
   }

   @Override
   public String getCompName() {
      return name;
   }
}


