## 🏪 무인매장 관리 시스템 - Smart Convenience

> 무인 편의점 점주를 위한 똑똑한 매장 운영 솔루션

**개발 기간 :** 2025.02.28  ~ 2025.04.22
---
### **팀원 구성**
| 정혜민(조장님) | 구혜연 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/0584281d-d1ee-46f3-9425-60463fdbd613" width="250px" height="280px"/> | <img src="https://github.com/user-attachments/assets/ca573a71-0642-4ba6-83fd-19b5858194f4" width="250px" height="280px"/> |

---
### 📌 프로젝트 소개

최근 무인 매장이 빠르게 늘어나고 있으며, N잡으로 무인 매장을 창업하시는 뉴비 점주님들이 많이 생기고 있습니다.

초보 점주님들을 위해 매장을 **안정적으로 관리할 수 있길 바라며** **Daily24 시스템을 구현했습니다.** 

이 프로젝트는 매장 운영의 핵심 요소인 **매출 / 재고 / 발주 / 폐기를** 중심으로, 데이터 기반의 매장 관리 시스템 개발을 목표로 합니다.

---
## **시작 가이드**
- ### 💻 개발 환경

| 항목            | 버전/도구              |
|-----------------|------------------------|
| Git             | `2.49.0`               |
| Node.js         | `v22.14.0`             |
| npm             | `v10.9.2`              |
| Java            | `17`                   |
| Maven           | IntelliJ 내장 Maven 사용 |
| MySQL           | MySQL Workbench 사용    |
| Python          | `3.11.9`               |

> ✅ **권장 개발 툴**: IntelliJ / VS Code  
> ⚠️ `mysql`, `mvn` 등의 CLI 명령어는 로컬에서 직접 사용되지 않으며, IntelliJ 및 Workbench를 통해 실행합니다.

### 레포지토리 클론

```bash
git clone https://github.com/your-org/your-repo.git
cd your-repo
```

### 프론트엔드 실행
1. 폴더로 들어가기
```bash
cd frontend
```

2. 필요한 패키지 설치
```bash
npm install
```

3. 개발 서버 실행하기
```bash
npm run start
```
---
### 백엔드(FastAPI) 실행
1. 폴더로 들어가기
```bash
cd backend
```

2. 가상환경 생성 및 활성화
```bash
# 가상환경 생성
python3 -m venv .venv

# 윈도우 가상환경 활성화
source .venv/bin/activate

# 맥 가상환경 활성화
source .venv/bin/activate

```

3. 필요한 패키지 설치
```bash
pip install -r requirements.txt
```

4. .env 파일 생성
.env 파일을 프로젝트 루트에 만들고 OpenAI 키를 입력하세요.
```ini
OPENAI_API_KEY=sk-...
```

5. 서버 실행
```bash
uvicorn application:app --reload
```

6. 서버 종료 방법
```bash
CTRL + C   # 서버 실행 중단
deactivate # 가상환경 종료
```

---
## 아키텍처
### 디렉토리 구조

```
프로젝트 루트
├── backend/                        # FastAPI 기반 챗봇 서버
│   ├── data/                       # 세일즈 데이터 저장 폴더
│   ├── prompts/                    # 프롬프트 템플릿
│   ├── vectorstore/                # FAISS 벡터 저장소
│   ├── build_vectorstore.py        # 벡터 DB 생성 스크립트
│   ├── load_vectorstore_chain.py   # LangChain 로드 로직
│   ├── rag_chain.py                # RAG 체인 구성 파일
│   ├── main.py                     # FastAPI 서버 진입점
│   ├── sales_tools.py              # 판매 분석 도구 모듈
│   ├── requirements.txt            # Python 패키지 정의
│   └── .env                        # 환경변수 설정
│
├── frontend/                       # React 프론트엔드
│   ├── public/                  
│   ├── src/
│   │   ├── components/            # 공통 컴포넌트
│   │   ├── contexts/              # 전역 상태 관리
│   │   ├── features/              # 주요 기능 폴더 (도메인 단위 구성)
│   │   │   ├── cart_analysis/     # 장바구니 분석
│   │   │   ├── dashboard/         # 관리자 대시보드
│   │   │   ├── disposal/          # 폐기 관리
│   │   │   ├── goods/             # 상품 등록/수정/조회
│   │   │   ├── inventory/         # 재고 관리
│   │   │   ├── member/            # 관리자 기능
│   │   │   ├── ordering/          # 발주 관리
│   │   │   ├── sales_analysis/    # 매출 분석
│   │   │   ├── shop/              # 사용자 쇼핑 페이지
│   │   │   └── statistics/        # 통계 그래프
│   │   ├── pages/                 # 라우팅 페이지
│   │   └── utils/                 # 유틸 함수
│   └── package.json
│
├── .gitignore
└── README.md

├── backend-java/                  # Spring Boot 백엔드 (IntelliJ 기준)
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com.exam/
│   │       │       ├── cartAnalysis/
│   │       │       ├── category/
│   │       │       ├── config/
│   │       │       ├── dashboard/
│   │       │       ├── disposal/
│   │       │       ├── goods/
│   │       │       ├── inventory/
│   │       │       ├── member/
│   │       │       ├── notification/
│   │       │       ├── orderRequest/
│   │       │       ├── payments/
│   │       │       ├── saleData/
│   │       │       ├── salesAlert/
│   │       │       ├── salesAnalysis/
│   │       │       ├── salesHistory/
│   │       │       ├── security/
│   │       │       ├── shop/
│   │       │       └── statistics/
│   │       │       └── Application.java
│   │       └── resources/
│   │           └── application.properties
│   ├── python/
│   │   ├── association_rules.py
│   │   └── association_TimeRules.py
│   └── pom.xml

```

---
## 주요기능

### 매출 레포트
- 일주일 전, 한달전, 일년전 오늘과 비교해 매출 분석 레포트 제공
- 매출에 영향을 주는 상품과 변화에 대한 인사이트를 제공

### 장바구니 분석
- 고객들이 자주 함께 구매하는 상품 조합을 분석해 매장 진열 및 행사 기획에 활용 가능
- 최근 7일 판매 추이를 함께 제공해 현재 트랜드에 맞춘 대응 가능

### 실시간 알림
- 결제 알림, 재고 부족 및 품절, 자동 폐기/폐기 예정 상품에 대한 실시간 알림 제공

---
## 화면 구성 

### 📂 관리자 페이지
| 대시보드 | 판매기록 조회 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/c0d27739-06cc-4ced-bcf8-0a63cf17e499" width="400px"/> | <img src="https://github.com/user-attachments/assets/1e5c4efb-8ac4-405e-99b4-ec8b44cab0bf" width="400px"/> |

| 기간별 매출 조회 | 매출 레포트 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/6da2ae68-816d-44d1-8c00-5658cb64bd17" width="400px" height="250px"/> | <img src="https://github.com/user-attachments/assets/e84e9cba-d49a-4676-b264-1d8e161c1a82" width="400px" height="350px"/> |

| 매출 비교 | 장바구니 분석 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/7a030ce0-b256-4057-abb7-41fff1061b7c" width="400px"/> | <img src="https://github.com/user-attachments/assets/088adcf9-6184-4607-b099-fd27c7a389130a9f" width="400px"/> |

| 재고현황 | 발주 관리 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/7ef29246-1fe9-4bd4-b13b-677144a2addf" width="400px"/> | <img src="https://github.com/user-attachments/assets/66d9a7b6-46d9-4a0d-8350-ed2ee829fd88" width="400px"/> |

| 발주 리스트 | 폐기내역 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/f61277f0-2f92-40cd-91a1-3d5d8d4a4c67" width="400px"/> | <img src="https://github.com/user-attachments/assets/60dcc810-fa5b-43c7-904c-05004a3d8200" width="400px"/> |

| 폐기통계 | 상품조회 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/bc4bfd59-eb57-41e3-a53d-be8445691f57" width="400px"/> | <img src="https://github.com/user-attachments/assets/610418f2-4351-4ea9-9dca-a717f38ec535" width="400px"/> |

| 상품 상세페이지 | 상품수정 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/41769219-b5de-4502-90ff-71e6c60f0670" width="400px"/> | <img src="https://github.com/user-attachments/assets/c9e796ba-418a-4172-9224-02710200eb04" width="400px"/> |


| 상품등록 |  실시간 알림 | 관리자용 챗봇 |
|-------------|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/37efc1f1-4ee5-4ba0-a3e4-e7a21cd780d3" width="400px"/> | <img src="https://github.com/user-attachments/assets/ac5619e9-a1d9-48fa-8ced-1e8f2725b143" width="400px"/> | <img src="https://github.com/user-attachments/assets/23528664-c98b-4fa3-aa8a-020eaae776a0" width="400px"/> |


### 📱 사용자 페이지

| 사용자 페이지 | 할인상품 페이지 | 상품 상세페이지 |
|---------------|----------------|------------------|
| <img src="https://github.com/user-attachments/assets/aa5ead6c-a128-4a3e-bc5e-caa0cd5dedbe" width="250px" height="450px"/> | <img src="https://github.com/user-attachments/assets/57fd7cd6-2a99-4f4c-9e9f-6891fb6e9a6e" width="250px" height="450px"/> | <img src="https://github.com/user-attachments/assets/2a809579-5f99-434e-8f42-dc7658fc5e77" width="250px" height="450px"/> |

| 장바구니 | 결제완료 |
|------------------|------------|
| <img src="https://github.com/user-attachments/assets/a5d6d53d-5b8f-4509-b617-8494110d7cfd" width="250px" height="450px"/> | <img src="https://github.com/user-attachments/assets/4088c1db-17c0-488d-99e5-7022377592f8" width="250px" height="450px"/> |

---
## Stacks 

### Backend
<div align=left> 
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring boot security-6DB33F?style=for-the-badge&logo=springboot-6DB33F&logoColor=white">
<img src="https://img.shields.io/badge/JPA-007396?style=for-the-badge&logo=java&logoColor=white" />
    <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
        <img src="https://img.shields.io/badge/aws-FF9900?style=for-the-badge&logo=aws&logoColor=white"> 
</div>

### Frontend
<div align=left> 
    <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> 
    <img src="https://img.shields.io/badge/tailwind-06B6D4?style=for-the-badge&logo=tailwind&logoColor=black">
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  </div>

### Tools
<div align=left> 
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/VSCode-007ACC?style=for-the-badge&logo=visualstudiocode&logoColor=white" />
<img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white" />
    <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
    <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white">
  </div>


---
## 백엔드 기술 스택 및 의존성
본 프로젝트의 백엔드는 Spring Boot 기반으로 개발되었으며, 아래와 같은 의존성을 포함하고 있습니다.

### 의존성 설정 
- spring-boot-starter-web  
- spring-boot-starter-validation  
- spring-boot-starter-data-jpa  
- mysql-connector-java  
- tomcat-embed-jasper  
- jakarta.servlet.jsp.jstl-api  
- jakarta.servlet.jsp.jstl  
- jquery  
- bootstrap  
- spring-boot-devtools  
- lombok  
- spring-boot-starter-security  
- spring-security-taglibs  
- spring-boot-starter-test  



