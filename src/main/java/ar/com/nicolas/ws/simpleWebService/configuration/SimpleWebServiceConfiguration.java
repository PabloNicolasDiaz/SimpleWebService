/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2018 Pablo Nicolas Diaz Bilotto <pablonicolas.diaz@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package ar.com.nicolas.ws.simpleWebService.configuration;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.transport.http.WsdlDefinitionHandlerAdapter;
import org.springframework.ws.wsdl.WsdlDefinition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.xml.sax.SAXException;

@EnableWs
@Configuration
public class SimpleWebServiceConfiguration extends WsConfigurerAdapter implements BeanFactoryAware {

	private static PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	private ConfigurableBeanFactory configurableBeanFactory;

	@Value("${server.xsdResourcePath}")
	private String xsdResourcePath;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
	}

	@PostConstruct
	public void onPostConstruct() throws ParserConfigurationException, IOException, SAXException {
		String path = "classpath:" + xsdResourcePath + "*.xsd";
		for (Resource r : resolver.getResources(path)) {
			SimpleXsdSchema s = new SimpleXsdSchema(r);
			s.afterPropertiesSet();
			configurableBeanFactory.registerSingleton(FilenameUtils.removeExtension(r.getFilename()), s);
		}
	}

	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
			ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		servlet.setTransformSchemaLocations(true);
		return new ServletRegistrationBean<>(servlet, "/*");
	}

	@Bean(name = "simpleWebService")
	public WsdlDefinition simpleWebServiceWsdl11Definition(@Value("${server.wsdlResourcePath}") String wsdl) {
		SimpleWsdl11Definition x = new SimpleWsdl11Definition(new ClassPathResource(wsdl));
		return x;
	}

	@Bean
	public WsdlDefinitionHandlerAdapter wsdlDefinitionHandlerAdapter() {
		return new WsdlDefinitionHandlerAdapter() {
			@Override
			protected String transformLocation(String location, HttpServletRequest request) {
				if (FilenameUtils.getExtension(location).equals("xsd"))
					return super.transformLocation("/" + FilenameUtils.getName(location), request);
				return super.transformLocation(location, request);
			}
		};
	}
}
