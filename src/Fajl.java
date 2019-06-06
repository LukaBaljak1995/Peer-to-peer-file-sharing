import java.util.LinkedList;
import java.util.List;

public class Fajl {

	String naziv;
	String checksum;
	LinkedList<Integer>portovi=new LinkedList<>();
	LinkedList<String> ipAdrese = new LinkedList<>();
	
	public Fajl() {
		// TODO Auto-generated constructor stub
	}

	public Fajl(String naziv, String checksum, int port, String ipAdresa) {	
		this.naziv = naziv;
		this.checksum = checksum;
		portovi.add(port);
		ipAdrese.add(ipAdresa);
	}

	public String getNaziv() {
		return naziv;
	}

	public String getChecksum() {
		return checksum;
	}

	public LinkedList<Integer> getPortovi() {
		return portovi;
	}

	
	public LinkedList<String> getIpAdrese(){
		return ipAdrese;
	}
	
	
	
}