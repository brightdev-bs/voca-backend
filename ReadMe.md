# Voca-Backend
## 네이밍 컨벤션
### 이름
1. 패키지 이름은 소문자로 구성한다.
2. 클래스, 인터페이스는 카멜표기법을 사용한다.
3. 클래스 이름은 명사를 사용한다.
4. 인터페이스 이름은 명사/형용사를 사용한다.
5. 테스트 클래스는 'Test'로 끝나게 한다. 
6. 메서드 이름은 동사/전치사로 시작한다. 
~~~java
// 동사 사용
renderHtml();
// Builder 클래스 메서드 
withUserId(String id)
~~~
7. 임시 변수 외에는 1 글자 이름은 사용하지 않는다.

### 선언
1. static import에만 와일드카드(*)를 사용한다.
2. 제한자 선언은 public -> protected -> private -> abstract -> static -> final -> transiet -> volatile -> synchronized -> natvie -> strictfp 순으로 쓴다.
3. 애너테이션 선언 후 새줄을 사용한다.
4. 한 줄에 한 문장만 쓴다.

### 중괄호
1. K&R 스타일을 따른다.
~~~java
public class SearchConditionParser {
    public boolean isValidExpression(String exp) {
        if (exp == null) {
            return false;
        }
        return true;
    }
}
~~~
2. 닫는 중괄호와 같은 줄에 else, catch, finally, while 선언
~~~java
if (line.startWith(WARNING_PREFIX)) {
    return LogPattern.WARN;
} else if (line.startWith(DANGER_PREFIX)) {
    return LogPattern.NORMAL;
} else {
    return LogPattern.NORMAL;
}
~~~

3. 조건/반복문에 중괄호 필수 사용 (한 줄이라도 예외를 두지 않는다.)

### 줄바꿈
1. 줄을 바꾸는 위치는 다음 중 하나로 한다.
~~~java
1 ) extends 선언 후
2 ) implements 선언 후
3 ) throws 선언 후
4 ) 시작 소괄호 ( 선언 후 
5 ) 콤마(,) 후
6 ) . 전
7 ) 연사자 전
~~~

### URI 규칙
1. 마지막에 '/'를 포함하지 않는다.
2. 복수형 명사와 구체적인 이름을 사용한다.
3. 언더바(_) 대신 -(dash)를 사용한다.
4. 소문자를 사용한다.
5. CRUD 함수 이름과 HTTP Method는 URI에 포함하지 않는다.

### 컨트롤러
1. 목록 조회 : 복수형을 사용한다. 
~~~java
books() ==> (o) 
bookList() ==> (x)
~~~
2. 상세 정보 : details를 접미사에 붙인다.
~~~java
bookDetails()
~~~
3. 등록, 수정, 삭제 : save, modify, delete를 접두사에 붙인다.
~~~java
saveBook()
modifyBook()
deleteBook()
~~~

### 서비스
1. 목록 조회 : find 접두사를 붙인다.
~~~java
findBook()
~~~
2. 등록, 수정, 삭제 : save, modify, delete접두사를 붙인다.
~~~java
saveBook()
modifyBook()
deleteBook()
~~~

## 프로젝트 구조
클래스가 많은 것으로 예상되지 않기 때문에 기본적인 계층형 구조를 따른다. 
~~~xml
|---main
   |----java
   |      |----vanille
   |             |----vocabe
   |                     |----entity
   |                     |----global
   |                     |        |----common
   |                     |        |----config
   |                     |        |----error
   |                     |----payload
   |                     |----repository
   |                     |----service
   |
   |----resources
~~~

