# Ch 7 JavaConfig와 @Bean 메서드
**JavaConfig**
- DI 컨테이너에 불러올 설정을 자바의 클래스에 작성하기 위한 스프링의 기능
```java
@Configuration
public class FooConfig {
  ...
}
```

## 여러개 Javaconfig 불러오는 법
### AnnotationConfigApplicationContext 생성자 인수로 지정하기
- AnnotationConfigApplicationContext 클래스의 생성자 인수로 여러개의 JavaConfig 클래스를 지정해줄 수 있음
```java
AnnotationConfigApplicationContext context =
  new AnnotationConfigApplicationContext(FooConfig.class, BarConfig.class);
```
### @Import로 가져오기
- 한 JavaConfig 클래스에서 다른 JavaConfig 클래스를 가져올 수 있음
```java
@Configuration
@Import(BarConfig.class)
public class FooConfig {
  ...
}
```

```java
@Configuration
public class BarConfig {
  ...
}
```

```java
AnnotationConfigApplicationContext context =
  new AnnotationConfigApplicationContext(FooConfig.class);
```
### 컴포넌트 스캔하기 
```java
package foo;

@Configuration
@CompnentScan
public class FooConfig {
  ...
}
```

```java
package foo;

@Configuration
public class BarConfig {
  ...
}
```

```java
AnnotationConfigApplicationContext context =
  new AnnotationConfigApplicationContext(FooConfig.class);
```

- AnnotationConfigApplicationContext로 fooconfig를 지정하면 Foo 패키지가 베이스 패키지로 하여 컴포넌트 스캔 진행
  - foo 패키지 포함 ~ 하위 패키지를 모두 컴포넌트 스캔을 진행
- @Configuration은 스테레오타입 애너테이션이므로 BarConfig로 불러온다

❓ 그럼 어떤 방식을 쓰면 되나욤

❗ 어떤 방법을 사용해도 상관없으나 JavaConfig가 늘어나는 상황에서는 컴포넌트 스캔 방식이 편리함 

❗ 컴포넌트 스캔할 패키지 아래에 JavaConfig class를 작성해주기만 하면되고, 어딘가에 지정해줄 필요가 없기때문에 누락 실수를 막을 수 있음

## @Bean 메서드란?
> JavaConfig클래스 안에서 @Bean을 붙인 메서드
```java
@Configuration
public class FooConfig {
  @Bean
  public FooService fooService() {
    return new FooService();
  }
}
```
- Bean 메서드는 DI 컨테이너에서 자동으로 호출해주고, 반환된 객체는 Bean으로 관리

**Injection**
- @Bean안에서 injection로 작성 가능
```java
@Bean
public TrainingService trainingService(TrainingRepository trainingRepository) {
  TrainingServiceImpl trainingServiceImpl = new TrainingServiceImpl(trainingRepository);
}

@Bean 
public TrainingRepository trainingRepository() {
  return new JdbcTrainingRepository();
} 
```
`TrainingServiceImpl` -> `JdbcTrainingRepository`

## 적절할 Bean 정의 방법 선택하기

❓ @Bean 사용 or 스테레오타입 애너테이션

| Bean 정의 방법 | 장점 | 단점 |
|--|--|--|
| 스테레오 타입 애너테이션 | 대량의 Bean정의를 간결하게 할 수 있다 | 개발자가 작성한 클래스에만 Bean 정의를 할 수 있다 |
| @Bean 메서드 | 라이브러리가 제공하는 클래스에도 Bean 정의를 할 수 있다 | 대량의 Bean 정의는 힘들다 |

❗ 결론 : 라이브러리의 클래스는 @Bean 메서드 사용 / 개발자가 만든 Repository, Service, Controller 같은 클래스는 스테레오타입 애너테이션 사용

## JavaConfig와 프로파일
JavaConfig 클래스도 프로파일을 할당해 그룹화할 수 있음
```java
@Profile("foo")
@Configuration
@ComponentScan
public class FooConfig {
  @Bean
  public FooService fooService() {

  }
}
```
- 프로파일 foo가 활성화된 경웨만 사용됨