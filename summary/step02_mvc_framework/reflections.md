# Reflection

## (1) Reflections ??

1. 힙 영역에 로드되어 있는 클래스 타입의 객체를 통해 필드/메서드/생성자를 접근 제어자와 상관없이 사용할 수 있도록 지원하는 API
2. 컴파일 시점이 아닌 **런타임 시점**에 동적으로 특정 클래스의 정보를 추출해낼 수 있는 프로그래밍 기법

## 2. 필드/메서드/생성자 + Class 조회 메서드

### (1) 필드/메서드/생성자 메서드

```java
@Test
void showClass() {
    final Class<User> clazz = User.class;

	// class: [kr.cooper.model.User]
    logger.debug("class: [{}]", clazz.getName());
    
	// user all declared fields: [[private java.lang.String kr.cooper.model.User.userId, private java.lang.String kr.cooper.model.User.name]]
    logger.debug("user all declared fields: [{}]",
        Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));

	// user all declared constructors: [[public kr.cooper.model.User(java.lang.String,java.lang.String)]]
    logger.debug("user all declared constructors: [{}]",
        Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));

	// user all declared methods: [[public java.lang.String kr.cooper.model.User.getUserId(), public boolean kr.cooper.model.User.equals(java.lang.Object), public int kr.cooper.model.User.hashCode(), public java.lang.String kr.cooper.model.User.getName()]]
    logger.debug("user all declared methods: [{}]",
        Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
}
```

### (2) Class 조회 메서드

```java
@Test
void load() throws ClassNotFoundException {
    // 1.
    final Class<User> userClazz01 = User.class;

    // 2.
    final User user = new User("cooperId", "cooper");
    final Class<? extends User> userClazz02 = user.getClass();

    // 3.
    final Class<?> userClazz03 = Class.forName("kr.cooper.model.User");

    logger.debug("userClazz01: [{}]", userClazz01);
    logger.debug("userClazz02: [{}]", userClazz02);
    logger.debug("userClazz03: [{}]", userClazz03);

    assertEquals(userClazz01, userClazz02);
    assertEquals(userClazz01, userClazz03);
    assertEquals(userClazz02, userClazz03);
}
```
