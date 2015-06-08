package game;

import java.io.IOException;
import java.io.ObjectOutputStream;

import buzzer.BuzzerController;

public class GameController {

	private PcClient pcClient;
		
	private int port = 1234;
	
	public GameController(){
				
	}
	
	private void initializeConnection(){
		
		setPcClient(new PcClient(getPort()));
		
	}

	public void startPcClient(){
		getPcClient().start();
	}
	
	public PcClient getPcClient() {
		return pcClient;
	}

	public void setPcClient(PcClient pcClient) {
		this.pcClient = pcClient;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public static void main(String args[]){
		
		GameController a = new GameController();
		a.initializeConnection();
		a.startPcClient();
		
	}
	
}
