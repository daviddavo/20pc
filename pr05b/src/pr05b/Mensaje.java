package pr05b;

/**
 * Sirve como raíz de la jerarquía de mensajes que deberemos
 * diseñar. Tiene como atributos al tipo, origen y destino del
 * mensaje en cuestión
 */
public abstract class Mensaje {
	public abstract int getTipo();
	public abstract String getOrigen();
	public abstract String getDestino();
}
