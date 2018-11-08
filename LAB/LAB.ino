#include <Wire.h>
#include "DHT.h"

#define ECHOPIN 9
#define TRIGPIN 8 
#define DHTPIN 2

#define DHTTYPE DHT21   // DHT 21 (AM2301)
// Connect pin 1 (on the left) of the sensor to +5V
// Connect pin 2 of the sensor to whatever your DHTPIN is
// Connect pin 4 (on the right) of the sensor to GROUND
// Connect a 10K resistor from pin 2 (data) to pin 1 (power) of the sensor
DHT tempHudtmSensor(DHTPIN, DHTTYPE);

struct PlantStateData {
  String date;
  String currentTime;
  float temperature;
  int groundHum;
  int airHum;
};

void setup() {
  Serial.begin(9600);
  
  tempHudtmSensor.begin();
  
  pinMode(ECHOPIN, INPUT);
  pinMode(TRIGPIN, OUTPUT);
}

//данные о растении
void sendPlantData(PlantStateData data){
  Serial.println();
}

//состояние устройства
void sendStatusData(){
  Serial.println();
}

float checkWaterLevel(){
  float duration, cm; 
  digitalWrite(TRIGPIN, LOW); 
  delayMicroseconds(2); 
  digitalWrite(TRIGPIN, HIGH); 
  delayMicroseconds(10); 
  digitalWrite(TRIGPIN, LOW); 
  duration = pulseIn(ECHOPIN, HIGH); 
  cm = duration / 58;
  return cm;
  delay(100);
}

void sendDebugInfo(){
  float h = tempHudtmSensor.readHumidity();
  float t = tempHudtmSensor.readTemperature();
  Serial.print("Temp ");
  Serial.print(t);
  Serial.print(" hadtm ");
  Serial.print(h);
  Serial.print(" dist");
  Serial.println(checkWaterLevel());
}

void loop() {
  // put your main code here, to run repeatedly:
  sendDebugInfo();
  delay(1000);
}
