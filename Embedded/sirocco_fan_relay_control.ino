//릴레이를 이용해 시로코팬을 2초 켜고 5초 끄는 동작을 반복하는 테스트 코드

int relayPin = 20;  // 릴레이 Signal 핀은 D8 사용

void setup() {
  pinMode(relayPin, OUTPUT);       // 릴레이 핀을 출력 모드로 설정
  Serial.begin(115200);            // 시리얼 통신 시작 (속도: 115200bps)
  Serial.println("릴레이 제어 시작됨");
}

void loop() {
  // 팬 켜기
  digitalWrite(relayPin, HIGH);     // 릴레이 ON → 팬 ON
  Serial.println("팬 켜짐");
  delay(2000);                     // 2초 대기

  // 팬 끄기
  digitalWrite(relayPin, LOW);    // 릴레이 OFF → 팬 OFF
  Serial.println("팬 꺼짐");
  delay(5000);                     // 5초 대기
}
