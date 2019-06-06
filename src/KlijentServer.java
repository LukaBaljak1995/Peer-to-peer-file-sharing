
import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class KlijentServer extends Thread {

	String putanja;
	BufferedReader ulazniTokOdKlijenta = null;
	PrintStream izlazniTokKaKlijentu = null;
	int port;
	public KlijentServer(String putanja, int port) {
		this.putanja = putanja;
		this.port=port;
	}

	

	@Override
	public void run() {
		
		Socket klijentSoket = null;

		try {
			ServerSocket serverSoket = new ServerSocket(port);

			while (true) {

				klijentSoket = serverSoket.accept();
				
				SendFileNit sfn = new SendFileNit(klijentSoket, putanja);
				sfn.start();
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
