#include <DHT.h>
#include <DS3231.h>
#include <GyverTimer.h>
#include <ArduinoJson.h>
// DATA STRUCTURES ------------------------------------------------------------
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
  byte waterLevel;      // уровень воды
  byte errorNum;        // код ошибки
};

// данные о растении, необх для работы поливалки
struct PlantInfo {
  byte plantID;                    // номер растения
  int  wateringFrequency;          // частота полива
  int  wateringGroundHumThreshold; // условия полива по влажности почвы
  int  wateringAitHumThreshold;    // условия полива по влажности воздуха
};

/*
Сделать:
  - таймер, по которому будет управляться полив
  - предусмотреть сохранение данных о растениях
  - предусм. возможность подключения нескольких растений (и компл. датчиков)
  - логгирование на флешку, пока нет подключение к управляющему устройству
*/
// VARIABLES & CONST -------------------------------------------------------------------------
#define DHTTYPE DHT21   // DHT 21 (AM2301)

#define ECHOPIN 9
#define TRIGPIN 8 
#define DHTPIN 2
#define WATERING_PIN 7

#define BRIGHT_PIN A1
#define HUM_PIN A0

#define STAT_LED_WATER 13
#define STAT_LED_NOCONNECT 12

DHT tempHudtmSensor(DHTPIN, DHTTYPE);
DS3231  rtc(SDA, SCL);

float MIN_WATER_LEVEL = 5.0;
byte errors = 0; // код ошибки (0 - ok, 1 - no water, 2 -no sensors avaiable, 3 - no connection)

//данные о растениях, пр, тд
DeviceStateData currentDevState; // данные о состоянии поливалки
PlantInfo       currentPlantInfo; // данные о подопечном растении

//---------------------------------------------------------------------------------------
//INITIALIZATION
void setup() {
  Serial.begin(9600);

  rtc.begin();
  tempHudtmSensor.begin();
  
  pinMode(ECHOPIN, INPUT);
  pinMode(TRIGPIN, OUTPUT);
  pinMode(WATERING_PIN, OUTPUT);
  digitalWrite(WATERING_PIN, HIGH); //инициализируем high, чтобы выключить

  pinMode(STAT_LED_WATER, OUTPUT);
  pinMode(STAT_LED_NOCONNECT, OUTPUT);
  
  checkState();
}

//MAIN LOOP
void loop() {
  sendDebugInfo();
  delay(1000);

  checkState();
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

  Serial.print("Brightnes: ");
  Serial.print(checkBrightness());
  Serial.print(" ground hudtm: ");
  Serial.println(checkGroundHum());

   // Send date
  Serial.print(rtc.getDateStr());
  Serial.print(" ");
  // Send time
  Serial.println(rtc.getTimeStr());

  Serial.println("*--------------------------------------------------------------------");
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
boolean watering(boolean isWatering, byte wateringPort){
  /*
   * если бак не пустой, то поливаем 
   */
   if(isWatering && checkWaterLevel() > MIN_WATER_LEVEL){
     digitalWrite(wateringPort, LOW);
     return true;
   } else {
     digitalWrite(wateringPort, HIGH);
     return false;
   }
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

  if (cm <= MIN_WATER_LEVEL){
    watering(false, WATERING_PIN);
  }
  
  return cm;
}

// проверка яркости 
int checkBrightness(){
  int brightness = 1024 - analogRead(BRIGHT_PIN);
  return brightness;
}

// проверка влажности почвы 
int checkGroundHum(){
  return analogRead(HUM_PIN);
}

void checkState(){
  if (Serial){
     digitalWrite(STAT_LED_NOCONNECT, LOW);
  } else {
    digitalWrite(STAT_LED_NOCONNECT, HIGH);
    currentDevState.errorNum = 3;
  }
  currentDevState.waterLevel = checkWaterLevel();
  if (currentDevState.waterLevel <= MIN_WATER_LEVEL){
    digitalWrite(STAT_LED_WATER, HIGH);
  } else {
    digitalWrite(STAT_LED_WATER, LOW);
  }
}
