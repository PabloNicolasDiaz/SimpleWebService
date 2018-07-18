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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationFault_Exception;
import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationRequest;
import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationResponse;
import ar.com.nicolas.ws.simpleWebService.endpoints.SimpleOperationEndpoint;

@Tag("unit")
class SimpleOperationEndpointTest {

	private SimpleOperationEndpoint endpoint;

	@BeforeEach
	public void setup() {
		this.endpoint = new SimpleOperationEndpoint();
	}

	@Test
	void testGivenASimpleRequestWhenIsCalledWithPepeThenReturnsASimpleResponse() throws SimpleOperationFault_Exception {
		SimpleOperationRequest simpleRequest = new SimpleOperationRequest("Pepe");
		SimpleOperationResponse simpleResponse = endpoint.simpleOperation(simpleRequest);
		assertNotNull(simpleResponse);
		assertEquals(simpleResponse.getOut(), "Hello Pepe");
	}

	@Test

	void testGivenASimpleRequestWhenIsCalledEmptyThenReturnsASimpleResponse() throws SimpleOperationFault_Exception {
		SimpleOperationRequest simpleRequest = new SimpleOperationRequest();
		assertThrows(SimpleOperationFault_Exception.class, () -> {
			endpoint.simpleOperation(simpleRequest);
		});
	}

}
