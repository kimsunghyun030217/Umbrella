//라즈베리파이에서 명령 받아서 제어
//시리얼 명령을 받아 여러 릴레이와 시로코팬을 제어하는 건조 시스템 제어 코드


const int relayPins[8] = {13, 12, 11, 10, 9, 8, 7, 6};

void setup() {
  for (int i = 0; i < 8; i++) {
    pinMode(relayPins[i], OUTPUT);
    digitalWrite(relayPins[i], HIGH); // 초기 상태: 잠금 (HIGH)
  }

  Serial.begin(9600);
  while (!Serial); // USB 연결 안정화 대기
}

void loop() {
  if (Serial.available()) {
    String input = Serial.readStringUntil('\n');
    input.trim();

    if (input == "ALL_OFF") {
      for (int i = 0; i < 8; i++) {
        digitalWrite(relayPins[i], HIGH);
      }
      Serial.println("[GIGA] ALL_OFF: 모든 릴레이 HIGH");
    } 
    else if (input == "START_DRYING") {
      Serial.println("[GIGA] START_DRYING 수신 → 팬 릴레이 ON(10초)");

      digitalWrite(5, HIGH); // 5번 핀 (index 4) HIGH → 팬 ON
      delay(10000);                     // 10초 대기
      digitalWrite(5, LOW);  // 다시 LOW → 팬 OFF

      Serial.println("[GIGA] 팬 릴레이 OFF (10초 후)");
    }
    else {
      int relayIndex = input.toInt();  // 1~8
      if (relayIndex >= 1 && relayIndex <= 8) {
        digitalWrite(relayPins[relayIndex - 1], LOW);
        Serial.print("[GIGA] 릴레이 ");
        Serial.print(relayIndex);
        Serial.println(" → LOW (잠금 해제)");
      } else {
        Serial.print("[GIGA] 잘못된 입력: ");
        Serial.println(input);
      }
    }
  }
}
