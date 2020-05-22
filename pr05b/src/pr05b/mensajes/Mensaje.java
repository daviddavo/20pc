package pr05b.mensajes;

import java.io.Serializable;

/**
 * Sirve como raíz de la jerarquía de mensajes que deberemos
 * diseñar. Tiene como atributos al tipo, origen y destino del
 * mensaje en cuestión
 */
public abstract class Mensaje implements Serializable {
	private static final long serialVersionUID = -5415242270020452110L;

	public enum Tipo {
		// Servidor
		MENSAJE_CONFIRMACION_CONEXION,
		MENSAJE_CONFIRMACION_LISTA_USUARIOS,
		MENSAJE_EMITIR_FICHERO,
		MENSAJE_PREPARADO_SERVIDORCLIENTE,
		MENSAJE_CONFIRMACION_CERRAR_CONEXION,
		// Cliente
		MENSAJE_CONEXION,
		MENSAJE_LISTA_USUARIOS,
		MENSAJE_CERRAR_CONEXION,
		MENSAJE_PEDIR_FICHERO,
		MENSAJE_PREPARADO_CLIENTESERVIDOR,
		// Añadidos por mi
		MENSAJE_PUT_INFO_FICHERO,
		MENSAJE_ACK_INFO_FICHERO,
	}
	
	private String _destino = null;
	private String _origen = null;
	
	public Mensaje(String destino, String origen) {
		_destino = destino;
		_origen = origen;
	}
	
	public abstract Tipo getTipo();
	
	public String getOrigen() {
		return _origen;
	}
	
	public String getDestino() {
		return _destino;
	}
}
