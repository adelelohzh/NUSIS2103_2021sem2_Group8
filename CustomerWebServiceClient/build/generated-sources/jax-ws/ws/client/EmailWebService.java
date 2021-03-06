
package ws.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "EmailWebService", targetNamespace = "http://ws.session.ejb/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface EmailWebService {


    /**
     * 
     * @param password
     * @param appointmentEntity
     * @param toEmailAddress
     * @param fromEmailAddress
     * @param email
     * @return
     *     returns java.lang.Boolean
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "emailCheckoutNotificationSync", targetNamespace = "http://ws.session.ejb/", className = "ws.client.EmailCheckoutNotificationSync")
    @ResponseWrapper(localName = "emailCheckoutNotificationSyncResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.EmailCheckoutNotificationSyncResponse")
    @Action(input = "http://ws.session.ejb/EmailWebService/emailCheckoutNotificationSyncRequest", output = "http://ws.session.ejb/EmailWebService/emailCheckoutNotificationSyncResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/EmailWebService/emailCheckoutNotificationSync/Fault/InvalidLoginCredentialException")
    })
    public Boolean emailCheckoutNotificationSync(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "appointmentEntity", targetNamespace = "")
        AppointmentEntity appointmentEntity,
        @WebParam(name = "fromEmailAddress", targetNamespace = "")
        String fromEmailAddress,
        @WebParam(name = "toEmailAddress", targetNamespace = "")
        String toEmailAddress)
        throws InvalidLoginCredentialException_Exception
    ;

}
