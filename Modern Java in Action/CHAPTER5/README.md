# chapter 5 스트림 활용

&nbsp;

## 5.1 필터링

> 스트림의 요소를 선택하는 것


- **filter(predicate)** : predicate(boolean 반환하는 함수)를 인수로 받아서 일치하는 모든 요소를 포함하는 스트림 반환
- **filter().distinct()** : 고유 요소(hashcode, equals 사용)로 이루어진 스트림을 반환

예제 - filter, distinct

```java
List<Integer> numbers = Arrays.asList(1,2,1,3,3,2,4);
numbers.stream()
	.filter(i -> i % 2 == 0) //짝수이면 줍줍
	.distinct() //고유 요소만 줍줍
	.forEach(System.out::println); 
```

&nbsp;

&nbsp;

## 5.2 스트림 슬라이싱

> 스트림의 요소를 효과적으로 선택하거나 스킵하는 것
>

**predicate 이용방법**

- **takeWhile(predicate)** : 조건 false인 요소가 나오면 반복작업 멈추고 전까지의 요소 반환
- **dropWhile(predicate)** : 조건 false인 요소가 나오면 반복작업 멈추고 이후의 요소 반환

예제 - takeWhile

```java
//칼로리가 320 미만인 dish만 가지고 싶을 때
List<Dish> sliceMenu1 
= specialMenu.stream()
	.takeWhile(dish -> dish.getCalories() < 320)
	.collect(toList());
```

예제 - dropWhile

```java
//칼로리가 320 초과인 dish만 가지고 싶을 때 
List<Dish> sliceMenu2
= specialMenu.stream()
	.dropWhile(dish -> dish.getCalories() < 320)
	.collect(toList());
```

&nbsp;

**스트림 축소**

- **limit(n)** : n 이하의 크기를 갖는 새로운 스트림 반환

예제 - limit

```java
//칼로리가 300 초과인 dish 3개만 가지고 싶을 때
List<Dish> dishes
= specialMenu.stream()
	.dropWhile(dish -> dish.getCalories() > 300)
	.limit(3)
	.collect(toList());
```

&nbsp;

**요소 건너뛰기**

- skip(n) : 처음 n개 요소를 제외한 스트림을 반환

예제 - skip

```java
//칼로리가 300 초과인 dish에서 앞에 2요소는 원치 않을 때
List<Dish> dishes
= specialMenu.stream()
	.dropWhile(dish -> dish.getCalories() > 300)
	.skip(2)
	.collect(toList());
```
&nbsp;

&nbsp;

## 5.3 매핑

> 특정 데이터를 선택하는 것
>

**스트림의 각 요소에 함수 적용하기**

- **map()** : 인수로 제공 된 함수를 각 요소에 적용되어 새로운 요소로 매핑(이 때 값을 고치기보다 새로운 버전을 만드므로 매핑이라는 개념 사용)

예제 - map

```java
//음식의 이름의 길이를 알고 싶을 때
List<Integer> dishNameLengths 
= menu.stream()
	.map(Dish::getName)
	.map(String::length)
	.collect(toList());
```

&nbsp;

**스트림 평면화**

- **flatmap()** : 스트림의 각 값을 다른 스트림으로 만든 다음, 모든 스트림을 하나의 스트림으로 연결

예제 - flatmap

```java
String[] words = {"Hello", "World"};
//우리가 원하는 결과 : ["H", "e", "l", "o", "W", "r", "d"]

List<String> uniqueChar
= words.stream()                 //Stream<String>
	.map(words -> split("")) //Stream<String[]> H e l l o / W o r l d
	.flatMap(Arrays::stream) //Stream<String> H e l l o W o r l d
	.distinct() //Stream<String> H e l o W r d
	.collect(toList());
```

&nbsp;

&nbsp;

## 5.4 검색과 매칭

> 특정 속성이 데이터 집합에 있는지의 여부를 검색하는 것


**predicate가 적어도 한 요소와 일치하는지**

- **anyMatch(predicate)** : 일치하는 것이 있으면 true 반환 (최종연산)

예제 - anyMatch

```java
//메뉴 중에 isVegetrian이 true인 것이 있다면 해당 문구를 출력하고 싶을 떄
if(menu.stream().anyMatch(Dish::isVegetarian)){
	System.out.pritnln("The menu is Vegetarian friendly!");
}
```
&nbsp;

**predicate가 모든 요소와 일치하는지**

- **allMatch(predicate)** : 모든 요소가 일치하면 true 반환 (최종연산)
- **noneMatch(predicate) :** allMatch와 반대 연산, 일치하는 요소가 없으면 true 반환 (최종연산)

예제 - allMatch, noneMatch

```java
//모든 menu가 1000칼로리 이하인지 알고 싶을 때
boolean isHealty = menu.stream().allMatch(dish -> dish.getCalories() < 1000);

boolean isHealty = menu.stream().noneMatch(dish -> dish.getCalories() >= 1000);
```

- anyMatch, allMatch, noneMatch 모두 스트림 쇼트서킷 기법(자바의 &&, || 연산 활용)

&nbsp;

**요소 검색**

- **findAny() :** 현재 스트림에서 임의의 요소를 반환(최종연산)

예제 - findAny

```java
// menu에서 isVegetrian이 true인 아무 dish나 원할 때
Optional<Dish> dish
= menu.stream()
	.filter(Dish::isVegetarian)
	.findAny();p
```

&nbsp;

**첫번째 요소 찾기**

- **findFirst()** : 논리적인 아이템 순서가 정해져있을 때 첫번째 요소를 반환

예제 - findFirst

```java
//숫자 리스트에서 3으로 나누어떨어지는 첫번째 제곱값을 알고 싶을 때
List<Integer> nums = Arrays.asList(1,2,3,4,5);
Optional<Integer> firstSqaureDivisionByThree
= nums.stream()
	.map(n -> n*n)
	.filter(n -> n%3 == 0)
	.findFirst();
	//9
```

findFirst vs findAny

- 병렬실행에서는 첫번째를 찾기 힘드므로 요소의 반환 순서가 상관이 없다면 findAny

### 5.5 리듀싱

> 모든 스트림 요소를 처리해서 값으로 도출하는 연산 aka fold
>
- 스트림이 하나의 값으로 줄어들 때까지 람다는 각 요소를 반복해서 조합

**요소의 합**

- `reduce(초깃값, BinaryOperator<T>)`
    - BinaryOperator<T> : 두 요소를 조합해서 새로운 값을 만듦
- `reduce(BinaryOperator<T>)`
    - 초깃값이 없으면 연산결과로 Optional 반환

예제 - reduce

```java
 //numbers = {4,5,3,9};
//for-each 문 사용시
int sum = 0;
for(int x : numbers) {
	sum += x;
}

//reduce 사용 시
int sum = numbers.stream().reduce(0, (a,b) -> a + b);
```

1. a에 초깃값 0 사용
2. a(0) + b(4) = 4
3. a(4) + b(5) = 9
4. … 반복
5. 최종적으로 21이 도출됨

**최댓값과 최솟값**

- reduce를 이용하여 최댓값 or 최솟값 도출 가능

예제 - 최댓값

```java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```

예제 - 최솟값

```java
Optional<Integer> min = numbers.stream().reduce(Integer::min);
```

**map reduce pattern**

- 맵과 리듀스를 연결하는 기법
- 쉽게 병렬화함
    - 내부 반복에서 추상화되면서 병렬로 실행가능
    - for문은 sum변수를 공유해야하므로 병렬로 실행하기 쉽지않다

```java
int count = 
menu.stream()
     .map(d->1)
        .reduce(0, (a, b) -> a + b);
```

**참고**

스트림 연산 : 상태 없음과 상태 있음

- 내부 상태를 갖지 않는 연산 stateless operation
    - ex) map, filter
    - 각 요소를 받아 결과를 출력 스트림으로 보냄
- 내부 상태를 갖는 연산 stateful operation
    - ex) reduce, sum, max, sorted, distinct
    - 결과를 누적할 내부 상태가 필요하므로 데이터 스트림의 크기가 크거나 무한이라면 문제가 생길 수 있다!

### 5.7 숫자형 스트림

> 기본형 특화 스트림
>
- **박싱 비용**을 피할 수 있는 기본형 특화 스트림을 제공(max, min, sum, average)
    1. int 요소에 특화된 IntStream
    2. double 요소에 특화된 DoubleStream
    3. long 요소에 특화된 LongStream

**숫자 스트림으로 매핑 mapTo자료형**

- mapToInt, mapToDouble, mapToLong
- map과 같은 기능 수행하지만 반환 시 stream대신 특화stream반환

예제 - mapToInt

```java
int calories = 
menu.stream()  //stream<Dish> 반환
     .mapToInt(Dish::getCalories) //IntStream 반환
     .sum();
```

**객체 스트림으로 복원하기 boxed**

- IntStream을 다시 stream으로 변환할 때 사용

예제 - boxed

```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
Stream<Integer> stream = intStream.boxed();
```

**기본값 OptionalInt**

- 스트림에 요소가 없을 때 or 최댓값이 0 인 상황을 구분하기 위해 나옴

```java
OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
int max = maxCalories.orElse(1);
```

**숫자 범위 range, rangeClosed**

- 특정 숫자 범위를 이용하여 숫자 생성
- range (시작, 종료) : 시작과 종료 숫자 포함 X
- rangeClosed(시작, 종료) : 시작과 종료 숫자 포함

예제 - rangeClosed

```java
IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
System.out.println(evenNumbers.count());
```

### 5.8 스트림 만들기

> 스트림을 만들 수 있는 다양한 방법 소개
>

**값으로 스트림 만들기**

- 임의의 수를 인수로 받는 정적 메서드 Stream.of로 스트림을 만들 수 있음

예제 - Stream.of()

```java
Stream<String> stream = Stream.of("Modern ", "Java ", "In ", "Action ");
stream.map(String::toUpperCase).forEach(System.out::println);
```

- empty 로 스트림을 비울 수 있음

예제 - empty

```java
Stream<String> stream = Stream.emtpy();
```

**null이 될 수 있는 객체로 스트림 만들기**

- ofNullable 메소드로 null이면 알아서 빈 스트림을 반환해줌

예제 - ofNullable

```java
//ofNullable을 사용하지 않았을 때
String homeValue = System.getProperty("home");
Stream<String> homeValueStream
= homeValue == null ? Stream.emtpy() : Stream.of(value);

//ofNullable
Stream<String> values =
	Stream.ofNullable(System.getProperty("home"));

```

**배열로 스트림 만들기**

- Arrays.stream()으로 배열을 스트림으로 만들 수 있다

예제 - Arrays.stream

```java
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum(); //IntStream반환
```

**파일로 스트림 만들기**

**함수로 무한 스트림(unbound) 만들기**

- interate, generate 로 무한 스트림을 만들 수 있음(크기가 고정되지 않은 스트림)
- 요청할 때 마다 주어진 함수를 이용해서 값을 만듦
- 보통은 무한히 생성하지 않도록 limit(n)과 함께 이용
- predicate 지원

**iterate 메서드**

예제

```java
Stream.iterate(0, n -> n + 2)
        .limit(10)
        .forEach(System.out.println);
```

- 0, 2, 4, 6, 8, 10 … 10개까지 생성

예제 - predicate

```java
IntStream.iterate(0, n -> n < 100, n -> n + 4)
        .forEach(System.out.println);
```

- 이때 단순히 filter로 동일한 동작을 하는 코드를 작성할 수 있다고 착각할 수도 있으나  filter는 함수를 언제 멈춰야할지 알지 못하므로 영원히 작동하는 코드가 됨 → forWhile을 쓰는 것이 해법

**generate 메서드**

- iterate와 비슷하게 요구할 때 값을 계산하는 무한 스트림을 만듦
- 다른점은 generate는 생산된 각 값을 연속적으로 계산 X, Supplier<T> 를 인수로 받아서 새로운 값을 생산
- iterate는 새로운 값을 생성하면서도 기존 상태를 바꾸지 않는 순수한 불변상태를 유지