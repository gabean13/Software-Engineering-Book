# Chapter 8 컬렉션 API 개선
## 8.1 컬렉션 팩토리
> 작은 컬렉션 객체를 쉽게 만들 수 있는 방법

#### 적은 요소를 포함하는 리스트를 만들 때
ex) 세 친구 이름을 포함한 그룹
1. 그냥 List에 할당하는 방법
```java
List<String> friends = new ArrayList<>();
friends.add("Raphael");
friends.add("Olivia");
friends.add("Thibaut");
```

2. Arrays.asList() 팩토리 메서드를 사용하는 방법
```java
List<String> friends 
    = Arrays.asList("Raphael", "Olivia", "Thibaut");
```
- 고정 크기 리스트 이므로 요소 갱신만 가능(삭제, 추가 불가 Unsupported Operation Exception)

### 8.1.1 리스트 팩토리
**List.of 팩토리 메소드**
```java
List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
```
- 리스트 변경 불가(UnsupportedOperationException)
- null 요소 금지


- List.of에 다양한 오버로드 버전이 있음 (요소가 4개, 5개 .. )
  - 처음부터 가변 인수를 사용하지 않은 이유 : 가변 인수 사용 시 추가 배열을 할당해서 리스트로 감싸야하므로(가비지 컬렉션 비용) 고정된 숫자의 요소를 API로 정의해 이런 비용을 제거함

❓ 컬렉션 메서드와 스트림 API를 어떻게 구별해서 사용해야하나요
❗ 데이터를 변환할 필요가 없다면 사용하기 간편한 팩토리 메서들르 이용하자

### 8.1.2 집합 팩토리
```java
Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
```
- 중복 요소 사용 시 IllegalArgumentException 발생 
### 8.1.3 맵 팩토리
1. 10개 이하의 맵을 만들 때
```java
Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
```

2. 그 이상의 맵을 만들 때
```java
Map<String, Integer> ageOfFriends 
        = Map.ofEntries(
                entry("Raphael", 30),
                entry("Olivia", 25), 
                entry("Thibaut", 26));
```

## 8.2 리스트와 집합 처리
### 8.2.1 removeIf 메서드
> predicate를 만족하는 요소를 제거. List나 Set을 구현하거나 그 구현을 상속받은 모든 클래스에서 사용 가능
```java
for(Transaction transaction : transactions) {
    if(어떤 조건) {
        transactions.remove(transaction);    
    }    
}
```
- **문제점** : 위 코드는 `ConcurrentModificationException` 발생
  - for-each는 Iterator객체를 사용 / Collection 객체 자체가 remove()를 호출해 요소를 삭제
  - 결국 두개의 개별 객체가 컬렉션을 관리하여 서로 동기화가 되지않음
- **해결책** : Iterator를 명시적으로 사용하여 iterator.remove()를 호출
```java
for(Iterator<Transaction> iterator  = transactions.iterator(); iterator.hasNext();) {
    Transaction transaction = iterator.next();
    if(어떤 조건) {
        iterator.remove();   
    }
}
```
- 이 코드 패턴 == removeIf 메서드
```java
transactions.removeIf(transaction -> 어떤조건);
```

### 8.2.2 replaceAll 메서드
> 리스트에서 이용할 수 있는 기능으로 UnaryOperator 함수를 이용해 요소를 바꾼다
- 새 컬렉션을 생성하지 않고 기존 컬렉션을 바꾸는 것 = ListIterator객체를 이용
```java
for(ListIterator<String> iterator = referenceCodes.listIterator(); iterator.hasNext();) {
    String code = iterator.next();
    iterator.set(바꿀것);
}
```
- 이 코드 패턴 = replaceAll
```java
referenceCodes.replaceAll(바꿀것)
```

## 8.3 맵 처리
### 8.3.1 forEach 메서드 
- 맵에서 키와 값을 반복자를 확인하려면 `Map.Entry<K,V>` 의 반복자를 이용하여 맵의 항목 집합을 반복하는데 이를 forEach로 간단하게 구현할 수 있음
```java
ageOfFriends.forEach((friends, age) -> System.out.println(friends + "is" + age + "years old"));
```
### 8.3.2 정렬 메서드
- Entry.comparingByValue
- Entry.comparingByKey
```java
favoriteMovie.entrySet().stream().sorted(Entry.comparingByKey()).forEachOrdered(System.out::println);
```
### 8.3.3 getOrDefault 메서드
- 찾으려는 키가 존재하지 않으면 NullPointerException 발생
- 기본값을 반환하는 방식으로 해결=
- 첫번째 : 키 / 두번째 : 기본값(키가 존재하지 않으면 반환할 값)

```java
favoriteMovie.getOrDefault("Olivia", "Matrix");
```
### 8.3.4 계산 패턴
> 맵에 키가 존재하는지 여부에 따라 어떤 동작을 실행
- `computeIfAbsent` : 키가 해당하는 값이 없으면 새 값을 계산하고 맵에 추가
- `computeIfPresent` : 키가 해당하는 값이 있으면 새 값을 계산하고 맵에 추가
- `compute` : 제공된 키로 새 값을 계산하고 맵에 저장

### 8.3.5 삭제 패턴
- remove(key, value)로 간편하게 요소 삭제 가능

### 8.3.6 교체 패턴
- `replaceAll` : BiFunction을 적용한 결과로 각 항목의 값 교체
- `Replace` : 키가 존재하면 값을 바꾼다
```java
favoriteMovie.replaceAll((friends, movie) -> movie.toUpperCase());
```
### 8.3.7 합침
- `putAll` : 두개의 맵을 합칠 때 + 중복된 키가 없을 때
```java
Map<String, String> family = Map.ofEntries(어쩌구);
Map<String, String> friends = Map.ofEntries(어쩌구);

Map<String, String> everyone = new HashMap<>(family);
everyone.putAll(friends);
```

- `merge` : 중복된 키를 어떻게 합칠지 결정하는 Bifunction을 인수로 받음
```java
Map<String, String> everyone = new HashMap<>(family);
friends.forEach((k, v) ->
        everyone.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
```

## 8.4 개선된 ConcurrentHashMap
> concurrentHashMap : 동시성 친화적, 읽기 쓰기 연산 성능이 월등

### 8.4.1 리듀스와 검색
- `forEach` : 각 (키, 값)에 주어진 액션 실행
- `reduce` : 모든 (키, 값) 쌍을 제공된 리듀스 함수를 이용해 결과로 합침
- `search` : 널이 아닌 값을 반환할 때까지 각 (키, 값)쌍에 함수를 적용
- 키, 값 / 키 / 값 / Map.Entry로 연산 지원

- 계산이 진행되는 동안 바뀔 수 있는 객체, 값, 순서 등에 의존하지 않아야함

### 8.4.2 계수
- `mappingCount` : 맵의 매핑 개수 반환

### 8.4.3 집합 뷰
- `keySet` : 집합 뷰로 변환해줌 
  - 맵을 바꾸면 집합도 바뀌고 집합을 바꾸면 맵도 영향을 받음