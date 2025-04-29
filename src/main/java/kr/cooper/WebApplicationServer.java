package kr.cooper;

import java.io.File;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationServer {

	private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);

	public static void main(String[] args) throws Exception {
		final String webappDirLocation = "webapps/";
		final Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		logger.info("configure app with base dir : {}", new File("./" + webappDirLocation).getAbsolutePath());

		tomcat.start();
		tomcat.getServer().await();
	}
}
