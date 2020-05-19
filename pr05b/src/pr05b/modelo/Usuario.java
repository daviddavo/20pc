package pr05b.modelo;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Guarda información para un usaurio registrado en el sistema.
 * Tendrá al menos los siguientes atributos: identificador de
 * usuario, dirección ip y lista de información compartida.
 * El servidor almacenará información sobre todos los usuarios
 * almacenados en el sitema.
 */
public class Usuario {
	String _userName;
	InetAddress _addr;
	ArrayList<InfoFichero> _info;
}
