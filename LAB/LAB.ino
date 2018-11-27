/*
Сделать:
  - таймер, по которому будет управляться полив
  - предусмотреть сохранение данных о растениях
  - предусм. возможность подключения нескольких растений (и компл. датчиков)
  - логгирование на флешку, пока нет подключение к управляющему устройству
*/

#include <DHT.h>
#include <DS3231.h>

#define DHTTYPE DHT21   // DHT 21 (AM2301)

#define ECHOPIN 9
#define TRIGPIN 8 
#define DHTPIN 2

#define WATERING_PIN 3
#define


DHT tempHudtmSensor(DHTPIN, DHTTYPE);
DS3231  rtc(SDA, SCL);

byte errors = 0; // код ошибки (0 - ok, 1 - no water, 2 -no sensors avaiable, 3 - no connection)

// состояние растения
struct PlantStateData {
  byte   plantID;         // номер растения
  String currentDate;     // текущая дата
  String currentTime;     // текущее время
  float  temperature;     // температура воздуха
  byte   groundHum;       // влажность почвы
  byte   airHum;          // влажность воздуха
  byte   bright;          // освещенность
};

// состояние поливалки
struct DeviceStateData {
  byte   waterLevel;      // уровень воды
};

// данные о растении, необх для работы поливалки
struct PlantInfo {
  byte plantID;                    // номер растения
  int  wateringFrequency;          // частота полива
  int  wateringGroundHumThreshold; // условия полива по влажности почвы
  int  wateringAitHumThreshold;    // условия полива по влажности воздуха
  /*
    номер растения
    периодичность полива
    при каких условиях поливать принудительно
    byte - 0..255
  */
};
//---------------------------------------------------------------------------------------
//INITIALIZATION
void setup() {
  Serial.begin(9600);

  rtc.begin();
  tempHudtmSensor.begin();
  
  pinMode(ECHOPIN, INPUT);
  pinMode(TRIGPIN, OUTPUT);
}

//MAIN LOOP
void loop() {
  sendDebugInfo();
  delay(1000);
}

// временная функция ----------------------------------------------------------------
void sendDebugInfo(){
  float h = tempHudtmSensor.readHumidity();
  float t = tempHudtmSensor.readTemperature();
  Serial.print("Temp ");
  Serial.print(t);
  Serial.print(" hadtm ");
  Serial.print(h);
  Serial.print(" dist ");
  Serial.println(checkWaterLevel());

   // Send date
  Serial.print(rtc.getDateStr());
  Serial.print(" ");
  // Send time
  Serial.println(rtc.getTimeStr());
}
//----------------------------------------------------------------------------------------
//отправка данных о растении
void sendPlantData(PlantStateData data){
  Serial.println();
}

//отправка данных о состоянии устройства
void sendStatusData(){
  Serial.println();
}

//получение данных о новом растении
void getPlantData(){
  
}


//полив
void watering(byte wateringPort){
  
}

//-------------------------------------------------------------------------------
// проверка остатка воды
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

// проверка яркости 
float checkBrightness(){
  
}
