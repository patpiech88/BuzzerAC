package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/** One instance of this thread will run for each client */
class PcClient {

	private int port;
	private boolean keepGoing;
	private PcClientThread pcClientThread;
	

	public PcClient(int port) {
		setPort(port);
	}
	
	public void start() {
		setKeepGoing(true);
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(keepGoing) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!keepGoing)
					break;
				setPcClientThread(new PcClientThread(socket));  // make a thread of it								// save it in the ArrayList
				getPcClientThread().start();
				
			}
			// I was asked to stop
			try {
					serverSocket.close();
					getPcClientThread().sInput.close();
					getPcClientThread().sOutput.close();
					getPcClientThread().socket.close();
				}
					catch(IOException ioE) {
						// not much I can do
					}
	
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}		
    /*
     * For the GUI to stop the server
     */
	protected void stop() {
		keepGoing = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	private void display(String msg) {
		System.out.println(msg);
	}

	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public boolean isKeepGoing() {
		return keepGoing;
	}


	public void setKeepGoing(boolean keepGoing) {
		this.keepGoing = keepGoing;
	}


	public PcClientThread getPcClientThread() {
		return pcClientThread;
	}


	public void setPcClientThread(PcClientThread pcClientThread) {
		this.pcClientThread = pcClientThread;
	}
}