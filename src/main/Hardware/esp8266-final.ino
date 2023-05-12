#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>

// Replace with your network credentials
const char* ssid = "projectMilk";
const char* password = "1234abcd";
String ip = "";
SoftwareSerial mySerial(5, 4); // RX pin = D1, TX pin = D2
void setup() {
  mySerial.begin(9600);
  Serial.begin(9600);
  delay(100);

  // Connect to Wi-Fi network with SSID and password
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  mySerial.println("WiFi Connected");
  Serial.println("Enter your IP");
  while(true){
    if(Serial.available()>0){
      ip=Serial.readStringUntil('\n');
      break;
    }
    delay(500);
  }
  Serial.println("IP configured :)");
}

void loop() {
  // Make an HTTP POST request to example.com
  if (mySerial.available()>0) {
    String message = mySerial.readStringUntil('\n');
    //deserialization
     StaticJsonDocument<400> doc;
     StaticJsonDocument<400> redoc;
    DeserializationError error = deserializeJson(doc, message);
    if(error){
      Serial.println("your data contains error !");
      return;
    }

    Serial.println(message);
      WiFiClient client;
      HTTPClient http;

      String serverName = "http://"+ip+":8080/projectmilk/update";
      http.begin(client, serverName);

      http.addHeader("Content-Type", "application/json");

      int httpResponseCode = http.POST(message);

      if (httpResponseCode > 0) {
        Serial.print("HTTP Response code: ");
        Serial.println(httpResponseCode);

        String response = http.getString();
        Serial.println(response);
      } else {
        Serial.print("Error code: ");
        Serial.println(httpResponseCode);
      }
      http.end();
  }
}
