#include <DHT.h>
#include <DS3231.h>
#include <ArduinoJson.h>
// DATA STRUCTURES ------------------------------------------------------------
// состояние поливалки
struct DeviceStateData {
  byte waterLevel;      // уровень воды
  byte errorNum;        // код ошибки
};

// данные о растении, необх для работы поливалки
struct PlantInfo {
  int   wateringFrequency;          // частота полива
  int   wateringGroundHumThreshold; // условия полива по влажности почвы
  float wateringAitHumThreshold;    // условия полива по влажности воздуха
  byte  wtHour;    //час полива
  byte  wtMinute;  //минта полива
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
Time    currentTime;

int   WATERING_TIMING = 1500;   //продолжительность полива в миллисекундах
float MIN_WATER_LEVEL = 5.0;    //в сантиметрах высота погруженной помпы - уровень, ниже которого опускаться нельзя
float TANK_HEIGHT = 20.0;       //в сантиметрах высота от дна банки до датчика расстояния
float MIN_AIR_HUM = 40.0;
int   MIN_GROUNG_HUM = 300;
byte  DEFAULT_WATERING_TIME = 12;

byte errors = 0; // код ошибки (0 - ok, 1 - no water, 2 -no sensors avaiable, 3 - no connection)

boolean isWateringInProcess = false;
int     waterintStartTime = 0;
boolean hasErrorsWhileWatering = false;

//данные о растениях, пр, тд
DeviceStateData currentDevState; // данные о состоянии поливалки
PlantInfo       currentPlantInfo; // данные о подопечном растении

//ARDUINO JSON
const int plantCapacity = JSON_OBJECT_SIZE(10);

//PARSER
#define PARSE_AMOUNT 3         // число значений в массиве, который хотим получить
int intData[PARSE_AMOUNT];     // массив численных значений после парсинга
boolean recievedFlag;
boolean getStarted;
byte index;
String string_convert = "";
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

  currentPlantInfo.wateringFrequency = 0;
  currentPlantInfo.wateringGroundHumThreshold = MIN_GROUNG_HUM;
  currentPlantInfo.wateringAitHumThreshold = MIN_AIR_HUM;
  currentPlantInfo.wtHour = DEFAULT_WATERING_TIME;
  currentPlantInfo.wtMinute = 0;

  checkState();
}

//MAIN LOOP
void loop() {
  makeAndSendPlantData();
  checkState();
  if (isWateringNeed()){
    watering(true);
  }
  if (isStopWatering()){
    watering(false);
  }
  forseWatering();
  readDataFromHost();
  delay(2000);
}
//----------------------------------------------------------------------------------------
//отправка данных о растении
void makeAndSendPlantData() {
  StaticJsonBuffer<plantCapacity> jb;
  JsonObject& obj = jb.createObject();
  obj["time"] = rtc.getTimeStr();
  obj["date"] = rtc.getDateStr();
  obj["temperature"] = checkTemperature();
  obj["groundHum"] = checkGroundHum();
  obj["airHum"] = checkAirHum();
  obj["waterLevel"] = currentDevState.waterLevel;
  obj["bright"] = checkBrightness();
  String res = "";
  obj.printTo(res);
  Serial.print(res);

  /*
  byte   plantID;         // номер растения
  String currentDate;     // текущая дата
  String currentTime;     // текущее время
  float  temperature;     // температура воздуха
  byte   groundHum;       // влажность почвы
  byte   airHum;          // влажность воздуха
  byte   bright;          // освещенность*/
}

boolean isWateringNeed() {
  currentTime = rtc.getTime();
  return currentTime.hour == currentPlantInfo.wtHour &&
      currentTime.min == currentPlantInfo.wtMinute;
}

boolean isStopWatering() {
  return (waterintStartTime != 0 && millis() > waterintStartTime + WATERING_TIMING);
}

//полив
boolean watering(boolean isWatering) {
  /*
     если бак не пустой, то поливаем
  */
  if (isWatering && checkWaterLevel() > MIN_WATER_LEVEL) {
    if (!isWateringInProcess) {
      digitalWrite(WATERING_PIN, LOW);
      isWateringInProcess = true;
      //ТУТ СТАВИМ ТАЙМЕР, КОТОРЫЙ ВЫЗОВЕТ ОТСТАНОВКУ ПОЛИВА
      waterintStartTime = millis();
      if (waterintStartTime == 0){
        waterintStartTime += 1;
      }
    }
    return true;
  } else {
    digitalWrite(WATERING_PIN, HIGH);
    isWateringInProcess = false;
    waterintStartTime = 0;
    return false;
  }
}

//ПРИНУДИТЕЛЬНЫЙ ПОЛИВ
void forseWatering(){
  if (!isWateringInProcess && !hasErrorsWhileWatering &&
        checkGroundHum() < currentPlantInfo.wateringGroundHumThreshold) 
    {
      watering(true);
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

//Чтение настроек с компа
void readDataFromHost(){
  parsing();               // функция парсинга
  if (recievedFlag) {      // если получены данные
    recievedFlag = false;
    currentPlantInfo.wateringFrequency = intData[2];
    currentPlantInfo.wtHour = intData[0];
    currentPlantInfo.wtMinute = intData[1];
  }
}

// ПАРСИНГ ПРИХОДЯЩИХ ЗНАЧЕНИЙ
void parsing() {
  if (Serial.available() > 0) {
    char incomingByte = Serial.read();        // обязательно ЧИТАЕМ входящий символ
    if (getStarted) {                         // если приняли начальный символ (парсинг разрешён)
      if (incomingByte != ' ' && incomingByte != ';') {   // если это не пробел И не конец
        string_convert += incomingByte;                   // складываем в строку
      } else {                                            // если это пробел или ; конец пакета
        intData[index] = string_convert.toInt();
        string_convert = "";
        index++;
      }
    }
    if (incomingByte == '$') {                // если $ - парисм, данные пришли
      getStarted = true;
      index = 0;
      string_convert = "";
    }
    if (incomingByte == ';') {                // если таки приняли ; - конец парсинга
      getStarted = false;
      recievedFlag = true;
    }
  }
}
