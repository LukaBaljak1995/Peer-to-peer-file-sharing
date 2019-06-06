
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class SendFileNit extends Thread {

	static Socket soketZaKom = null;
	String putanja = null;
	static BufferedReader ulazniTokOdKlijenta = null;
	static PrintStream izlazniTokKaKlijentu = null;

	public SendFileNit(Socket soket, String putanja) {
		soketZaKom = soket;
		this.putanja = putanja;
	}

	@Override
	public void run() {
		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());

			File f = new File(putanja);
			// od fajla f pravi se niz bajtova (niz je velicine fajla)
			byte[] nizBajtova = new byte[(int) f.length()];
			izlazniTokKaKlijentu.println((int) f.length());
			izlazniTokKaKlijentu.println(putanja);
			// treba citac fajla, da bi se fajl ubacio u niz bajtova
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			// preko tog citaca(bis) se u nizBajtova ubacuje to sto se cita iz
			// fajla, od 0. elementa niza pa sve do kraja
			bis.read(nizBajtova, 0, nizBajtova.length);
			//taj niz bajtova, odnosno FAJL se salje kroz soket
			izlazniTokKaKlijentu.write(nizBajtova, 0, nizBajtova.length);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.run();
	}

}

