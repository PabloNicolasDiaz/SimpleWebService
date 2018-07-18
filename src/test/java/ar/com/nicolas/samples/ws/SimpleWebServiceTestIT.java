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
package ar.com.nicolas.samples.ws;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.io.CharSource;
import com.predic8.schema.Import;
import com.predic8.soamodel.Difference;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;
import com.predic8.xml.util.ClasspathResolver;

import ar.com.nicolas.ws.simpleWebService.SimpleWebServiceApplication;

@Tag("acceptance")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SimpleWebServiceApplication.class)
public class SimpleWebServiceTestIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${server.xsdResourcePath}")
	private String xsdResourcePath;

	@Test
	public void greetingShouldReturnDefaultMessage(@Value("${server.wsdlResourcePath}") String wsdl) throws Exception {
		ClassPathResource wsdlResource = new ClassPathResource(wsdl);
		WSDLParser classpathParser = new WSDLParser();
		ClasspathResolver rr = new ClasspathResolver() {
			@Override
			public Object resolve(Object input, Object baseDir) {
				if (input instanceof Import) {
					return super.resolve("/wsdl/".concat(Import.class.cast(input).getSchemaLocation()), baseDir);
				}
				return super.resolve(input, baseDir);
			}
		};
		classpathParser.setResourceResolver(rr);
		Definitions d1 = classpathParser.parse(wsdlResource.getInputStream());
		String s = this.restTemplate.getForObject("http://localhost:" + port + "/simpleWebService.wsdl", String.class);
		WSDLParser externalParser = new WSDLParser();
		Definitions d2 = externalParser
				.parse(new ReaderInputStream(CharSource.wrap(s).openStream(), Charset.defaultCharset()));
		WsdlDiffGenerator diffGen = new WsdlDiffGenerator(d1, d2);
		List<Difference> lst = diffGen.compare();
		assertThat(lst.size()).isEqualTo(1);	
	}
}