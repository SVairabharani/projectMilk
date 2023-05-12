// UNO code

#include <LiquidCrystal.h>
#include <SimpleDHT.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>
LiquidCrystal LCD(8, 9, 10, 11, 12, 13);
SoftwareSerial myserial(2, A5);    //RX TX
SoftwareSerial myserial2(A4, A3);  //RX TX
// TCS230 or TCS3200 pins wiring to Arduino
int pinDHT11 = 3;  // TEMPARATURE AND HUMIDITY SENSOR
#define S0 5
#define S1 4
#define S2 6
#define S3 7
#define relay1 A2
#define button A1
#define sensorOut A0
SimpleDHT11 dht11(pinDHT11);
String a;
int val1 = 0;
int Z3 = 0, Z1 = 0, Z2 = 0;
unsigned int b = 0, c = 0, VALA = 1, d = 0;
int B, C, D, E, G, J, K, L, M, I1, J1, K1, L1, M1, N1;
int F[20], A = 1, H, I[20];
void PH_SENSOR();

// Stores frequency read by the photodiodes
int redFrequency = 0;
int greenFrequency = 0;
int blueFrequency = 0;

//static data
String city="Karur";
String holder_id="202201";
String holder_name="Mothers Milk karur";

void setup() {
  // Setting the outputs
  pinMode(S0, OUTPUT);
  pinMode(S1, OUTPUT);
  pinMode(S2, OUTPUT);
  pinMode(S3, OUTPUT);
  pinMode(A2, OUTPUT);
  pinMode(A1, INPUT);
  // Setting the sensorOut as an input
  pinMode(sensorOut, INPUT);

  // Setting frequency scaling to 20%
  digitalWrite(S0, HIGH);
  digitalWrite(S1, LOW);
  digitalWrite(A2, LOW);

  // Begins serial communication; 
  myserial2.begin(9600);
  myserial.begin(9600);
  Serial.begin(9600);
 
  LCD.begin(16, 2);
  LCD.setCursor(0, 0);
  LCD.print("----WELCOME----");
  delay(1000);
  LCD.clear();

  
}
void loop() {
  byte temperature = 0;
  byte humidity = 0;
  int err = SimpleDHTErrSuccess;
  if ((err = dht11.read(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
    delay(1000);
    return;
  }
  b = temperature;
  c = humidity;
  Serial.print("A-Temprature=");  // TEMPARATURE SENSOR
    delay(500);
  Serial.print(b);
  delay(500);
  Serial.print(", C-Humidity=");  // HUMIDITY SENSOR
    delay(500);
  Serial.print(c);
  delay(500);  
  Serial.println("");

  if(myserial2.available()>0){
    String esp_msg=myserial2.readStringUntil('/n');
    Serial.println(esp_msg + " from esp msg ");
    LCD.clear();
    LCD.setCursor(0, 0);
    LCD.print(esp_msg);
    delay(1000);
    LCD.clear();
  }
  
  a = myserial.readString();
  d = digitalRead(A1);
  if (d == 1) {
    digitalWrite(A2, HIGH);
    delay(3000);
  }
  if (d == 0) {
    digitalWrite(A2, LOW);
    delay(3000);
  }
  LCD.setCursor(0, 0);
  LCD.print("SHOW YOUR CARD  ");

  // if (a == "540051D670A3") {
  //   Z1 = Z1 + 1;
  //   delay(1000);
  //   Serial.print('Z');  // COLOSTRUM
  //   delay(50);
  //   Serial.print(Z1);
  //   delay(50);

  //   LCD.clear();
  //   LCD.setCursor(0, 0);
  //   LCD.print(a);
  //   delay(3000);

  //   val1 = 1;
  // }
  // if (a == "540051B42D9C") {
  //   Z2 = Z2 + 1;
  //   delay(1000);
  //   Serial.print('T');  // TRANSITIONAL MILK
  //   delay(50);
  //   Serial.print(Z1);
  //   delay(50);

  //   LCD.clear();
  //   LCD.setCursor(0, 0);
  //   LCD.print(a);
  //   delay(3000);

  //   val1 = 1;
  // }
  // if (a == "540051BCCE77") {
  //   Z3 = Z3 + 1;
  //   delay(1000);
  //   Serial.print('D');  // milk
  //   delay(1000);
  //   Serial.print(Z3);
  //   delay(1000);

  //   LCD.clear();
  //   LCD.setCursor(0, 0);
  //   LCD.print(a);
  //   delay(3000);
  //   val1 = 1;
  // }
if (a.compareTo("") != 0) {

  Serial.println(" card: " + a);

    /*Z3=Z3+1;
delay(1000);
Serial.print('D');         // milk
  delay(1000);
  Serial.print(Z3);
  delay(1000);*/

    //digitalWrite(relay1,HIGH);
    // delay(3000);
    LCD.clear();
    PH_SENSOR();
    LCD.setCursor(0, 1);
    LCD.print("PH=");
    LCD.print(M1);
    delay(3000);
    // digitalWrite(relay1,LOW);
    delay(1000);
    // Setting RED (R) filtered photodiodes to be read
    digitalWrite(S2, LOW);
    digitalWrite(S3, LOW);

    // Reading the output frequency
    redFrequency = pulseIn(sensorOut, LOW);

    delay(100);
    LCD.setCursor(0, 0);
    LCD.print("R = ");
    LCD.print(redFrequency);
    delay(3000);

    if ((M1 >= 6 && M1 < 8) && (redFrequency > 30 && redFrequency < 50)) {
      LCD.clear();

      LCD.setCursor(1, 0);
      LCD.print("Colostrum Milk");
      delay(3000);
      /*Z1=Z1+1;
delay(3000);
Serial.print('D');         // COLOSTRUM
  delay(50);
  Serial.print(Z1);
  delay(50);*/
    }
    if ((M1 >= 6 && M1 < 8) && (redFrequency > 70 && redFrequency < 90)) {
      LCD.clear();
      LCD.setCursor(0, 0);
      LCD.print("TRANSITIONAL MILK");
      delay(3000);
      /*Z2=Z2+1;
delay(3000);
Serial.print('T');         // TRANSITIONAL MILK
  delay(50);
  Serial.print(Z1);
  delay(50);*/
    }
    if ((M1 >= 6 && M1 < 8) && (redFrequency > 50 && redFrequency < 70)) {
      LCD.clear();
      LCD.setCursor(1, 0);
      LCD.print("Mature milk");
      delay(1000);
      /*Z3=Z3+1;
  delay(3000);
  Serial.print('M');         // LDR MOISTURE
  delay(50);
  Serial.print(Z3);
  delay(50);*/
    }
    // Setting GREEN (G) filtered photodiodes to be read
    digitalWrite(S2, HIGH);
    digitalWrite(S3, HIGH);

    // Reading the output frequency
    greenFrequency = pulseIn(sensorOut, LOW);

    // Printing the GREEN (G) value

    delay(100);
    LCD.setCursor(8, 0);
    LCD.print("G = ");
    LCD.print(greenFrequency);
    delay(1000);

    // Setting BLUE (B) filtered photodiodes to be read
    digitalWrite(S2, LOW);
    digitalWrite(S3, HIGH);

    // Reading the output frequency
    blueFrequency = pulseIn(sensorOut, LOW);
    delay(100);
    LCD.setCursor(8, 1);
    LCD.print("b = ");
    LCD.print(blueFrequency);
    delay(3000);
    LCD.clear();
    E = 0;
    D = 0;
    G = 0;
    
    //bind datas in json format
    StaticJsonDocument<200> data;
    StaticJsonDocument<200> color; 

    color["r"]=redFrequency;
    color["b"]=blueFrequency;
    color["g"]=greenFrequency;
    String phStr=String(M1);

    data["_id"] = a;
    data["ph"] = phStr;
    data["color"] = color;
    data["city"] = city;
    data["holder_id"] = holder_id;
    data["holder_name"] = holder_name;
    data["temp"] = b;
    data["hum"] = c;
    String res;
    serializeJson(data, res);
    

    //Sending datas to ESP8266
    myserial2.print(res);
    Serial.print(res);

    data.clear();
    color.clear();
  }
}

void PH_SENSOR() {
  VALA = 1;
  A = 1;
  if (Serial.available() > 0) {
    while ((Serial.available() > 0) && (A == 1)) {
      B = Serial.read();
      C = 1;
      if ((B == 'P') && (C == 1)) {
        C = 0;
        D = 1;
      }
      if ((D == 1) && (E <= 13)) {
        F[E] = char(B);
        E++;
      }
      if ((B == '.') && (D == 1)) {
        A = 0;
        D = 0;
      }
    }
  }
  if ((E == 6) && (VALA == 1)) {
    VALA = 0;
    //LCD.setCursor(0,1);
    delay(600);
    //Serial.print('D');
    for (G = 0; G <= 4; G++) {
      VALA = 0;
      H = F[G];
      //LCD.print(H);
      //Serial.print(H);
      delay(200);
      A = 0;
    }
    I1 = ((F[3] - 48) * 10);
    J1 = (F[4] - 48);
    M1 = I1 + J1;
    VALA = 0;
    E = 0;
    VALA = 0;
  }
  if ((E == 5) && (VALA == 1)) {
    VALA = 0;
    //LCD.setCursor(0,1);
    delay(600);
    //Serial.print('D');
    for (G = 0; G <= 3; G++) {
      VALA = 0;
      H = F[G];
      //LCD.print(H);
      //Serial.print(H);
      delay(200);
      A = 0;
    }
    VALA = 0;
    I1 = (F[3] - 48);
    M1 = I1;
    E = 0;
    VALA = 0;
  }
}
