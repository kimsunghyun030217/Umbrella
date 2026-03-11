//릴레이 모듈(12V 제어) - > 솔레노이드 락 제어 1

int relay1 = 13;
int relay2 = 12;
int relay3 = 11;
int relay4 = 10;
int relay5 = 9;
int relay6 = 8;
int relay7 = 7;
int relay8 = 6;
int relay9 = 5;
int relay10 = 4;
int relay11 = 3;
int relay12 = 2;
int relay13 = 1;
int relay14 = 0;
int relay15 = 14;
int relay16 = 15;
int relay17 = 16;
int relay18 = 17;
int relay19 = 18;

void setup()  {     
    pinMode(relay1, OUTPUT);     
    pinMode(relay2, OUTPUT);     
    pinMode(relay3, OUTPUT);     
    pinMode(relay4, OUTPUT);     
    pinMode(relay5, OUTPUT);     
    pinMode(relay6, OUTPUT);     
    pinMode(relay7, OUTPUT);     
    pinMode(relay8, OUTPUT);     
    pinMode(relay9, OUTPUT);     
    pinMode(relay10, OUTPUT);     
    pinMode(relay11, OUTPUT);     
    pinMode(relay12, OUTPUT);     
    pinMode(relay13, OUTPUT);     
    pinMode(relay14, OUTPUT);     
    pinMode(relay15, OUTPUT);
    pinMode(relay16, OUTPUT);     
    pinMode(relay17, OUTPUT);     
    pinMode(relay18, OUTPUT);     
    pinMode(relay19, OUTPUT);     
}


// void loop() 
// {
//     digitalWrite(relay1, LOW);        // 릴레이1을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay1, HIGH);       // 릴레이1을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay2, LOW);        // 릴레이2을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay2, HIGH);       // 릴레이2을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay3, LOW);        // 릴레이3을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay3, HIGH);       // 릴레이3을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay4, LOW);        // 릴레이4을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay4, HIGH);       // 릴레이4을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay5, LOW);        // 릴레이5을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay5, HIGH);       // 릴레이5을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay6, LOW);        // 릴레이6을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay6, HIGH);       // 릴레이6을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay7, LOW);        // 릴레이7을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay7, HIGH);       // 릴레이7을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay8, LOW);        // 릴레이8을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay8, HIGH);       // 릴레이8을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay9, LOW);        // 릴레이9을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay9, HIGH);       // 릴레이9을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay10, LOW);       // 릴레이10을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay10, HIGH);      // 릴레이10을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay11, LOW);       // 릴레이11을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay11, HIGH);      // 릴레이11을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay12, LOW);       // 릴레이12을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay12, HIGH);      // 릴레이12을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay13, LOW);       // 릴레이13을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay13, HIGH);      // 릴레이13을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay14, LOW);       // 릴레이14을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay14, HIGH);      // 릴레이14을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay15, LOW);       // 릴레이15을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay15, HIGH);      // 릴레이15을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay16, LOW);       // 릴레이16을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay16, HIGH);      // 릴레이16을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay17, LOW);       // 릴레이17을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay17, HIGH);      // 릴레이17을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay18, LOW);       // 릴레이18을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay18, HIGH);      // 릴레이18을 켜라
//     delay(100);                       // 1초간 딜레이

//     digitalWrite(relay19, LOW);       // 릴레이19을 꺼라
//     delay(100);                       // 1초간 딜레이
//     digitalWrite(relay19, HIGH);      // 릴레이1을 켜라
//     delay(100);                       // 1초간 딜레이
// }


// void loop()
// {
//     digitalWrite(relay1, LOW);       // 릴레이1을 꺼라
//     digitalWrite(relay2, LOW);       // 릴레이2을 꺼라
//     digitalWrite(relay3, LOW);       // 릴레이3을 꺼라
//     digitalWrite(relay4, LOW);       // 릴레이4을 꺼라
//     digitalWrite(relay5, LOW);       // 릴레이5을 꺼라
//     digitalWrite(relay6, LOW);       // 릴레이6을 꺼라
//     digitalWrite(relay7, LOW);       // 릴레이7을 꺼라
//     digitalWrite(relay8, LOW);       // 릴레이8을 꺼라
//     digitalWrite(relay9, LOW);       // 릴레이9을 꺼라
//     digitalWrite(relay10, LOW);      // 릴레이10을 꺼라
//     digitalWrite(relay11, LOW);      // 릴레이11을 꺼라
//     digitalWrite(relay12, LOW);      // 릴레이12을 꺼라
//     digitalWrite(relay13, LOW);      // 릴레이13을 꺼라
//     digitalWrite(relay14, LOW);      // 릴레이14을 꺼라
//     digitalWrite(relay15, LOW);      // 릴레이15을 꺼라
//     digitalWrite(relay16, LOW);      // 릴레이16을 꺼라
//     digitalWrite(relay17, HIGH);      // 릴레이17을 꺼라
//     digitalWrite(relay18, HIGH);      // 릴레이18을 꺼라
//     digitalWrite(relay19, HIGH);      // 릴레이19을 꺼라
// }

void loop()
{
    digitalWrite(relay1, HIGH);       // 릴레이1을 켜라
    digitalWrite(relay2, HIGH);       // 릴레이2을 켜라
    digitalWrite(relay3, HIGH);       // 릴레이3을 켜라
    digitalWrite(relay4, HIGH);       // 릴레이4을 켜라
    digitalWrite(relay5, HIGH);       // 릴레이5을 켜라
    digitalWrite(relay6, HIGH);       // 릴레이6을 켜라
    digitalWrite(relay7, HIGH);       // 릴레이7을 켜라
    digitalWrite(relay8, HIGH);       // 릴레이8을 켜라
    digitalWrite(relay9, HIGH);       // 릴레이9을 켜라
    digitalWrite(relay10, HIGH);      // 릴레이10을 켜라
    digitalWrite(relay11, HIGH);      // 릴레이11을 켜라
    digitalWrite(relay12, HIGH);      // 릴레이12을 켜라
    digitalWrite(relay13, HIGH);      // 릴레이13을 켜라
    digitalWrite(relay14, HIGH);      // 릴레이14을 켜라
    digitalWrite(relay15, HIGH);      // 릴레이15을 켜라
    digitalWrite(relay16, HIGH);      // 릴레이16을 켜라
    digitalWrite(relay17, LOW);      // 릴레이17을 켜라
    digitalWrite(relay18, LOW);      // 릴레이18을 켜라
    digitalWrite(relay19, LOW);      // 릴레이19을 켜라
}
