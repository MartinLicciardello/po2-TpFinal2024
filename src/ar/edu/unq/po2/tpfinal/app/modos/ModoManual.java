package ar.edu.unq.po2.tpfinal.app.modos;

import ar.edu.unq.po2.tpfinal.app.AppUsuario;

public class ModoManual implements EstrategiaModo{

	@Override
	public String alertaFinEstacionamiento(AppUsuario app) {
		return "No se ha finalizado el estacionamiento.";
		
	} 

	@Override
	public String alertaInicioEstacionamiento(AppUsuario app) {
		return "No se ha inicado el estacionamiento.";
	}
} 
