# 🕰️ Crewing(쿠잉)

<img width="7066" height="4200" alt="Image" src="https://github.com/user-attachments/assets/1e1fba0c-603d-47bd-9758-b547f29d1813" />
쿠잉은 사용자의 근무 시간을 관리하고 다른 사람과 근무를 교환할 수 있는 기능을 제공하는 안드로이드 클라이언트 애플리케이션입니다.


# 📌 프로젝트 개요

본 저장소는 쿠잉 서비스의 안드로이드 클라이언트 코드를 담고 있습니다. 사용자는 자신의 근무를 리스트에 올리거나 타인과 교환할 수 있습니다.

## 📱 주요 기능

### 1. 로그인, 회원가입
  - ID Duplicate Validation 및 PW Validation
  - Owner, Employer 구분
  - 최초 로그인 후 자동 로그인 로직

### 2. 근무 등록 및 급여
  - 근무지 등록(Owner는 크루룸(Crewroom) 생성, Employer는 생성된 크루룸에 입장)
  - 근무 일정 등록
  - 근무자 일정 확인
  - 근무자끼리 일정 교환 가능
  - 근무자 목록 확인
  - 본인 급여 확인

### 3. 회원 정보 열람
  - 개인 정보 확인 및 수정
  - 로그아웃 및 탈퇴

### 4. FlowChart

<img width="16384" height="4544" alt="Image" src="https://github.com/user-attachments/assets/ab650bd8-9be8-476a-b665-5cb0b601bc8a" />

[원본 보기](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=DDIP!.drawio&dark=auto#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1Wu1UvNVMkmaUPrxCaT8EBkB4w25uxFYN%26export%3Ddownload)

# 📱app 화면 및 시연

### 로그인 및 회원가입

![Image](https://github.com/user-attachments/assets/326a5547-5b6c-405c-ba28-c1bd34ccb7c7)

### 메인 화면

<img width="3926" height="2600" alt="Image" src="https://github.com/user-attachments/assets/d2d5aa04-edc1-4ac6-b29e-2acea4885d3b" />

### 크루룸 생성 및 입장

![Image](https://github.com/user-attachments/assets/73431b6b-e63f-4043-95ac-d67095a42a34)

### 근무 일정 등록 및 확인

![Image](https://github.com/user-attachments/assets/e43864dd-d5b7-483e-a28e-b06e3c5e0d57)

### 근무 교환 신청/수락 후 반영

![Image](https://github.com/user-attachments/assets/b642ae56-3033-4bc2-b0ef-37205632f8ab)

### 회원 유형 별 근무자 리스트

<img width="622" height="557" alt="Image" src="https://github.com/user-attachments/assets/af7a9b5f-5508-40a8-ae34-2f7e31fc3d77" />

### 회원정보 수정 및 확인

<img src="https://github.com/user-attachments/assets/f55d1e66-71e3-4c94-b06d-f80cdc251413" width="300" />

# 🛠 기술 스택 (Tech Stack)

### IDE
|<img src="https://github.com/user-attachments/assets/2f7ee7c7-fc40-4128-85f7-b3c10716f86c" width="150" />|<img src="https://github.com/user-attachments/assets/9260aa1a-b61e-4c45-a997-648541b00900" width="150" />|
|---|---|
|intelliJ|Android Studio|

### Language
|<img src="https://github.com/user-attachments/assets/6c1de071-e096-4e9e-b0d7-526a833e4f3b" width="150" />|
|---|
|Java|

### DB
|<img src="https://github.com/user-attachments/assets/4dbdaf70-57fd-4bbd-b0b7-64536828e675" width="150" />|
|---|
|MySql|

### Framework
|<img src="https://github.com/user-attachments/assets/accd979c-be07-4be8-b7c6-07dd2d9891b2" width="150" />|
|---|
|Spring Boot|

### Build tool
|<img src="https://github.com/user-attachments/assets/7f607a51-53bc-4dec-9708-dc7ab3912a6f" width="150" />|
|--|
|Gradle|

# 🚀 시작하기 (Getting Started)

1. 이 저장소를 로컬 PC로 클론합니다.

   **git clone [https://github.com/HaYoung1018/DDIP_Client_rev.git](https://github.com/HaYoung1018/DDIP_Client_rev.git)**

2. Android Studio를 실행하고 Open an Existing Project를 통해 클론한 폴더를 엽니다.

3. Gradle Sync가 완료될 때까지 기다립니다.

4. 안드로이드 기기(또는 에뮬레이터)를 연결하고 Run(Shift + F10) 버튼을 눌러 앱을 실행합니다.

    **target API = 31**



# 👩‍💻 Developer

|이름|Github ID|역할|
|---|---|---|
|유우선|@first-woosun|안드로이드 로직 개발, 데이터 흐름 제어, 프로젝트 총괄|
|안해은|@An-Haeeun-02|서버 구축, DB 관리, 프로젝트 진행 일정 관리 및 조율|
|이고은|@Leebbangthug|UI/UX 제작, 프로젝트 재무 관리|
|이재현|@LeeJJJae|안드로이드 로직 개발, 데이터 흐름 제어, 보고서 작성|
|전하영|@HaYoung1018|UI/UX 제작, 서버 구축, DB 관리, 회의록 작성|


