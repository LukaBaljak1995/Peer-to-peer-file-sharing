import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DownloadNit extends Thread {

	static Socket soketZaKomunikaciju;
	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader ulazniTokOdServera = null;
	int port;
	String folder;
	String ipAdresa;
	
	public DownloadNit(int port, String folder,String ipAdresa) {
		this.port = port;
		this.ipAdresa = ipAdresa;
		this.folder = folder;
	}

	@Override
	public void run() {

		try {
			soketZaKomunikaciju = new Socket(ipAdresa, port);
			ulazniTokOdServera = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			izlazniTokKaServeru = new PrintStream(soketZaKomunikaciju.getOutputStream());

			int velicina = Integer.parseInt(ulazniTokOdServera.readLine());
			String niz[] = ulazniTokOdServera.readLine().split("/");

			String naziv = niz[niz.length - 1];
			// 92117 je velicina fajla sa kojim sam isprobavao, treba da polje
			// ove klase bude i velicina fajla koji ce biti poslat!!!
			byte[] mybytearray = new byte[velicina];
			// Odabrao sam folder u kom ne postoji *fajl*, zbog metode ispod
			// *fajl* je ovo u putanji, a fajl je file xD
			File novi = new File(folder + "/" + naziv);
			// na putanji koja je specificirana se kreira nov fajl *fajl*
			novi.createNewFile();
			// output stream prema tom novom fajlu
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(novi));

			// trebao mi je pojedinacni input stream, jer kod konstrukotra samo
			// kazemo new InputStream, ne damo mu ime i ne mozemo kao
			// promenljivu da ga koristimo posle
			InputStream is = soketZaKomunikaciju.getInputStream();
			// bafer promenljiva u koju se ucitavaju bajtovi koji su procitani
			// iz fajla koji je poslat PUTEM SOKETA, znaci is CITA STA SOKET
			// POSALJE!
			int bytesRead = is.read(mybytearray, 0, mybytearray.length);
			// inkrementator koji nam treba za petlju, koji obelezava gde smo
			// stali u citanju fajla
			int current = bytesRead;

			// citanje fajla: iz soketa upisujemo u mybytearray, na poziciji
			// CURRENT, do pozicije velicina-current, tj popunjavamo fajl
			// bajtovima koji su procitani. Na oracle helpu pise da read
			// odredjeni broj bajtova procita, ne pise koliko, zato treba
			// current xD
			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (current < velicina);
			// citamo do 922117 jer je to velicina mog fajla. Predlog: pocetak
			// komunikacije preko soketa da bude razmena velicine fajla, da bi
			// DownloadNit mogla da stvori niz bajtova duzine fajla

			// u fajl specificiran za BufferedOutput stream se upisuje ceo
			// sadrzaj niza bajtova
			bos.write(mybytearray, 0, current);
			// ovo je lep ispis xD
			System.out.println("Fajl " + naziv + " je preuzet (" + current + " bajtova)");

			bos.flush();
			bos.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.run();
	}

}