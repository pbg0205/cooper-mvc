package kr.cooper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import kr.cooper.annotation.RequestMapping;
import kr.cooper.controller.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping{
    private final Object[] basePackage;

    private final Map<HandlerKey, AnnotationHandler> handlers = new HashMap<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> clazzesWithControllerAnnotation = reflections.getTypesAnnotatedWith(kr.cooper.annotation.Controller.class, true);

        clazzesWithControllerAnnotation.forEach(clazz ->

                Arrays.stream(clazz.getDeclaredMethods()).forEach(declaredMethod -> {
                    RequestMapping requestMappingAnnotation = declaredMethod.getDeclaredAnnotation(RequestMapping.class);

                    Arrays.stream(getRequestMethods(requestMappingAnnotation))
                            .forEach(requestMethod -> handlers.put(
                                    new HandlerKey(requestMappingAnnotation.value(), requestMethod), new AnnotationHandler(clazz, declaredMethod)
                            ));

                })
        );
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMappingAnnotation) {
        return requestMappingAnnotation.method();
    }

    @Override
    public Object findHandler(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }
}
