## 🏪 무인매장 관리 시스템 - Smart Convenience

> 무인 편의점 점주를 위한 똑똑한 매장 운영 솔루션
> 

**개발 기간 :** 2025.03.06  ~ 2025.04.22
---
### **팀원 구성**
| 정혜민(조장님) | 구혜연 |
|-------------|-------------|
| <img src="https://github.com/user-attachments/assets/0584281d-d1ee-46f3-9425-60463fdbd613" width="250px" height="280px"/> | <img src="https://github.com/user-attachments/assets/ca573a71-0642-4ba6-83fd-19b5858194f4" width="250px" height="280px"/> |

---
### 📌 프로젝트 소개

최근 무인 매장이 빠르게 늘어나고 있으며, N잡으로 무인 매장을 창업하시는 뉴비 점주님들이 많이 생기고 있습니다.

초보 점주님들을 위해 매장을 **안정적으로 관리할 수 있길 바라며** **Daily24 시스템을 구현했습니다.** 

이 프로젝트는매장 운영의 핵심 요소인 **매출 / 재고 / 발주 / 폐기를** 중심으로, 데이터 기반의 매장 관리 시스템 개발을 목표로 합니다.

---
## **시작 가이드**

### 공통
- Git
- Node.js `v18.x` 이상
- npm `v9.x` 이상
- Java `17` (Spring Boot)
- Maven `3.8` 이상
- Python `3.10` 이상 (FastAPI용)
- MySQL `8.0` 이상
- AWS CLI (배포 시)

> 권장 개발 환경: IntelliJ / VS Code


### 레포지토리 클론

```bash
git clone https://github.com/your-org/your-repo.git
cd your-repo
```


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
| <img src="https://github.com/user-attachments/assets/37efc1f1-4ee5-4ba0-a3e4-e7a21cd780d3" width="400px"/> | <img src="https://github.com/user-attachments/assets/ac5619e9-a1d9-48fa-8ced-1e8f2725b143" width="400px"/> | <img src="https://github.com/user-attachments/assets/37efc1f1-4ee5-4ba0-a3e4-e7a21cd780d3" width="400px"/> |


### 📱 사용자 페이지

| 사용자 페이지 | 할인상품 페이지 | 상품 상세페이지 |
|---------------|----------------|------------------|
| <img src="https://github.com/user-attachments/assets/57fd7cd6-2a99-4f4c-9e9f-6891fb6e9a6e" width="250px" height="450px"/> | <img src="https://github.com/user-attachments/assets/aa5ead6c-a128-4a3e-bc5e-caa0cd5dedbe" width="250px" height="450px"/> | <img src="https://github.com/user-attachments/assets/2a809579-5f99-434e-8f42-dc7658fc5e77" width="250px" height="450px"/> |

| 장바구니 | 결제완료 |
|------------------|------------|
| <img src="https://github.com/user-attachments/assets/2a809579-5f99-434e-8f42-dc7658fc5e77" width="250px" height="450px"/> | <img src="https://github.com/user-attachments/assets/4088c1db-17c0-488d-99e5-7022377592f8" width="250px" height="450px"/> |

---


# backend
프로젝트의 백엔드 코드가 저장됩니다.
---
# 의존성 설정
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


