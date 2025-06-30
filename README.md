
# 🏢 Shinlogis WMS (Shinlogis Warehouse Management System)
신세계 I&C JAVA 기반 백엔드 개발자 양성 과정 8차수 미니 프로젝트

Shinlogis WMS는 창고 내 상품의 입고, 출고, 재고, 보관 현황 등을 체계적으로 관리할 수 있도록 구축한 **경량형 창고관리시스템**입니다. 공급사로부터의 입고, 지점으로의 출고, 창고별 재고 현황, 보관 중 파손 등 핵심 기능을 중심으로 설계되었습니다.

---
## 🧑‍💻 팀 소개

| 이름 | 역할 | 담당|
|------|------|------ |
| 이세형 | 팀장 | 출고관리 및 출고등록, 본사에서의 주문조회 |
| 김예진 | 팀원 |  메인 페이지 대시보드 통계 및 시각화, 입고 관리 및 입고 처리 엑셀 내보내기, 데이터베이스 설계|
| 김지민 | 팀원 | 상품관리, 상품관리, 입출입고 재고관리를 통한 재고 관리 | 
| 서예닮 | 팀원 | 지점 메인 페이지, 주문신청, 지점에서의 주문조회, 지점관리  |
| 성유진 | 팀원 |  로그인, 회원가입, 회원관리, 마이페이지, 공급사 관리, 채팅 시스템|

---

## 🛠️ 기술 스택

![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)
![macOS](https://img.shields.io/badge/macOS-000000?style=for-the-badge&logo=apple&logoColor=white)

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)
![VS Code](https://img.shields.io/badge/VSCode-007ACC?style=for-the-badge&logo=visualstudiocode&logoColor=white)

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)

![Jakarta Mail](https://img.shields.io/badge/Jakarta%20Mail-EE5B3C?style=for-the-badge&logo=jakartaee&logoColor=white)
![Apache POI](https://img.shields.io/badge/Apache%20POI-D22128?style=for-the-badge&logo=apache&logoColor=white)
![JFreeChart](https://img.shields.io/badge/JFreeChart-0099CC?style=for-the-badge)
![OpenWeather API](https://img.shields.io/badge/OpenWeather-FF6C00?style=for-the-badge&logo=OpenWeatherMap&logoColor=white)

![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)


---


## ✨ 주요 기능

### 📊 대시보드
- 당일 입출고 예정/완료 수, 출고 총액 표시
- 최근 입출고 완료 목록 제공
- 최근 7일간 입출고 수량 영역 차트
- 당일 출고 상위 지점 5곳 막대 차트 제공

### 👤 회원 관리
- **회원가입**: 아이디/이메일 중복 검사, 비밀번호 유효성 검사 및 암호화 저장
- **로그인**: 아이디/비밀번호 일치 여부 확인
- **아이디 찾기**: 이메일 일치 시 아이디 반환
- **비밀번호 찾기**: 아이디, 이메일 일치 시 랜덤 비밀번호 메일 발송 및 암호화 저장
- **회원 목록/수정/삭제**: 회원 조회, 이메일 수정, 상태값 ‘활성→탈퇴’로 관리
- **마이페이지**: 본인 정보 열람 및 비밀번호 제외 수정 가능, 탈퇴 처리 지원

### 🏢 공급사 관리
- 공급사 목록 조회 및 검색
- 공급사 정보 수정
- 다중 선택 후 ‘비활성’ 처리로 삭제

### 💬 실시간 채팅
- 본사와 지점 간 채팅 가능 (동일 서버 내)

### 📦 입고 관리
- **입고 예정 등록**: 상품, 수량, 입고예정일 입력
- **입고 상세 확인**: 다중 상품 포함, 검수 및 파손 처리 가능
- **입고 처리**: 중복 재고 병합, 신규 유통기한 분리 저장, 엑셀로 저장 가능

### 📤 출고 관리
- **출고 예정 등록**: 출고 대상 목록 확인 후 등록
- **출고 상세 처리**: 상세 정보 수정 및 출고 완료 상태 반영
- **주문 기반 출고 처리**: 주문 목록 확인 후 출고 등록

### 🏬 창고 관리
- 창고 리스트 조회 및 검색/정렬 기능 제공
- 창고 상세 페이지에서 입출고 내역과 재고 확인 가능

### 📦 재고 관리
- 현재 재고 조회: 창고별, 상품별, 유통기한별 분류 제공
- 재고 병합/분할: 동일 상품 + 유통기한 기준으로 병합, 분할 가능
- 재고 조정: 수량, 유통기한 등 수정 가능

### 🛒 상품 관리
- 상품 등록 및 수정: 상품명, 단위, 보관 조건 입력
- 상품 검색: 키워드 및 카테고리별 검색 기능
- 상품 상세 보기: 사진, 상품코드 등 확인 가능

### 🧾 주문하기
- 상품 테이블 제공: 검색/필터 가능
- 주문서 작성: 선택 상품과 수량 기반, 필수 입력 시에만 등록 가능

### 📃 주문 내역
- 발주 목록 조회 및 날짜별 필터
- 상세 내역 확인: 각 주문의 상세 테이블 제공

### 🏪 지점 관리
- 지점 목록 조회: 지점명, 상태 확인
- 지점 상태 수정: ‘활성’ / ‘탈퇴’ 상태로 변경 가능


---

## 🗄️ 데이터베이스 테이블 정의 및 역할
| 테이블명          | 역할 및 설명                                                                                  |
|-------------------|----------------------------------------------------------------------------------------------|
| **store_order**       | 지점에서 본사로의 상품 주문 정보 (주문일, 총 금액, 상태 등) 저장                               |
| **store_order_item**  | 주문 상세 항목 관리 (주문별 상품, 수량, 처리 상태 등)                                          |
| **io_receipt**        | 입출고 요청 정보 저장 (입고/출고 구분, 예정일, 상태 등)                                       |
| **io_detail**         | 입출고 상세 내역 관리 (계획 수량, 실제 수량, 파손 수량 등 포함)                               |
| **inventory**         | 창고별 상품 재고 수량 및 유통기한 정보 저장                                                  |
| **product**           | 상품 기본 정보 (상품명, 코드, 이미지, 가격, 보관 방식, 공급사) 관리                            |
| **snapshot**          | 입출고 시점의 상품 정보 스냅샷 저장 (변경 이력 추적용)                                        |
| **warehouse**         | 창고 정보 (이름, 주소, 보관 타입, 고유 코드 등) 저장                                         |
| **location**          | 지점 정보 (지점명, 주소, 상태 등) 관리                                                       |
| **headquarters_user** | 본사 사용자 계정 정보 (ID, 이메일, 비밀번호, 상태) 관리                                       |
| **location_user**     | 지점 사용자 계정 및 소속 지점 연결 정보 관리                                                 |
| **storage_type**      | 보관 유형 코드 정의 (상온, 냉장, 냉동 등)                                                    |
| **supplier**          | 공급사 정보 (이름, 주소, 상태 등) 저장                                                       |
| **damaged_code**      | 파손 사유 코드 및 설명 저장                                                                  |



---




## 🚀 향후 개선 예정
- 로컬에 저장한 이미지를 톰캣에 저장
- 예외처리, 트랜잭션 추가




