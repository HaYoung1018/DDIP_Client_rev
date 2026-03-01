# <img src="https://github.com/user-attachments/assets/3d72ddfa-3371-4796-9e61-a54b0beb162d" width="50"> Crewing(쿠잉)

<img width="7066" height="4200" alt="Image" src="https://github.com/user-attachments/assets/1e1fba0c-603d-47bd-9758-b547f29d1813" />
쿠잉은 사용자의 근무 시간을 관리하고 다른 사람과 근무를 교환할 수 있는 기능을 제공하는 안드로이드 클라이언트 애플리케이션입니다.

<div align=center><h2>📚 STACKS</h2></div>

<div align="center">
  
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
<br>
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
<br>
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
<br>
</div>

# 📌 프로젝트 개요

### 프로젝트 진행 기간 : 2024.06~2024.12

### 프로젝트 추진 배경


2024년 1월 통계청의 조사에 따르면, 최근 5년간 우리나라 파트타임 근로자(주 30시간 미만 근로) 수는 51만 9000여 명에서 62만 4000여 명으로 20.2% 증가했습니다.

파트타임 근로자의 증가 원인은 주로 2030세대가 워라벨을 추구하여 파트타임을 선호하고, 고령화 사회로 인해 은퇴 후 경제활동을 이어가는 노인 세대가 장시간 근무의 부담을 느끼기 때문입니다. 게다가 코로나 이후 경제 상황이 어려워진 업장주들이 주휴수당 지급에 대한 부담감 때문에 파트타임 일자리를 더 많이 구하게 되었습니다.

그러나 이러한 증가에도 불구하고 파트타임 근로자 관리는 여전히 체계적이지 않습니다. 대학생들의 아르바이트 경험에서도 다양한 문제가 드러났습니다. 특히 소규모 사업장에서는 주간 근무 일정이 매주 조정되는 경우가 흔한데, 지금까지는 이와 같은 일정 조정이 직접적인 연락을 통해 이루어졌습니다. 예를 들어, 급한 일정이 생길 경우, 다른 동료에게 개인적으로 연락을 돌려 공백을 채워줄 인원을 찾는 번거로움이 있습니다.

위 문제를 해결하기 위해, 업장주와 파트타임 근무자들이 공동으로 접속하여 서로의 근무 일정을 공유하고 능동적으로 조율할 수 있는 시스템을 개발하고자 합니다. 이를 통해 업장주는 근무 현황을 한눈에 확인하고, 임금 계산 및 지급 여부를 효율적으로 관리할 수 있게 하는 것이 목표입니다.


## ✅ 주요 기능

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

---

### 메인 화면

<img width="3926" height="2600" alt="Image" src="https://github.com/user-attachments/assets/d2d5aa04-edc1-4ac6-b29e-2acea4885d3b" />

---

### 크루룸 생성 및 입장

![Image](https://github.com/user-attachments/assets/73431b6b-e63f-4043-95ac-d67095a42a34)

---

### 근무 일정 등록 및 확인

![Image](https://github.com/user-attachments/assets/e43864dd-d5b7-483e-a28e-b06e3c5e0d57)

---

### 근무 교환 신청/수락 후 반영

![Image](https://github.com/user-attachments/assets/b642ae56-3033-4bc2-b0ef-37205632f8ab)

---

### 회원 유형 별 근무자 리스트

<img width="622" height="557" alt="Image" src="https://github.com/user-attachments/assets/af7a9b5f-5508-40a8-ae34-2f7e31fc3d77" />

---

### 회원정보 수정 및 확인

<img src="https://github.com/user-attachments/assets/f55d1e66-71e3-4c94-b06d-f80cdc251413" width="300" />

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



