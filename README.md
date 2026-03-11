# ☂️ Smart Umbrella Sharing Platform

IoT 기반 스마트 공유 우산 대여·반납 및 통합 관리 시스템

본 프로젝트는 공유 경제 모델을 기반으로 공공 우산의 효율적인 관리와 분실 방지를 위해 개발된 **IoT 기반 스마트 우산 대여 플랫폼**입니다.  
사용자는 모바일 앱을 통해 우산 거치대 위치와 대여 가능 우산 수를 확인하고 **NFC 태깅을 통해 간편하게 우산을 대여 및 반납**할 수 있습니다.

백엔드 서버는 사용자 정보, 우산 대여 상태, 거치대 재고 데이터를 **실시간으로 관리**하며 임베디드 장치와 연동하여 실제 우산 보관 장치를 제어합니다.  
우산 보관 장치는 **Microcontroller 기반 임베디드 시스템**으로 구성되어 있으며 릴레이 기반 잠금 제어, LED 슬롯 표시, 건조 팬 제어 등의 기능을 수행합니다.

---

# 🚀 Key Features

### 📱 Smart Umbrella Rental
모바일 앱을 통해 우산 거치대 위치, 대여 가능 우산 수, 서비스 구역 정보를 확인하고 NFC 인증을 통해 간편하게 우산 대여 및 반납 가능

### 🔄 Real-time Backend Management
백엔드 서버에서 사용자 인증, 우산 대여 상태, 거치대 재고 정보를 실시간으로 관리

### 🔒 Intelligent Lock Control
솔레노이드 락과 릴레이 모듈을 이용하여 우산 보관 슬롯의 잠금 및 해제 제어

### 💡 LED Slot Indicator
시프트 레지스터 기반 LED 제어를 통해 사용자에게 대여 가능한 우산 슬롯 위치를 직관적으로 안내

### 🌬️ Automatic Drying System
우산 반납 시 시로코 팬을 작동시켜 우산을 건조하고 보관 상태를 유지

### 📡 BLE Communication
우산 손잡이에 탑재된 BLE 장치를 통해 사용자 스마트폰과 연결

---

# 🏗️ System Architecture

모바일 앱, 백엔드 서버, 임베디드 장치로 구성된 **IoT 기반 시스템 구조**



---

# ⚙️ Tech Stack

## Backend
- Spring Boot
- MySQL
- Redis
- Firebase

## Mobile
- Flutter

## Embedded / Hardware
- Arduino
- Raspberry Pi
- PN532 NFC Module
- Solenoid Lock
- 8-Channel Relay
- Shift Register
- LED Slot Indicator
- Shirocco Fan

---

# 🧩 Hardware Components

| Module | Description | Components |
|---|---|---|
| Main Controller | 우산 보관 장치 제어 | Arduino Giga R1 |
| Lock Control | 우산 슬롯 잠금 제어 | Solenoid Lock, Relay |
| Slot Indicator | 우산 위치 표시 | Shift Register, LED |
| Authentication | 사용자 인증 | PN532 NFC Module |
| Communication | 장치 간 데이터 통신 | BLE |
| Drying System | 우산 건조 기능 | Shirocco Fan |

---

# 🔄 System Workflow

1. 사용자가 모바일 앱에서 우산 거치대 위치 확인  
2. NFC 태깅을 통해 사용자 인증 진행  
3. 백엔드 서버에서 대여 가능 여부 확인  
4. 임베디드 장치에서 릴레이를 통해 우산 슬롯 잠금 해제  
5. 우산 반납 시 자동으로 상태 업데이트 및 건조 시스템 작동

---

# 🎯 Expected Impact

- 공공 우산 서비스의 체계적인 관리
- 우산 분실 및 관리 비용 감소
- 사용자 중심의 간편한 대여·반납 경험 제공
- IoT 기반 스마트 공유 서비스 구현

---

# 👨‍💻 My Role

### Backend
- 사용자 인증 및 계정 관리 시스템 구현  
- NFC 인증 기반 우산 대여 가능 여부 판단 로직 구현  
- 우산 대여/반납 핵심 비즈니스 로직 설계 및 구현  
- 우산 연체 사용자 패널티 관리 기능 구현  
- 서버–클라이언트 간 실시간 통신 및 알림 시스템 구축

### Embedded
- Arduino 기반 릴레이 제어를 통한 우산 슬롯 잠금/해제 기능 구현
- Shift Register를 활용한 LED 슬롯 표시 시스템 구현
- 시로코 팬 제어를 통한 우산 건조 기능 구현
