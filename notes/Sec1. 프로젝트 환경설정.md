# 1. 프로젝트 환경설정

## 프로젝트 생성
### 스프링 부트 스타터로 프로젝트 생성
* Maven vs Gradle
  * 빌드 관리 도구
  * 빌드: 소스코드 파일을 jvm이나 톰캣 같은 WAS가 인식할 수 있도록 패키징하는 과정 및 결과물
  * 빌드 관리 도구는 종속성 다운로드, 컴파일, 패키징, 테스트 실행, 배포 등의 작업을 수행
  * Ant, Maven, Gradle 등이 있음

  * Maven
    * Java 전용 프로젝트 관리 도구, 정해진 Lifecycle에 의해 작업 수행
    * Ant의 단점을 보완하여 표준화된 포맷을 제공하나, 여전헤 POM.xml이라는 xml 파일로 관리
    * 멀티 프로젝트 상속 방식으로 지원
  * Gradle
    * Java, C/C++, Python 등 지원
    * 빌드 속도 Maven에 비해 10~100배 이상 빠름 (캐싱O)
    * Groovy script (JVM에서 실행되는 스크립트 언어)를 기반으로 구축
    * 멀티 프로젝트 빌드 지원 (설정 주입 방식, Configuration Injection)
  
    → 스크립트 길이, 가독성, 속도 면에서 Gradle이 우세

* JSP vs Thymeleaf
  * 일반적인 Spring MVC 구조에서 View의 역할
  * JSP
    * Servlet 형태로 변환되어 WAS에서 동작하며 필요 기능 수행 후 생성된 데이터를 웹페이지와 함께 클라이언트로 응답함
    * Servlet은 자바 소스이므로 jsp 파일 내에서 java 코드 사용 가능 → View에 비즈니스 로직이 포함되어 복잡해짐
    * war(web application archive) 패키징만 가능 → WAS 필요, 사전에 정의된 구조만 사용 가능하므로 복잡성 증가
    * 응답성 측면에서는 Thymeleaf 보다 빠름
  * Thymeleaf
    * HTML 파일을 파싱하고 분석해 정해진 위치에 데이터를 치환해 웹페이지 생성
    * jar(java archive) 파일로 export 가능

  → thymeleaf를 더 많이 사용하는 추세

* 기타 사용 기능들
  * Spring Data JPA
    * JPA: 객체-관계 매핑을 위한 표준 명세인 '인터페이스'
    * Hibernate: JPA의 '구현체', 객체와 RDB 간 매핑을 자동처리하는 라이브러리
    * Spring Data JPA: Repository 인터페이스를 제공하여 JPA 기반 애플리케이션 개발을 간편하게 하는 라이브러리, 일반적인 데이터베이스 작업에 사용되는 메서드를 미리 정의
  * H2: 인메모리 관계형 데이터베이스로 로컬, 테스트 환경에서 간편하게 사용
  * Lombok 어노테이션 기반으로 코드를 자동완성해주는 라이브러리, Getter, Setter, Equals, ToString 등

## View 환경 설정
* 스프링에서 Thymeleaf 권장
  * 장점: 마크업 형태 유지, 브라우저에서 열림
  * 단점(v2.0 이하): 태그 매칭 안되는 경우 에러 발생 → 3.0 이후 개선됨 (ex. \<BR\>)

* HelloController
  * org.springframework.ui.Model
    * Model은 controller에서 생성된 데이터를 담아 view에 전달할 때 사용되는 객체
    * return string은 관례상 화면 이름
* 타임리프에서 viewName을 자동으로 매핑해줌
  * templates/{viewNane}.html → 템플릿엔진으로 렌더링O
  * static/{viewName}.html → 순수 html, 정적컨텐츠
* 요즘은 SSR 보다 CSR 프레임워크 많이 쓰는 추세

[ 참고자료 ]
* [Maven과 Gradle의 개념 및 비교](https://velog.io/@leesomyoung/Maven%EA%B3%BC-Gradle%EC%9D%98-%EC%B0%A8%EC%9D%B4-%EB%B0%8F-%EB%B9%84%EA%B5%90)
* [[build] ant, maven, gradle 차이점](https://blog.naver.com/rorean/222236619759)
* [[Spring]JSP vs Thymeleaf](https://velog.io/@posasac/SpringJSP-vs-Thymeleaf)
* [H2 Database 란? 그리고 사용법?](https://yjkim-dev.tistory.com/3)
* [[Java] Lombok이란? 및 Lombok 활용법](https://mangkyu.tistory.com/78)
* [[Spring] Model 객체와 @ModelAttribute 어노테이션](https://dev-coco.tistory.com/100)