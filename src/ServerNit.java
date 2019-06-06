
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class ServerNit extends Thread {

	BufferedReader ulazniTokOdKlijenta = null;
	PrintStream izlazniTokKaKlijentu = null;
	Socket soketZaKom = null;
	ServerNit klijenti[] = null;
	PrintWriter out = null;
	static LinkedList<Fajl> f = new LinkedList<>();
	static int i = 0;
	int port = 9000;
	String nick;
	String ipAdresa;

	public ServerNit(Socket soket, ServerNit[] klijenti) {
		soketZaKom = soket;
		this.klijenti = klijenti;

	}

	int postojiFajl(String checksum) {
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).getChecksum().equals(checksum))
				return i;
		}
		return -1;
	}

	@Override
	public void run() {

		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());

			izlazniTokKaKlijentu.println("Zdravo! Unesite svoj nick :) (format nick-a mora da bude #nick)");
			String odgovor=ulazniTokOdKlijenta.readLine();
			this.nick =  odgovor.split("#")[1];
			this.ipAdresa =  odgovor.split("#")[2];
			izlazniTokKaKlijentu.println("Dobrodosao, " + nick);
			izlazniTokKaKlijentu.println("Da li zelite da podelite fajl?");
			String daLiZeliDaPodeliFajl = "";

			// ***potvrda da se fajl deli
			do {
				daLiZeliDaPodeliFajl = ulazniTokOdKlijenta.readLine().toUpperCase();
				if (!(daLiZeliDaPodeliFajl.equals("DA") || daLiZeliDaPodeliFajl.equals("NE"))) {
					izlazniTokKaKlijentu.println("Unesite DA ili NE");
				} else
					break;

			} while (!(daLiZeliDaPodeliFajl.equals("DA") || daLiZeliDaPodeliFajl.equals("NE")));

			if ((daLiZeliDaPodeliFajl.equals("DA"))) {
				izlazniTokKaKlijentu.println("Unesite putanje fajlova. Za kraj unesite gotovo.");
				do {
					String putanja = ulazniTokOdKlijenta.readLine();
					if (putanja.equalsIgnoreCase("gotovo")) {

						izlazniTokKaKlijentu.println("Zavrsili ste sa seedovanjem.");
						break;
					}
					String imeFajla;
					imeFajla = putanja.split("/")[putanja.split("/").length - 1];
					String checksum = MD5Checksum.getMD5Checksum(new File(putanja));
					

					if (checksum != null) {
						int indikator = postojiFajl(checksum);
						if (indikator == -1) {
							Fajl fajl = new Fajl(imeFajla, checksum, (port + (i++)),ipAdresa);
							f.add(fajl);
							// ovo salje server klijentu i onda on zna da treba
							// da
							// otvori soket za osluskivanje konekcije.
							// U klijentu ce biti reguliran port, bice poslat
							// ovde,
							// i azurirace se list afajlova
							izlazniTokKaKlijentu.println(putanja + ">" + fajl.getPortovi().getLast());
						} else {
							Fajl fajl = f.get(indikator);
							fajl.getPortovi().add(port + (i++));
							izlazniTokKaKlijentu.println(putanja + ">" + fajl.getPortovi().getLast());
						}
						// izlazniTokKaKlijentu.println(putanja + ">" +
						// fajl.getPortovi().getLast());
						izlazniTokKaKlijentu.println("<" + nick + ">, poceli ste da seedujete novi fajl " + imeFajla);
					} else {
						izlazniTokKaKlijentu.println("Fajl na toj putanji ne postoji.");

					}
				} while (true);
			} else {
				do {
					izlazniTokKaKlijentu.println("Dostupni fajlovi za download:");
					int rb = 1;
					for (int i = 0; i < f.size(); i++) {
						izlazniTokKaKlijentu.println((rb++) + " " + f.get(i).naziv);
					}
					izlazniTokKaKlijentu.println("Odaberite jedan redni broj.");
					// on bira redni broj, mi ga ucitavamo, i saljemo PORT na
					// kom se
					// osluskuje ta konekcija
					rb = Integer.parseInt(ulazniTokOdKlijenta.readLine());
					izlazniTokKaKlijentu.println("Skini#"+f.get(rb - 1).getPortovi().getFirst() + "#" + f.get(rb-1).getIpAdrese().getFirst());
				} while (true);
			}
		} catch (Exception e) {
			for(int i = 0; i < f.size();i++){
				for(int j = 0; j < f.get(i).getIpAdrese().size(); j++){
					if(f.get(i).getIpAdrese().get(j).equals(ipAdresa)) {
						f.get(i).getIpAdrese().remove(j);
						f.get(i).getPortovi().remove(j);
						}
					}
				}
			}

		}
	}
