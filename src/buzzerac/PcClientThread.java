package game;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/** One instance of this thread will run for each client */
class PcClientThread extends Thread {

	Socket socket;
	ObjectInputStream sInput;
	ObjectOutputStream sOutput;


	public PcClientThread(Socket socket) throws ClassNotFoundException {
	
		this.socket = socket;

		System.out.println("Thread trying to create Object Input/Output Streams");
		try
		{
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sInput  = new ObjectInputStream(socket.getInputStream());

		}
		catch (IOException e) {
			System.out.println("Exception creating new Input/output Streams: " + e);
			return;
		}
	}

	public void run() {
		
		boolean keepGoing = true;
		while(keepGoing) {
			String msg;
			try {
				msg = (String) sInput.readObject();
				System.out.println(msg);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		close();
	}
	
	// try to close everything
	private void close() {
		// try to close the connection
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
		try {
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {};
		try {
			if(socket != null) socket.close();
		}
		catch (Exception e) {}
	}

	/*
	 * Write a String to the Client output stream
	 */
	private boolean writeMsg(String msg) {
		// if Client is still connected send the message to it
		if(!socket.isConnected()) {
			close();
			return false;
		}
		// write the message to the stream
		try {
			sOutput.writeObject(msg);
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e) {
			System.out.println("Error sending message");
		}
		return true;
	}
}