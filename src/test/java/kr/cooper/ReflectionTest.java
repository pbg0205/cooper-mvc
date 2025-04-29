package kr.cooper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.cooper.annotation.Controller;
import kr.cooper.annotation.Service;
import kr.cooper.model.User;

public class ReflectionTest {

	private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

	@Test
	void getTypesAnnotatedWith() {
		final Set<Class<?>> beans = getTypesAnnotatedWith(List.of(Controller.class, Service.class));

		logger.debug("beans: [{}]", beans);
		assertEquals(3, beans.size());
	}

	private Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotations) {
		final Reflections reflections = new Reflections("kr.cooper");

		final Set<Class<?>> beans = new HashSet<>();
		annotations.forEach(annotation -> beans.addAll(reflections.getTypesAnnotatedWith(annotation)));

		return beans;
	}

	@Test
	void showClass() {
		final Class<User> clazz = User.class;

		logger.debug("class: [{}]", clazz.getName());

		logger.debug("user all declared fields: [{}]",
			Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));

		logger.debug("user all declared constructors: [{}]",
			Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));

		logger.debug("user all declared methods: [{}]",
			Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
	}

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
}
