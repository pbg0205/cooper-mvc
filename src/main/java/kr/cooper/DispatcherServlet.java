package kr.cooper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.cooper.controller.RequestMethod;
import kr.cooper.view.JspViewResolver;
import kr.cooper.view.ModelAndView;
import kr.cooper.view.View;
import kr.cooper.view.ViewResolver;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	private List<HandlerMapping> handlerMappings;
	private List<HandlerAdapter> handlerAdapters;
	private List<ViewResolver> viewResolvers;

	@Override
	public void init() throws ServletException {
		RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
		rmhm.init();

		AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("kr.cooper");
		ahm.initialize();

		handlerMappings = List.of(rmhm, ahm);
		handlerAdapters = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());

		viewResolvers = Collections.singletonList(new JspViewResolver());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String requestURI = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

		Object controller = handlerMappings.stream()
			.filter(hm -> hm.findHandler(new HandlerKey(requestURI, requestMethod)) != null)
			.map(hm -> hm.findHandler(new HandlerKey(requestURI, requestMethod)))
			.findFirst()
			.orElseThrow(() -> new ServletException("No handler for [" + requestMethod + ", " + requestURI + "]"));

		try {
			HandlerAdapter handlerAdapter = handlerAdapters.stream()
				.filter(ha -> ha.supports(controller))
				.findFirst()
				.orElseThrow(() -> new ServletException("No adapter for handler [" + controller + "]"));

			ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

			for (ViewResolver viewResolver : this.viewResolvers) {
				View view = viewResolver.resolveViewName(modelAndView.getViewName());
				view.render(modelAndView.getModel(), request, response);
			}
		} catch (Throwable e) {
			logger.error("exception occurred: [{}]", e.getMessage(), e);
			throw new ServletException(e);
		}
	}
}
