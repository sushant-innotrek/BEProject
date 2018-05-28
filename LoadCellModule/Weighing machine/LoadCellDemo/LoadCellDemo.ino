#include "HX711.h"

HX711 cell(2, 0);

void setup() {
  Serial.begin(115200);
}

long val = 0;
float temp = 0;
float count = 0;

void loop() {
  count = count + 1;
  
  // Use only one of these
  //val = ((count-1)/count) * val    +  (1/count) * cell.read(); // take long term average
  val = 0.5 * val    +   0.5 * cell.read(); // take recent average
  //val = cell.read(); // most recent reading
  //val = val - 8382000; //base zero
  //weight with plank = 8452
  //weight with 0.5 kg = 8510
   temp  = val * 0.01;
   temp = (temp - 84521) / 855850.0 * 800;
   
   //temp = temp / 8510 ;  //0.5 kg calibration
  Serial.println( temp  );
}
