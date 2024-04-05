# 요구사항
1. 특정 사용자가 해당 작품에 대해 평가(좋아요/싫어요, 댓글) API, 작품당 1개의 평가(여러 작품은 가능), 좋아요/싫어요 필수, 댓글(특수문자 불가) 선택
2. 좋아요가 가장 많은 작품 3개, 싫어요가 가장 많은 작품 3개 조회 API
3. 작품별로 언제 어떤 사용자가 조회했는지 이력 조회 API
4. 최근 1주일간 등록 사용자중 성인물 3개 이상 본 사용자 조회 API
5. 특정 작품을 유료, 무료 변경 API(무료시 금액 0원, 유료시 금액(100~500))
6. 특정 사용자 삭제시 해당 사용자 정보와 평가, 조회 이력 모두 삭제 API

# DB 테이블 정의서

## users 테이블

| 컬럼명         | 데이터 유형                 | 제약 조건       | 설명                   |
|---------------|------------------------|----------------|----------------------|
| user_id       | BIGINT, AUTO_INCREMENT | NOT NULL, PK   | 고유 식별자               |
| name          | VARCHAR(255)           |                | 이름                   |
| email         | VARCHAR(255)           |                | 이메일                  |
| gender        | VARCHAR(255)           |                | 성별(MALE,FEMALE)      |
| age_type      | VARCHAR(255)           |                | 연령 유형(COMMON,ADULT)  |
| register_date | TIMESTAMP              | NOT NULL       | 등록 일시                |

- 제약 조건:
  - `user_id`: NOT NULL, Primary Key
  - `register_date`: NOT NULL

## contents 테이블

| 컬럼명           | 데이터 유형                 | 제약 조건       | 설명                   |
|-----------------|------------------------|----------------|----------------------|
| contents_id     | BIGINT, AUTO_INCREMENT | NOT NULL, PK   | 고유 식별자               |
| name            | VARCHAR(255)           |                | 이름                   |
| author          | VARCHAR(255)           |                | 저자                   |
| charge_type     | VARCHAR(255)           |                | 청구 유형(PAY,FREE)      |
| age_type        | VARCHAR(255)           |                | 연령 유형(COMMON,ADULT)  |
| coin            | INT                    |                | 코인                   |
| open_date       | TIMESTAMP              |                | 공개일시                 |

- 제약 조건:
    - `contents_id`: NOT NULL, Primary Key
    - `name`: 문자열 최대 길이 255
    - `charge_type`: 청구 유형 (FREE, PAY)
    - `age_type`: 연령 유형
    - `coin`: 0 이상 500 이하의 정수
    - `open_date`: 날짜와 시간 정보

## evaluation 테이블

| 컬럼명           | 데이터 유형                 | 제약 조건       | 설명                 |
|-----------------|------------------------|----------------|--------------------|
| evaluation_id   | BIGINT, AUTO_INCREMENT | NOT NULL, PK   | 고유 식별자             |
| user_id         | BIGINT                 | NOT NULL       | users의 고유 식별자      |
| contents_id     | BIGINT                 | NOT NULL       | contents의 고유 식별자   |
| evaluation_type | VARCHAR(255)           | NOT NULL       | 평가 유형(LIKE,DISLIE) |
| comment         | VARCHAR(255)           |                | 평가에 대한 코멘트         |
| register_date   | TIMESTAMP              | NOT NULL       | 등록 일시              |

- 제약 조건:
  - `evaluation_id`: NOT NULL, Primary Key
  - `user_id`: NOT NULL, 외래 키(FK) 참조: users(user_id)
  - `contents_id`: NOT NULL, 외래 키(FK) 참조: contents(contents_id)
  - `evaluation_type`: NOT NULL
  - `register_date`: NOT NULL

- 유니크 제약 조건:
  - (user_id, contents_id) 유니크 제약 조건


## view_history 테이블

| 컬럼명              | 데이터 유형                 | 제약 조건       | 설명               |
|--------------------|------------------------|----------------|------------------|
| view_history_id    | BIGINT, AUTO_INCREMENT | NOT NULL, PK   | 고유 식별자           |
| contents_id        | BIGINT                 | NOT NULL       | contents의 고유 식별자 |
| user_id            | BIGINT                 | NOT NULL       | users의 고유 식별자    |
| created_date       | TIMESTAMP              | NOT NULL       | 생성 일시            |

- 제약 조건:
  - `view_history_id`: NOT NULL, Primary Key
  - `contents_id`: NOT NULL, 외래 키(FK) 참조: contents(contents_id)
  - `user_id`: NOT NULL, 외래 키(FK) 참조: users(user_id)
  - `created_date`: NOT NULL

# API 정의 문서

## 특정 사용자가 해당 작품에 대해 평가를 할 수 있는 API

- **URL**: `/contents/{contentsId}/evaluation`
- **Method**: `POST`
- **Header**:
  - Authorization : `userId` : users의 고유 식별자
- **Parameters**:
  - `contentsId` (Path variable): contents의 고유 식별자
- **Request Body**:
  ```json
  {
    "evaluationType": "LIKE",
    "comment": "좋은 내용입니다."
  }
  ```
  - `evaluationType` (필수): 평가 유형 (`LIKE` 또는 `DISLIKE`)
  - `comment` (옵션): 추가적인 코멘트
- **Response**:
  - `200 OK`: 요청이 성공하고 결과 데이터가 반환됨
  - Response Body:
    ```json
    {
      "data": 123456,  // 평가 저장 결과의 고유 식별자
      "message": null
    }
    ```
    
## 특정 작품을 유료, 무료로 변경할 수 있는 API

- **URL**: `/contents/{contentsId}`
- **Method**: `PUT`
- **Parameters**:
  - `contentsId` (Path variable): contents의 고유 식별자
- **Request Body**:
  ```json
  {
    "chargeType": "PAY",
    "coin": 100
  }
  ```
  - `chargeType` (필수): contents의 요금 유형 (`FREE`, `PAY`)
  - `coin` (옵션): 요금 유형이 `PAY`인 경우 필요한 코인 수량
- **Response**:
  - `200 OK`: 요청이 성공하고 결과 데이터가 반환됨
  - Response Body:
    ```json
    {
      "data": 123456,  // 업데이트된 contents의 고유 식별자
      "message": null
    }
    ```

## 작품별로 언제, 어떤 사용자가 조회했는지 이력을 조회하는 API

- **URL**: `/contents/{contentsId}/view-histories`
- **Method**: `GET`
- **Parameters**:
  - `contentsId` (Path variable): contents의 고유 식별자
- **Response**:
  - `200 OK`: 요청이 성공하고 결과 데이터가 반환됨
  - Response Body:
    ```json
    {
      "data": [
        {
          "id": 123456,  // view_history의 고유 식별자
          "contents": {
            "id": 7890,  // contents의 고유 식별자
            "contentsName": "Sample Contents",
            "author": "John Doe"
          },
          "user": {
            "id": 9876,  // users의 고유 식별자
            "userName": "johndoe",
            "userEmail": "johndoe@example.com",
            "gender": "MALE",
            "ageType": "ADULT",
            "registerDate": "2022-12-31T18:30:00"
          },
          "createdDate": "2022-12-31T20:15:30"
        },
        {
          "id": 789012,
          "contents": {
            "id": 1234,
            "contentsName": "Another Contents",
            "author": "Jane Smith"
          },
          "user": {
            "id": 54321,
            "userName": "janesmith",
            "userEmail": "janesmith@example.com",
            "gender": "FEMALE",
            "ageType": "ADULT",
            "registerDate": "2022-10-15T09:45:00"
          },
          "createdDate": "2022-10-15T12:30:45"
        }
      ],
      "message": null
    }
    ```

## '좋아요' 또는 '싫어요'가 가장 많은 작품 N개를 조회하는 API

- **URL**: `/contents/top-rated`
- **Method**: `GET`
- **Parameters**:
  - `evaluationType` (Query parameter): 평가 유형 (예: "LIKE", "DISLIKE")
  - `limit` (Query parameter): 결과 제한 수 (예: 3 = top3)
- **Response**:
  - `200 OK`: 요청이 성공하고 결과 데이터가 반환됨
  - Response Body:
    ```json
    {
      "data": [
        {
          "id": 1234,  // contents의 고유 식별자
          "contentsName": "Sample Contents",
          "author": "John Doe"
        },
        {
          "id": 5678,
          "contentsName": "Another Contents",
          "author": "Jane Smith"
        },
        {
          "id": 2342,
          "contentsName": "the Contents",
          "author": "Will Smith"
        }
      ],
      "message": null
    }
    ```

## 최근 1주일간 등록된 사용자 중 성인작품 3개 이상 조회한 사용자 목록을 조회하는 API

- **URL**: `/users/adult-search/recent`
- **Method**: `GET`
- **Response**:
  - `200 OK`: 요청이 성공하고 결과 데이터가 반환됨
  - Response Body:
    ```json
    {
      "data": [
        {
          "id": 1234,  // users의 고유 식별자
          "userName": "John Doe",
          "userEmail": "johndoe@example.com"
        },
        {
          "id": 5678,
          "userName": "Jane Smith",
          "userEmail": "janesmith@example.com"
        }
      ],
      "message": null
    }
    ```

## 특정 사용자 삭제 시 해당 사용자 정보와 사용자의 평가, 조회 이력 모두 삭제하는 API

- **URL**: `/users/{userId}`
- **Method**: `DELETE`
- **Parameters**:
  - `userId` (Path parameter): 사용자의 고유 식별자
- **Response**:
  - `200 OK`: 요청이 성공함


# API 테스트 방법

## 테스트 코드 실행

- Controller
  - ContentsControllerTest
  - UserControllerTest
- Service
  - ContentsServiceTest
  - EvaluationServiceTest
  - UserServiceTest
  - ViewHistoryServiceTest

## Swagger-ui릍 통한 테스트

- 애플리케이션 실행 후 http://localhost:8080/swagger-ui/index.html 접속
- Authorize 버튼을 클릭하여 Authorization Header의 userId 값 세팅 가능
- 사용방법 
  1. 사용할 API 클릭
  2. 오른쪽 상단의 Try it out 버튼 클릭
  3. 원하는 값 세팅 후 Execute 버튼 클릭

## 테스트 데이터 설명
- users : 7명의 유저 등록
  - 1~4번 유저는 현재시간으로 등록
  - 5~7번 유저는 10일전 시간으로 등록
- contents : 6개의 작품 등록
  - 1~3번 작품은 성인작품으로 동록
  - 4~6번 작품은 일반작품으로 동록
- view_history : 18개의 이력 등록
  - 1번 유저 성인물 3개(중복 제거) 조회
  - 2번 유저 성인물 2개(중복 제거) 조회
  - 3번 유저 성인물 3개(중복 제거) 조회
  - 5번 유저 성인물 3개(중복 제거) 조회(5번 유저는 10일전에 등록된 사용자라 일주일전 등록된 사용자에 조회 X)
- evaluation : 30개의 평가 등록
  - 작품 1 = 좋아요 2 싫어요 4
  - 작품 2 = 좋아요 2 싫어요 3
  - 작품 3 = 좋아요 2 싫어요 2
  - 작품 4 = 좋아요 3 싫어요 1
  - 작품 5 = 좋아요 4 싫어요 1
  - 작품 6 = 좋아요 5 싫어요 1
  - 좋아요 Top 3 = 작품 6, 작품 5, 작품 4
  - 싫어요 Top 3 = 작품 1, 작품 2, 작품 3

## 결과
불합격
