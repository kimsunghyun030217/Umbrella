// BLE 연결 상태 확인

#include <bluefruit.h>

const int BLUE_PIN = 4;  // A2 / P0.04

void setup() {
  pinMode(BLUE_PIN, OUTPUT);
  digitalWrite(BLUE_PIN, LOW);  // 기본은 꺼짐

  Bluefruit.begin();
  Bluefruit.setName("umbrella_1");
  Bluefruit.Advertising.addFlags(BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE);
  Bluefruit.Advertising.addTxPower();
  Bluefruit.Advertising.start(0);  // 무제한 광고
}

void loop() {
  if (Bluefruit.connected()) {
    digitalWrite(BLUE_PIN, HIGH);  // BLE 연결 시 LED 켬
  } else {
    digitalWrite(BLUE_PIN, LOW);   // BLE 미연결 시 LED 끔
  }

  delay(100);  // 체크 주기
}
