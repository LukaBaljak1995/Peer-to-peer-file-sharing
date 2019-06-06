
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class Server {

	final static int brojKlijenata=50;	
	static ServerNit klijenti[] = new ServerNit[brojKlijenata];

		
		
		
		public static void main(String[] args) {

			int port = 9898;
			Socket klijentSoket = null;
			

			try {
				ServerSocket serverSoket = new ServerSocket(port);
				System.out.println("Server je pokrenut");
				
				
				
				while (true) {
					
					klijentSoket = serverSoket.accept();
					
							for (int i = 0; i < klijenti.length; i++) {
								if(klijenti[i]==null){
									klijenti[i]=new ServerNit(klijentSoket, klijenti);
									klijenti[i].start();
									break;
								}							
							}				
				}			
				
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	
	
}

