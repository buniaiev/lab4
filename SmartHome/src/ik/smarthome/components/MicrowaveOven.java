package ter.lab.factory;

import ter.lab.components.Component;
import ter.lab.components.ComponentMicrowaveOven;

public class MicrowaveOven extends Factory {
   private String name = "������������� ����";

   @Override
   public Component createComponent() {
      return new ComponentMicrowaveOven(name);
   }

   @Override
   public Component createComponent(String _name) {
      name = _name;
      return new ComponentMicrowaveOven(_name);
   }

   @Override
   public String getCompName() {
      return name;
   }
}

