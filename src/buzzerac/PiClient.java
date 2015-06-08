package buzzer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class PiClient implements Runnable {

	private ObjectInputStream inputStream;		// to read from the socket
	private ObjectOutputStream outputStream;		// to write on the socket
	private Socket socket;

	private String servername = "192.168.0.167";
//	private String servername = "localhost";
	
	private Queue<String> msg;
	
	private int port;

	public PiClient(int port) {
		// which calls the common constructor with the GUI set to null
		setPort(port);
		setMsg(new LinkedList<String>());
	}


	public void run() {
		System.out.println("Starte Thread");
		try {
			socket = new Socket(getServername(), getPort());
		} 
		catch(Exception ec) {
			System.out.println("Error connectiong to server:" + ec);
		}
		
		String text = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		System.out.println(text);
	
		/* Creating both Data Stream */
		try
		{
			inputStream  = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
		}

		while(true){
			if(!getMsg().isEmpty()){
				try {
					System.out.println("Sende " + getMsg().peek());
					getOutputStream().writeObject(getMsg().poll());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	public void disconnect() {
		try { 
			if(inputStream != null) inputStream.close();
		}
		catch(Exception e) {} // not much else I can do
		try {
			if(outputStream != null) outputStream.close();
		}
		catch(Exception e) {} // not much else I can do
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} // not much else I can do
					
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}


	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}


	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}


	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}


	public synchronized Queue<String> getMsg() {
		return msg;
	}


	public void setMsg(Queue<String> msg) {
		this.msg = msg;
	}


	public String getServername() {
		return servername;
	}


	public void setServername(String servername) {
		this.servername = servername;
	}

}