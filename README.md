# API 명세서

이 문서는 파일 확장자 차단 시스템의 API 명세를 제공합니다. 각 API에 대한 요청/응답 모델과 에러 응답 모델을 포함하고 있습니다.


## 목차
1. [공통 응답 포맷](#공통-응답-포맷)
2. [에러 응답 모델](#에러-응답-모델)
3. [파일 확장자 API](#파일-확장자-api)
4. [파일 저장 API](#파일-저장-api)

## 공통 응답 포맷

성공적인 API 응답은 다음 형식을 따릅니다:

```json
{
 "success": true,
 "data": { /* 응답 데이터 */ },
 "message": "요청이 성공적으로 처리되었습니다."
}
```

## 에러 응답 모델

에러 발생 시 응답은 다음 형식을 따릅니다:

```json
{
 "success": false,
 "error": {
   "message": "에러 메시지",
   "details": { /* 추가적인 에러 정보 (선택적) */ }
 }
}
```

## 파일 확장자 API

### 커스텀 차단 확장자 등록

**POST /api/admin/extensions/custom**

새로운 커스텀 차단 확장자를 등록합니다.

**요청 모델**
``` json
{
 "extension" : "jpg"
}
```

**응답 모델 (성공 - 201 Created)**
```json
{
 "success": true,
 "data": null,
 "message": "커스텀 차단 확장자가 성공적으로 등록되었습니다."
}
```

**에러 응답 예시 (400 Bad Request)**
```json
{
 "success": false,
 "error": {
   "message": "이미 등록된 커스텀 차단 확장자입니다.",
 }
}
```

### 커스텀 차단 확장자 삭제

**DELETE /api/admin/extensions/custom/{extension}**

특정 커스텀 차단 확장자를 삭제합니다.

**응답 모델 (성공 - 200 OK)**
```json
{
 "success": true,
 "data": null,
 "message": "커스텀 차단 확장자를 성공적으로 삭제했습니다."
}
```

### 기본 차단 확장자 업데이트

**PATCH /api/admin/extensions/default/{extension}**

기본 차단 확장자를 업데이트합니다. (on <-> off)

**응답 모델 (성공 - 200 OK)**
```json
{
 "success": true,
 "data": null,
 "message": "기본 차단 확장자를 성공적으로 변경했습니다."
}
```

### 모든 차단 확장자 목록 조회

**GET /api/admin/extensions**

차단 확장자의 목록을 조회합니다.

**응답 모델 (성공 - 200 OK)**
```json
{
 "success": true,
 "data": {
   "defaultExtensions": [
     {
       "extension" : "bat",
       "checked" : false
     }, 
     {
       "extension" : "cmd",
       "checked" : true
     }, 
     {
       "extension" : "com",
       "checked" : true
     }, 
     {
       "extension" : "cpl",
       "checked" : false
     }, 
     {
       "extension" : "exe",
       "checked" : true
     } 
   ],
   "customExtensions": [
     "doc", "docx", "pdf", "hwp"
   ]
 },
 "message": "모든 차단 확장자 목록을 성공적으로 조회했습니다."
}
```

## 파일 저장 API

### 파일 업로드 API

**POST /api/upload**

선택한 파일을 서버에 업로드합니다.

**요청 파라미터**
```
?file
```

| 파라미터 | 타입            | 필수 여부 | 설명      |
| ---- | ------------- | ----- | ------- |
| file | Multipartfile | 예     | 업로드할 파일 |

**응답 모델 (성공 - 200 OK)**
```json
{
 "success": true,
 "data": {
   "name" : "test.txt",
   "extension" : "txt",
   "created_at" : "2025-07-24T15:30:00Z"
 },
 "message": "파일을 성공적으로 업로드하였습니다."
}
```

**에러 응답 예시 (400 Bad Request)**
```json
{
 "success": false,
 "error": {
   "message": "허용되지 않은 파일 형식입니다."
 }
}
```

**에러 응답 예시 (500 Internal Server Error)**
```json
{
 "success": false,
 "error": {
   "message": "서버 문제로 인해 업로드가 불가능합니다."
 }
}
```

