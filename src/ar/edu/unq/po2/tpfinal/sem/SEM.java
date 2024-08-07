package ar.edu.unq.po2.tpfinal.sem;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import ar.edu.unq.po2.tpfinal.app.AppUsuario;
import ar.edu.unq.po2.tpfinal.app.Celular;
import ar.edu.unq.po2.tpfinal.compra.Compra;
import ar.edu.unq.po2.tpfinal.inspector.Infraccion;
import ar.edu.unq.po2.tpfinal.inspector.Inspector;
import ar.edu.unq.po2.tpfinal.sem.estacionamiento.Estacionamiento;
import ar.edu.unq.po2.tpfinal.sem.estacionamiento.EstacionamientoPorApp;

public class SEM {
	
	private double precioXHora;
	private List <Infraccion> infracciones;
	private List <ZonaSEM> zonas;
	private List <Compra> compras;
	private List <Estacionamiento> estacionamientos;
	private List <Entidad> entidades;
	private List <AppUsuario> usuarios;
	private LocalTime horaActual; 
	
	public SEM(double precioXHora, List<Infraccion> infracciones, List<ZonaSEM> zonas, List<Compra> compras,
			List<Estacionamiento> estacionamientos, List<Entidad> entidades, List<AppUsuario> usuarios,
			LocalTime horaActual) {
		this.precioXHora = precioXHora;
		this.infracciones = infracciones;
		this.zonas = zonas;
		this.compras = compras;
		this.estacionamientos = estacionamientos;
		this.entidades = entidades;
		this.usuarios = usuarios;
		this.horaActual = horaActual;
	}

	public String inicioEstacionamiento(Celular cel, String patente, LocalTime horaActual) {
			
		String msg = "No se puede estacionar en este horario.";
		if (this.esHorarioValido(horaActual)){
			LocalTime horaFin = this.calcularHoraFinEstacionamiento(cel);
			Estacionamiento estacionamiento = new EstacionamientoPorApp(patente,horaFin,horaActual,cel,precioXHora);
			estacionamientos.add(estacionamiento);
			this.notificarEntidadesInicioDeEstacionamiento(estacionamiento);
			msg = "Hora de Inicio del Estacionamiento:" + String.valueOf(horaActual) + "Hora maxima de Fin del Estacionamiento:" + String.valueOf(horaFin);
		} 
		return msg;
	}
	
	public String finEstacionamiento(Celular cel, AppUsuario usuario) {
		String patente = usuario.getPatente();
		Estacionamiento estacionamiento = this.estacionamientos.stream().filter(e -> e.getPatente() == patente).findFirst().get();
		estacionamientos.remove(estacionamiento);
		estacionamiento.setHoraFin(horaActual);
		this.debitarCredito(cel,this.precioXHora,estacionamiento);
		String msg = this.enviarMensaje(estacionamiento);
		this.notificarEntidadesFinDeEstacionamiento(estacionamiento);
		return msg;
		
	}
	
	public LocalTime calcularHoraFinEstacionamiento(Celular cel) {
		
		int maximoDeHorasPagables = (int) Math.round (cel.getCredito() / this.precioXHora);
		LocalTime horaFINAL;
		LocalTime horaFin = this.horaActual.plusHours(maximoDeHorasPagables);
		LocalTime horaFinSEM = LocalTime.of(20, 0);
		if (horaFin.isBefore(horaFinSEM)) {
			horaFINAL = horaFin;
		}else {
			horaFINAL = horaFinSEM;
		}
		
		return horaFINAL;
		
	}
	
	private String enviarMensaje(Estacionamiento estacionamiento) {
		
		return  "Hora de Inicio del Estacionamiento:" + String.valueOf(estacionamiento.getHoraInicio()) + ". " + 
				"Hora Fin del Estacionamiento:" + String.valueOf(estacionamiento.getHoraFin()) +		  ". " + 
				"Duracion del Estacionamiento:" + String.valueOf(estacionamiento.duracion())   + 		  ". " + 
				"El costo fue de:" + String.valueOf(estacionamiento.getCostoEstacionamiento(this.precioXHora));
	}

	private void notificarEntidadesFinDeEstacionamiento(Estacionamiento estacionamiento) {
		this.entidades.forEach(entidad -> entidad.notificarFinEstacionamiento(estacionamiento));
	}
	
	private void notificarEntidadesInicioDeEstacionamiento(Estacionamiento estacionamiento) {
		this.entidades.forEach(entidad -> entidad.notificarInicioEstacionamiento(estacionamiento));
	}

	private void debitarCredito(Celular cel, double precioXHora2, Estacionamiento estacionamiento) {
		cel.debitarCredito(estacionamiento.getCostoEstacionamiento(precioXHora2));
	}

	public void finalizarTodosLosEstacionamientos(){
		if (!esHorarioValido(this.horaActual)) {
			for (Estacionamiento e : estacionamientos) {
				e.terminarEstacionamiento();
				this.notificarEntidadesFinDeEstacionamiento(e);
			}
			estacionamientos.removeAll(estacionamientos);
		} 
	} 
	
	public void addZona(ZonaSEM zona) {
		zonas.add(zona);
	}
	
	public void addInfraccion(Infraccion infraccion) {
		infracciones.add(infraccion);
	}
	 
	public void addCompra(Compra compra) {
		compras.add(compra);
		this.entidades.forEach(entidad -> entidad.notificarCompraNueva(compra));
	}  
	
	public void addEstacionamiento(Estacionamiento estacionamiento) {
		estacionamientos.add(estacionamiento);
	}
	
	public void suscribirEntidad(Entidad entidad) {
		entidades.add(entidad);
	}
	
	public void desuscribirEntidad(Entidad entidad) {
		entidades.remove(entidad);
	}
	
	public boolean tieneSaldoSuficiente(Celular cel) {
		return cel.getCredito() >= precioXHora;
	}
	
	public void setHoraActual(LocalTime hora) {
		this.horaActual = hora; 
	}
	
	public LocalTime getHoraActual() {
		return this.horaActual; 
	}

	public boolean esHorarioValido(LocalTime horaActual) {
		return horaActual.isAfter(LocalTime.of(7,0)) && horaActual.isBefore(LocalTime.of(20, 0));
	}

	public boolean estaVigente(String patente) {
		return this.estacionamientos.stream().anyMatch(e -> e.getPatente().equals(patente));
	}
	public List<Estacionamiento> getEstacionamientos() {
		return this.estacionamientos; 
	}

	public void altaDeInfraccion(String patente, Inspector inspector, ZonaSEM zona) {
		if (!this.estaVigente(patente)) {
			Infraccion infraccion = new Infraccion (patente, LocalDate.now(), LocalTime.now(), inspector, zona);
			this.addInfraccion(infraccion);
		}
		
	}
	
}
