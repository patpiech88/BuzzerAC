//Copyright P.W.

package buzzer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class BuzzerController {

	private PiClient piClient;
	
	private GpioController gpioController;

	private GpioPinDigitalInput controllerOne;
	private GpioPinDigitalInput controllerTwo;
	private GpioPinDigitalInput controllerThree;
	private GpioPinDigitalInput controllerFour;
	
	private int port = 1234;
	
	public BuzzerController(){
		
		final GpioController gpio = GpioFactory.getInstance();
		this.gpioController = gpio;
		
		controllerOne = initializeController(RaspiPin.GPIO_00);
		controllerTwo = initializeController(RaspiPin.GPIO_02);
		controllerThree = initializeController(RaspiPin.GPIO_04);
		
//		controllerFour = initializeController(RaspiPin.GPIO_05);
			
	}

	private void initializeConnection(){
		setClient(new PiClient(getPort()));
	}

	private void sendClickByControllerToServer(Pin pin){
		
		getClient().getMsg().add(pin.getName());
		
	}
	
	private GpioPinDigitalInput initializeController(Pin pin) {
	
		GpioPinDigitalInput gpioPin =  this.gpioController.provisionDigitalInputPin(pin,PinPullResistance.PULL_DOWN);
	
		gpioPin.addListener(new GpioPinListenerDigital() {
			
			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent ev) {
				System.out.println(ev.getPin().getName());
				if(ev.getState().isHigh()){
					sendClickByControllerToServer(ev.getPin().getPin());
				}
				
			}
		});
		
		return gpioPin;		
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}

	public PiClient getClient() {
		return piClient;
	}

	public void setClient(PiClient client) {
		this.piClient = client;
	}
	
	
	public static void main(String args[]){
		
		System.out.println("starte BuzzerController");		
		BuzzerController a = new BuzzerController();
		
		System.out.println("initialize Connection");
		a.initializeConnection();
		Thread t = new Thread(a.getClient());
		t.start();
		
		System.out.println("Test");
		
		a.getClient().getMsg().add("Hello World");
		
		while(true){
			
		}
		
//		for(int i = 0; i<100; i++){
//			a.getClient().getMsg().add(String.valueOf(i));
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				a.getClient().disconnect();
//				e.printStackTrace();
//			}
//		}
		
	}
	
}
