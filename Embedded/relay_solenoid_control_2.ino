//릴레이 모듈(12V 제어) - > 솔레노이드 락 제어2

int relay1 = 13;
int relay2 = 12;
int relay3 = 11;
int relay4 = 10;
int relay5 = 9;
int relay6 = 8;
int relay7 = 7;
int relay8 = 6;

void setup() 
{
    pinMode(relay1, OUTPUT);
    pinMode(relay2, OUTPUT);
    pinMode(relay3, OUTPUT);
    pinMode(relay4, OUTPUT);
    pinMode(relay5, OUTPUT);
    pinMode(relay6, OUTPUT);
    pinMode(relay7, OUTPUT);
    pinMode(relay8, OUTPUT);
}

void loop() 
{
    digitalWrite(relay1, LOW);        // 릴레이1을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay1, HIGH);       // 릴레이1을 켜라
    delay(100);                             // 1초간 딜레이

    digitalWrite(relay2, LOW);        // 릴레이2을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay2, HIGH);       // 릴레이2을 켜라
    delay(100);                             // 1초간 딜레이

    digitalWrite(relay3, LOW);        // 릴레이3을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay3, HIGH);       // 릴레이3을 켜라
    delay(100);                             // 1초간 딜레이 

    digitalWrite(relay4, LOW);        // 릴레이4을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay4, HIGH);       // 릴레이4을 켜라
    delay(100);                             // 1초간 딜레이

    digitalWrite(relay5, LOW);        // 릴레이5을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay5, HIGH);       // 릴레이5을 켜라
    delay(100);                             // 1초간 딜레이

    digitalWrite(relay6, LOW);        // 릴레이6을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay6, HIGH);       // 릴레이6을 켜라
    delay(100);                             // 1초간 딜레이

    digitalWrite(relay7, LOW);        // 릴레이7을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay7, HIGH);       // 릴레이7을 켜라
    delay(100);                             // 1초간 딜레이

    digitalWrite(relay8, LOW);        // 릴레이8을 꺼라
    delay(100);                             // 1초간 딜레이
    digitalWrite(relay8, HIGH);       // 릴레이8을 켜라
    delay(100);                             // 1초간 딜레이
}

// void loop()
// {
//     digitalWrite(relay1, HIGH);       // 릴레이1을 켜라
//     digitalWrite(relay2, HIGH);       // 릴레이2을 켜라
//     digitalWrite(relay3, HIGH);       // 릴레이3을 켜라
//     digitalWrite(relay4, HIGH);       // 릴레이4을 켜라
//     digitalWrite(relay5, HIGH);       // 릴레이5을 켜라
//     digitalWrite(relay6, HIGH);       // 릴레이6을 켜라
//     digitalWrite(relay7, HIGH);       // 릴레이7을 켜라
//     digitalWrite(relay8, HIGH);       // 릴레이8을 켜라
// }
