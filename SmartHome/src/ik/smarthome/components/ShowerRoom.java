package ter.lab.factory;

import ter.lab.components.Component;
import ter.lab.components.ComponentShowerRoom;

public class ShowerRoom extends Factory  {
   private String name = "Душевая кабина";

   @Override
   public Component createComponent() {
      return new ComponentShowerRoom(name);
   }

   @Override
   public Component createComponent(String _name) {
      name = _name; 
      return new ComponentShowerRoom(_name); 
   }

   @Override
   public String getCompName() {
      return name;
   }
}
