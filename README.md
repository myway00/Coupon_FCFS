# 윈터테크 인턴십 (서버 엔지니어 인턴) 과제 🥕 

> 선착순 쿠폰 API 구현하기 

윈터테크 인턴십 과제 명세서에 나온 내용을 구현한 프로젝트입니다. 
  - [로컬 환경 서버 구동 가이드 ](#로컬-환경-서버-구동-가이드 )
  - [API 명세서](#API-명세서)
![](../header.png)

## 로컬 환경 서버 구동 가이드 

`OS : 윈도우 ( cmd 창 기준입니다 ! )` 

### 0-0) 레디스 설치가 필요합니다 ! 
- [제가 레디스를 설치하고 실행했을 때 기록한 블로그 글](https://velog.io/@myway00/Spring-Boot-%EB%8F%99%EC%8B%9C-%EC%A0%91%EC%86%8D-%EC%9C%A0%EC%A0%80%EC%88%98-%EC%A0%9C%ED%95%9Cfeat.Redis-%EC%84%B8%EC%85%98-2#2-1--%EB%A0%88%EB%94%94%EC%8A%A4-%EC%84%A4%EC%B9%98--%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8%EC%99%80-%EC%97%B0%EB%8F%99) 포스트의 2-1 과정을 따라해서 설치해주시면 될 것입니다!  

### 0-1 ) h2 데이터베이스가 필요합니다 ! 
- h2 데이터베이스를 설치후 h2w.bat 으로 h2 데이터베이스를 실행시켜주세요 ! 
<center><img src="https://user-images.githubusercontent.com/76711238/203405816-2d38286c-6015-4e04-8748-4a37ef5acbdb.png" width="300" height="200"></center>

### 1) 실행 방법
- zip 파일을 다운 받아주세요 ! 
<center><img src="https://user-images.githubusercontent.com/76711238/203403089-e627de26-c3f0-4d34-85bd-48e994ce0800.png" width="300" height="300"></center>
- 다운받은 파일 경로로 들어가주세요 ! (제 경우엔 바탕화면에 저장했답니다 : `C:\Users\DONGYUN\Desktop\karrotpay-369-69169629003-74972841003-main` )
-  **gradlew build** 명령어를 쳐주세요 ! ( jre or jdk 설치가 필요해요 ! ) 

<center><img src="https://user-images.githubusercontent.com/76711238/203404328-f35552d9-7c6c-4bd2-94d2-f32a05d95ce6.png" width="600" height="80"></center>

- `cd build\libs` 명령어로 디렉토리를 이동해주세요 
- `java -jar 

 ## API 명세서 
 - 포스트맨에서 제공하는 DOCUMENTATION 기능을 사용했습니다 ! 
 [ 윈터테크 인턴십 과제 API 문서 링크 ] (https://documenter.getpostman.com/view/18959784/2s8YsnWFsL#33fde076-9ec1-403e-bc54-c75adbfce590) 
