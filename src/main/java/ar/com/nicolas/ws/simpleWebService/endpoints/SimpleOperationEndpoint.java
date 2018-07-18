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
package ar.com.nicolas.ws.simpleWebService.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

import com.google.common.base.Strings;

import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationFault;
import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationFault_Exception;
import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationRequest;
import ar.com.nicolas.ws.generated.simpleWebService.SimpleOperationResponse;

@Endpoint
public class SimpleOperationEndpoint {

	@SoapAction(value = "http://www.nicolas.com.ar/simpleWebService/simpleOperation")
	@ResponsePayload
	public SimpleOperationResponse simpleOperation(@RequestPayload SimpleOperationRequest parameters)
			throws SimpleOperationFault_Exception

	{
		String name = parameters.getIn();
		if (Strings.isNullOrEmpty(name))
			throw new SimpleOperationFault_Exception("In Parameter can not be null",
					new SimpleOperationFault("EmptyInCode", "In Parameter can not be null"));
		return new SimpleOperationResponse("Hello " + name);
	}
}
