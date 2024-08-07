package ar.edu.unq.po2.tpfinal.app;
import java.time.LocalTime;

import ar.edu.unq.po2.tpfinal.app.estadoDesplazamiento.EstadoDesplazamiento;
import ar.edu.unq.po2.tpfinal.app.estadoEstacionamiento.EstadoEstacionamiento;
import ar.edu.unq.po2.tpfinal.app.modos.EstrategiaModo;
import ar.edu.unq.po2.tpfinal.sem.SEM;

public class AppUsuario implements MovementSensor{
	
	private String patente;
	private SEM sem;
	private Celular cel;
	private EstadoDesplazamiento desplazamiento;
	private EstadoEstacionamiento estadoEstacionamiento;
	private EstrategiaModo modo;
	private LocalTime horaActual;
	
	public double consultarSaldo() {
		return this.cel.getCredito();
	} 
	  
	public void inicioEstacionamiento() {
		this.cel.alerta(this.estadoEstacionamiento.iniciarEstacionamiento(this,this.sem,this.cel,this.patente,this.horaActual));
	}
	
	public void finEstacionamiento() {
		this.cel.alerta(this.estadoEstacionamiento.finalizarEstacionamiento(this.sem,this.cel,this));
	}
	
	
	public AppUsuario(String patente, SEM sem, Celular cel, EstadoEstacionamiento estadoEstacionamiento,
			EstrategiaModo modo) {
		this.patente = patente;
		this.sem = sem;
		this.cel = cel;
		this.estadoEstacionamiento = estadoEstacionamiento;
		this.modo = modo;
	}    

	public void setEstadoEstacionamiento(EstadoEstacionamiento estado) {
		this.estadoEstacionamiento = estado; 
	}
	
	public String getPatente() {
		return this.patente; 
	}   
	public void ahoraEstasCaminando() {
		this.estadoEstacionamiento.ahoraEstasCaminando(this, this.cel);
	}
	public void ahoraEstasManejando() {
		this.estadoEstacionamiento.ahoraEstasManejando(this, this.cel);
	}

	public void setEstadoMovimiento(EstadoDesplazamiento estadoMovimiento) {
		this.desplazamiento = estadoMovimiento;
	}
	
	public void setModo(EstrategiaModo modo) {
		this.modo = modo;
	}
	
	public EstrategiaModo getModo() {
		return this.modo;
	}
	
	public void setHoraActual(LocalTime hora) {
		this.horaActual = hora;
	}

	public LocalTime getHoraActual() {
		return horaActual;
	}

	public EstadoEstacionamiento getEstado() {
		return this.estadoEstacionamiento;
	}
	
	public EstadoDesplazamiento getEstadoDesplazamiento() {
		return this.desplazamiento; 

	}

	@Override
	public void driving() {
		this.ahoraEstasManejando();	
	}

	@Override
	public void walking() {
		this.ahoraEstasCaminando();
	}
	
}