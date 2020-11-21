package ter.lab.factory;

import ter.lab.components.Component;
import ter.lab.components.ComponentAirConditioning;

public class AirConditioning extends Factory {
   private String name = "Кондиционер";

   @Override
   public Component createComponent() {
      return new ComponentAirConditioning(name);
   }

   @Override
   public Component createComponent(String _name) {
      name = _name;
      return new ComponentAirConditioning(_name);
   }

   @Override
   public String getCompName() {
      return name;
   }
}

