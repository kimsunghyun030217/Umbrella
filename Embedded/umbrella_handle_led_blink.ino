// 우산 손잡이 LED 깜빡이는 기본 테스트

// void setup() {
//   // put your setup code here, to run once:

// }

// void loop() {
//   // put your main code here, to run repeatedly:

// }


// 내장 LED 핀 정의 (Adafruit Feather nRF52 보드의 경우 기본적으로 13번 핀)
const int ledPin = 13; 

void setup() {
  // LED 핀을 출력 모드로 설정
  pinMode(ledPin, OUTPUT);
}

void loop() {
  // LED 켜기
  digitalWrite(ledPin, HIGH);
  delay(1000);  // 1초 대기
  
  // LED 끄기
  digitalWrite(ledPin, LOW);
  delay(1000);  // 1초 대기
}
