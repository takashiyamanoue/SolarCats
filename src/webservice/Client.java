package webservice;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.Options;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

public class Client {
    public static void main(String [] args)
    {
        try {
            Options options = new Options(args);
            
            String endpointURL = options.getURL();
            String textToSend;
            
            args = options.getRemainingArgs();
            if ((args == null) || (args.length < 1)) {
                textToSend = "<nothing>";
            } else {
                textToSend = args[0];
            }
            
            Service  service = new Service();
            Call     call    = (Call) service.createCall();

            call.setTargetEndpointAddress( new java.net.URL(endpointURL) );
            call.setOperationName( new QName("http://example3.userguide.samples", "serviceMethod") );
            call.addParameter( "arg1", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING );

            String ret = (String) call.invoke( new Object[] { textToSend } );
            
            System.out.println("You typed : " + ret);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
