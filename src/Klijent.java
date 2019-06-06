

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Klijent implements Runnable {

	static Socket soketZaKomunikaciju = null;

	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader ulazniTokOdServera = null;
	static BufferedReader ulazKonzola = null;
	static boolean kraj = false;
	String folder;
	static String ipAdresa;
	public static void main(String[] args) {
		try {
			soketZaKomunikaciju = new Socket("localhost", 9898);

			ulazKonzola = new BufferedReader(new InputStreamReader(System.in));

			izlazniTokKaServeru = new PrintStream(soketZaKomunikaciju.getOutputStream());

			ulazniTokOdServera = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));

			new Thread(new Klijent()).start();

			while (!kraj) {
				String recenica= ulazKonzola.readLine();
				if(recenica.startsWith("#")){
					ipAdresa = ((InetSocketAddress) soketZaKomunikaciju.getLocalSocketAddress())
							.getHostString();
					izlazniTokKaServeru.println(recenica +"#"+ ipAdresa);
				}
				else izlazniTokKaServeru.println(recenica);
				
				
			}
			soketZaKomunikaciju.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean tryParseInt(String value) {  
	     try {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      }  
	}

	
	@Override
	public void run() {
		String linijaOdServera;

		try {
			while ((linijaOdServera = ulazniTokOdServera.readLine()) != null) {
				boolean brojje=tryParseInt(linijaOdServera);
				if(!(linijaOdServera.startsWith("/") || linijaOdServera.startsWith("Dobro")||brojje &&!linijaOdServera.startsWith(("Skini"))))
					System.out.println(linijaOdServera);
				
				if(linijaOdServera.startsWith("Dobrodosao, ")){
					//cim se uloguje, u folderu nas pravim folder po imenu klijenta
					System.out.println(linijaOdServera+"!");
					String niz[] = linijaOdServera.split(", ");
					folder="/Users/lukabaljak/Desktop/nas/"+niz[1];
					
					File f = new File(folder);
					f.mkdir();
				}
				if (linijaOdServera.startsWith("Dovidjenja")) {
					System.out.println(linijaOdServera);
					kraj = true;
					return;
				}
				if (linijaOdServera.startsWith("/")) {
					String niz[] = linijaOdServera.split(">");
					int port = Integer.parseInt(niz[1]);
					File f = new File(niz[0]);
					if (f.exists()) {
						
						(new KlijentServer(niz[0], port)).start();
					} else
						System.out.println("Ne postoji fajl na putanji" + niz[0]);
					// U konstruktor ce biti ubacen i broj porta, uvecan za novu
					// staticku promenljivu
					// Onda ce taj broj porta da bdue poslat serveru, da bi se
					// ubacio u listu Fajlova
					//(new KlijentServer(niz[0], port)).start();
				}
				//if (linijaOdServera.startsWith("Dostupni")) {
					// Ovo ce se izmeniti u broj porta. Parsiracu svaki put pa
					// ako bude broj, onda se otvara DN na taj port
					//(new DownloadNit()).start();
				//}

				if(linijaOdServera.startsWith("Skini")){
					int portPrimanje = Integer.parseInt(linijaOdServera.split("#")[1]);
					String adresaPrimanje = (linijaOdServera.split("#")[2]);
					(new DownloadNit(portPrimanje,folder,adresaPrimanje)).start();
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
