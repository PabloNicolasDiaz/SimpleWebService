Feature: Hello endpoint

  Background: 
    * url demoBaseUrl

  Scenario: Hello
    Given request
      """
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sim="http://www.nicolas.com.ar/simpleWebService/simpleWebServiceTypes">
      <soapenv:Header/>
      <soapenv:Body>
        <sim:SimpleOperationRequest>
           <in>Nicolas</in>
        </sim:SimpleOperationRequest>
      </soapenv:Body>
      </soapenv:Envelope>
      """
    When soap action 'http://www.nicolas.com.ar/simpleWebService/simpleOperation'
    Then status 200
    And match /Envelope/Body/SimpleOperationResponse/out == 'Hello Nicolas'
