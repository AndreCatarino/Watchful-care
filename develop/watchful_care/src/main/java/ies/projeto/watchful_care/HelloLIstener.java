package ies.projeto.watchful_care;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(HelloBinding.class)
public class HelloLIstener {
	@Autowired
	private healthDataRepository healthdata;
	
	@Autowired
	private temperatureDataRepository temperaturedata;
	
	@Autowired
	private PushNotificationController control;
	
	@StreamListener(target = HelloBinding.GREETING)
    public void processHelloChannelGreeting(String msg) {
		String[] treatedMsg = msg.split("/");
		if(treatedMsg.length == 4) {
			healthData hd = new healthData(Integer.parseInt(treatedMsg[0]), Double.parseDouble(treatedMsg[1]), Double.parseDouble(treatedMsg[2]), Double.parseDouble(treatedMsg[3]));
			healthdata.save(hd);
			if(Integer.parseInt(treatedMsg[1]) > 128 ||  Integer.parseInt(treatedMsg[1]) < 75) {
				String body = "Paciente: "+Integer.parseInt(treatedMsg[0])+" está com pulsação anormal!";
				PushNotificationRequest alertMessage = new PushNotificationRequest("Alerta!!!", body, "common");
				control.sendNotification(alertMessage);
			}
		}
		else {
			temperatureData td = new temperatureData(Integer.parseInt(treatedMsg[0]), Float.parseFloat(treatedMsg[1]));
			temperaturedata.save(td);
			if(Integer.parseInt(treatedMsg[1]) > 38.0 ||  Integer.parseInt(treatedMsg[1]) < 36.0) {
				String body = "Paciente: "+Integer.parseInt(treatedMsg[0])+" está com temperatura anormal!";
				PushNotificationRequest alertMessage = new PushNotificationRequest("Alerta!!!", body, "common");
				control.sendNotification(alertMessage);
			}
		}
		
		
		
		System.out.println("Saved new data: "+msg);
	}
}
