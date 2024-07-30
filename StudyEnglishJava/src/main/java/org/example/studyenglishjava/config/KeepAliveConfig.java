package org.example.studyenglishjava.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeepAliveConfig implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        if (factory instanceof TomcatServletWebServerFactory) {
            ((TomcatServletWebServerFactory) factory)
                    .addConnectorCustomizers(new TomcatConnectorCustomizer() {
                        @Override
                        public void customize(Connector connector) {
                            if (connector != null) {
                                Http11NioProtocol protocolHandler = (Http11NioProtocol) connector.getProtocolHandler();
                                // Keep-alive timeout setting
                                System.out.println("Reset protocol keep-alive timeout");
                                protocolHandler.setKeepAliveTimeout(60 * 5 * 1000);
                            }
                        }
                    });
        }
    }
}
