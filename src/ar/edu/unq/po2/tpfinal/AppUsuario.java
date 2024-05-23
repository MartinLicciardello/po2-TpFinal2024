package ar.edu.unq.po2.tpfinal;
import java.time.LocalTime;

public class AppUsuario {
	
	private String patente;
	private SEM sem;
	private Celular cel;
	private EstadoDesplazamiento desplazamiento;
	private EstadoEstacionamiento estado;
	private EstrategiaModo modo;
	private LocalTime horaActual;
	
	public double consultarSaldo() {
		return this.cel.getCredito();
	}
	
	public void inicioEstacionamiento() {
		this.cel.alerta(this.estado.iniciarEstacionamiento(this,this.sem,this.cel,this.patente,this.horaActual));
	}
	
	public void finEstacionamiento() {
		this.cel.alerta(this.estado.finalizarEstacionamiento(this.sem,this.cel,this));
	}

	public void alertaInicioEstacionamiento() {

	}
	
	public void alertaFinEstacionamiento() {
		
	}

	protected void setEstadoEstacionamiento(EstadoEstacionamiento estado) {
		this.estado = estado; 
	}
	
	public String getPatente() {
		return this.patente;
	}
}