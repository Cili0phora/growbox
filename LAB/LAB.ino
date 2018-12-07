#include <DHT.h>
#include <DS3231.h>
#include <GyverTimer.h>
#include <ArduinoJson.h>
// DATA STRUCTURES ------------------------------------------------------------
// состояние растения
//struct PlantStateData {
//  byte   plantID;         // номер растения
//  String currentDate;     // текущая дата
//  String currentTime;     // текущее время
//  float  temperature;     // температура воздуха
//  int    groundHum;       // влажность почвы
//  float  airHum;          // влажность воздуха
//  float  bright;          // освещенность
//};

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

float MIN_WATER_LEVEL = 5.0; //в сантиметрах высота погруженной помпы - уровень, ниже которого опускаться нельзя
float TANK_HEIGHT = 20.0;    //в сантиметрах высота от дна банки до датчика расстояния
byte errors = 0; // код ошибки (0 - ok, 1 - no water, 2 -no sensors avaiable, 3 - no connection)

boolean isWateringInProcess = false;
boolean hasErrorsWhileWatering = false;

//данные о растениях, пр, тд
DeviceStateData currentDevState; // данные о состоянии поливалки
PlantInfo       currentPlantInfo; // данные о подопечном растении

//ARDUINO JSON
const int plantCapacity = JSON_OBJECT_SIZE(10);

//TODO ПЕРЕДЕЛАТЬ ЭТО ВСЕ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//ПОЛУЧЕНИЕ ДАННЫХ ОТ ПРИЛОЖЕНИЯ (BY GYVER)
#define PARSE_AMOUNT 5       // число значений в массиве, который хотим получить
#define INPUT_AMOUNT 80      // максимальное количество символов в пакете, который идёт в сериал
char inputData[INPUT_AMOUNT];  // массив входных значений (СИМВОЛЫ)
int intData[PARSE_AMOUNT];     // массив численных значений после парсинга
boolean recievedFlag;
boolean getStarted;
byte index;
String string_convert;
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

  currentPlantInfo.plantID = 0;
  currentPlantInfo.wateringFrequency = 0;
  currentPlantInfo.wateringGroundHumThreshold = 0;
  currentPlantInfo.wateringAitHumThreshold = 0;

  checkState();
}

//MAIN LOOP
void loop() {
//  sendDebugInfo();
  makeAndSendPlantData();
  checkState();

  parsing();       // функция парсинга
  if (recievedFlag) {                           // если получены данные
    recievedFlag = false;
    // ЗАПИХИВАЕМ ДАННЫЕ В ПАЧКУ О РАСТЕНИИ
  }
  
  delay(1000);
}

// временная функция ----------------------------------------------------------------
void sendDebugInfo() {
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
void makeAndSendPlantData() {
  StaticJsonBuffer<plantCapacity> jb;
  JsonObject& obj = jb.createObject();
  obj["plantID"] = currentPlantInfo.plantID;
  obj["currentDate"] = rtc.getDateStr();
  obj["currentTime"] = rtc.getTimeStr();
  obj["temperature"] = checkTemperature();
  obj["groundHum"] = checkGroundHum();
  obj["airHum"] = checkAirHum();
  obj["waterLevel"] = currentDevState.waterLevel;
//  obj["bright"] = checkBrightness();
  String res = "";
  obj.printTo(res);
  Serial.println(res);

  /*
  byte   plantID;         // номер растения
  String currentDate;     // текущая дата
  String currentTime;     // текущее время
  float  temperature;     // температура воздуха
  byte   groundHum;       // влажность почвы
  byte   airHum;          // влажность воздуха
  byte   bright;          // освещенность*/
}

//получение данных о новом растении
void getPlantData() {

}

//полив
boolean watering(boolean isWatering) {
  /*
     если бак не пустой, то поливаем
  */
  if (isWatering && checkWaterLevel() > MIN_WATER_LEVEL) {
    digitalWrite(WATERING_PIN, LOW);
    isWateringInProcess = true;
    return true;
  } else {
    digitalWrite(WATERING_PIN, HIGH);
    isWateringInProcess = false;
    return false;
  }
}

//-------------------------------------------------------------------------------
// проверка остатка воды
float checkWaterLevel() {
  float duration, currentVolume;
  digitalWrite(TRIGPIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIGPIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIGPIN, LOW);
  duration = pulseIn(ECHOPIN, HIGH);
  //инвертим для получения остаточного уровня воды
  currentVolume = TANK_HEIGHT - (duration / 58);

  if (currentVolume <= MIN_WATER_LEVEL) {
    watering(false);
  }

  return currentVolume;
}

// проверка яркости
int checkBrightness() {
  int brightness = 1024 - analogRead(BRIGHT_PIN);
  return brightness;
}

// проверка влажности почвы
int checkGroundHum() {
  return analogRead(HUM_PIN);
}

// проверка влажности воздуха
float checkAirHum() {
  return tempHudtmSensor.readHumidity();
}

// проверка температуры
float checkTemperature() {
  return tempHudtmSensor.readTemperature();
}

// проверка состояния устройства, вылавливание ошибок в работе
void checkState() {
  if (Serial) {
    digitalWrite(STAT_LED_NOCONNECT, LOW);
  } else {
    digitalWrite(STAT_LED_NOCONNECT, HIGH);
    currentDevState.errorNum = 3;
  }
  currentDevState.waterLevel = checkWaterLevel();
  if (currentDevState.waterLevel <= MIN_WATER_LEVEL) {
    hasErrorsWhileWatering = isWateringInProcess;
    isWateringInProcess = false;
    digitalWrite(STAT_LED_WATER, HIGH);
  } else {
    digitalWrite(STAT_LED_WATER, LOW);
  }
}

//восстановление после сбоев
void checkErrorsAndRepair() {
  //восстанавливаем работу после долива воды
  if (currentDevState.waterLevel > MIN_WATER_LEVEL && hasErrorsWhileWatering){
    watering(true);
    hasErrorsWhileWatering = false;
  }
}

// ПАРСИНГ ПРИХОДЯЩИХ ЗНАЧЕНИЙ
void parsing() {
  while (Serial.available() > 0) {
    char incomingByte = Serial.read();      // обязательно ЧИТАЕМ входящий символ
    if (incomingByte == '$') {              // если это $
      getStarted = true;                    // поднимаем флаг, что можно парсить
    } else if (incomingByte != ';' && getStarted) { // пока это не ;
      // в общем происходит всякая магия, парсинг осуществляется функцией strtok_r
      inputData[index] = incomingByte;
      index++;
      inputData[index] = '\0';
    } else {
      if (getStarted) {
        char *p = inputData;
        char *str;
        index = 0;
        String value = "";
        while ((str = strtok_r(p, " ", & p)) != NULL) {
          string_convert = str;
          intData[index] = string_convert.toInt();
          index++;
        }
        index = 0;
      }
    }
    if (incomingByte == ';') {        // если таки приняли ; - конец парсинга
      getStarted = false;
      recievedFlag = true;
    }
  }
}
