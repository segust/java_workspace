package _10_26;

import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadEchoServer {
	public static void main(String args[]) {
		try {
			int i = 1;
			@SuppressWarnings("resource")
			ServerSocket s = new ServerSocket(8189);
			while (true) {
				Socket incoming = s.accept();
				System.out.println("Spawning" + i);
				Runnable r = new ThreadedEchoHandler(incoming);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ThreadedEchoHandler implements Runnable {
	private Socket incoming;

	public ThreadedEchoHandler(Socket i) {
		incoming = i;
	}

	public void run() {
		try {
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				@SuppressWarnings("resource")
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);

				out.println("Hello!Enter BYE to exit.");

				boolean done = false;
				while (!done && in.hasNextLine()) {
					String line = in.nextLine();
					out.println("Echo:" + line);
					if (line.trim().equals("BYE"))
						done = true;
				}
			} finally {
				incoming.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}