package kr.cooper.view;

import static kr.cooper.view.RedirectView.DEFAULT_REDIRECT_PREFIX;

public class JspViewResolver implements ViewResolver {
	@Override
	public View resolveViewName(String viewName) {
		if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			return new RedirectView(viewName);
		}
		return new JspView(viewName + ".jsp");
	}
}
