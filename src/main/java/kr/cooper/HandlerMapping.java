package kr.cooper;

public interface HandlerMapping {
    Object findHandler(HandlerKey handlerKey);
}
