package pr05b.mensajes;

/**
 * Sirve como raíz de la jerarquía de mensajes que deberemos
 * diseñar. Tiene como atributos al tipo, origen y destino del
 * mensaje en cuestión
 */
public abstract class Mensaje {
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
		MENSAJE_PREPARADO_CLIENTESERVIDOR
	}
	
	public abstract Tipo getTipo();
	public abstract String getOrigen();
	public abstract String getDestino();
}
