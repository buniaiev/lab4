
package ter.lab.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Calendar;
import java.util.Date;

public abstract class Component {
   /**
    * Component activity indicator
    */
   private boolean active;

   /**
    * Get the component activity indicator
    */
   private boolean getActive() { return active; }

   /**
    * Scheduled start time
    */
   private Date dateOn;
   /**
    * Set the "Scheduled start time"
    * @param h hours of the set date
    * @param m minutes of the set date
    */
   private void setDateOn(int h, int m) {
      if(!active){
         dateOn = Service.setDate(h, m);
      }
   }

   /**
    * Scheduled shutdown time
    */
   private Date dateOff;
   /**
    * Set the "Scheduled shutdown time"
    * @param h hours of the set date
    * @param m minutes of the set date
    */
   private void setDateOff(int h, int m) {
      if(active){
         dateOff = Service.setDate(h, m);
      }
   }

   /**
    * Component name
    */
   private String name;
   /**
    * Get the name of the component
    */
   public String getName() { return name; }

   /**
    * Label for "Scheduled start time"
    */
   private Label lActiveOn;
   /**
    * Label for "Scheduled shutdown time"
    */
   private Label lActiveOff;
   /**
    * Label for "Activity indicator"
    */
   private Label lActive;
   /**
    * Update Labels Indicators
    */
   private void refreshLabel(){
      lActiveOn.setText("Turn on at:\t" + Service.dateToString(dateOn));
      lActiveOff.setText("Turn off at:\t" + Service.dateToString(dateOff));
      lActive.setText("Active:\t" + getActive());
   }

   /**
    * Class constructor
    *
    * @param name Component name
    */
   Component(String name) {
      lActiveOn = new Label();
      lActiveOff = new Label();
      lActive = new Label();
      this.active = false;
      this.dateOn = null;
      this.dateOff = null;
      this.name = name;
      refreshLabel();
   }

   /**
    * Activate component
    */
   private void on() {
      active = true;
      dateOn = null;
      refreshLabel();
   }

   /**
    * Disable component
    */
   private void off() {
      active = false;
      dateOff = null;
      refreshLabel();
   }

   /**
    * Simulating the component
    * @param curDate Current time
    * @return true - if the activity of the component is changed
    */
   public boolean work(Date curDate) {
      if(active){
         if(dateOff != null && dateOff.getHours() == curDate.getHours() && dateOff.getMinutes() == curDate.getMinutes()){
            off();
            return true;
         }
      } else{
         if(dateOn != null && dateOn.getHours() == curDate.getHours() && dateOn.getMinutes() == curDate.getMinutes()){
            on();
            return true;
         }
      }
      return false;
   }

   /**
    * Drawing the component menu
    * @return VBox of component menu
    */
   public abstract VBox getMenu();

   /**
    * Draw the sub-item "Time" of the component menu
    * @return VBox sub-item "Time"
    */
   VBox getMenuTime(){
      Calendar rightNow = Calendar.getInstance();
      TextField tfHour = Service.getTextFieldWithLimit(24);
      tfHour.setText(String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)));
      TextField tfMinute = Service.getTextFieldWithLimit(60);
      tfMinute.setText(String.valueOf(rightNow.get(Calendar.MINUTE)));

      HBox hbTime = new HBox(10,
            new Label("Время: "),
            tfHour,
            new Label("ч."),
            tfMinute,
            new Label("м.")
      );

      Button bCompOnTime = new Button("Включить компонент в...");
      bCompOnTime.setOnAction(e -> {
         setDateOn(Integer.parseInt(tfHour.getText()),Integer.parseInt(tfMinute.getText()));
         refreshLabel();
      });

      Button bCompOffTime = new Button("Выключить компонент в...");
      bCompOffTime.setOnAction(e -> {
         setDateOff(Integer.parseInt(tfHour.getText()),Integer.parseInt(tfMinute.getText()));
         refreshLabel();
      });

      return new VBox(10,
            lActiveOn,
            lActiveOff,
            hbTime,
            bCompOnTime,
            bCompOffTime
      );
   }

   /**
    * Draw the sub-item "Activity" of the component menu
    * @return VBox sub-item "Activity"
    */
   VBox getMenuActive(){
      Button bCompOn = new Button("Включить компонент");
      bCompOn.setOnAction(e -> on());
      Button bCompOff = new Button("Выключить компонент");
      bCompOff.setOnAction(e -> off());

      return new VBox(10,
            lActive,
            bCompOn,
            bCompOff
      );
   }
}
