package ar.edu.unq.po2.tpfinal.app;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.po2.tpfinal.app.estadoEstacionamiento.EstaEstacionado;
import ar.edu.unq.po2.tpfinal.app.estadoEstacionamiento.EstadoEstacionamiento;
import ar.edu.unq.po2.tpfinal.app.estadoEstacionamiento.NoEstaEnZona;
import ar.edu.unq.po2.tpfinal.app.estadoEstacionamiento.NoEstaEstacionado;
import ar.edu.unq.po2.tpfinal.app.modos.EstrategiaModo;
import ar.edu.unq.po2.tpfinal.app.modos.ModoAutomatico;
import ar.edu.unq.po2.tpfinal.app.modos.ModoManual;
import ar.edu.unq.po2.tpfinal.sem.SEM;

public class AppUsuarioTest {
    private String patente;
    private SEM sem;
    private Celular cel;
    private EstrategiaModo modo;
    private EstadoEstacionamiento estado;
    private AppUsuario app;

    @BeforeEach
    public void setUp() {
        LocalTime horaActual = LocalTime.of(9, 0);
        patente = "ABC123";
        estado = spy(new NoEstaEstacionado()); 
        sem = mock(SEM.class);
        cel = mock(Celular.class);
        modo = new ModoManual(); 
        app = new AppUsuario(patente, sem, cel, estado, modo);
        app.setHoraActual(horaActual);
 
        when(sem.tieneSaldoSuficiente(cel)).thenReturn(true);
        when(cel.getCredito()).thenReturn((double) 200); 
    }

    @Test 
    public void testConsultarSaldo() {
        assertEquals(200, app.consultarSaldo()); 
    }

    @Test
    public void testInicioEstacionamiento() {
        app.inicioEstacionamiento();
        verify(estado).iniciarEstacionamiento(app, sem, cel, patente, app.getHoraActual());
        verify(cel).alerta(estado.iniciarEstacionamiento(app, sem, cel, patente, app.getHoraActual()));
    }
    
    @Test 
    public void testFinEstacionamiento() {
    	app.finEstacionamiento();
        verify(estado).finalizarEstacionamiento(sem, cel, app);
        verify(cel).alerta(estado.finalizarEstacionamiento(sem, cel, app));
    }
    
    @Test
    public void testSetyGetEstadoEstacionamiento() {
    		
    		EstadoEstacionamiento estaEstacionado = new EstaEstacionado();
    		app.setEstadoEstacionamiento(estaEstacionado);
    		assertEquals(estaEstacionado, app.getEstado());
    }
    
    @Test
    public void testSetyGetHoraActual() {
    	app.setHoraActual(LocalTime.of(6, 0));
    	assertEquals(LocalTime.of(6, 0), app.getHoraActual());
    }
    
    @Test
    public void testSetyGetModo() {
    	EstrategiaModo modoAutomatico = new ModoAutomatico();
    	app.setModo(modoAutomatico);
    	assertEquals(modoAutomatico, app.getModo());
    }
    
    @Test
    public void testAhoraEstasCaminandoNoEstaEstacionado() {
    	app.ahoraEstasCaminando();
    	verify(estado).ahoraEstasCaminando(app, cel);
    	verify(cel).alerta(modo.alertaInicioEstacionamiento(app));
    }
    
    @Test
    public void testAhoraEstasManejandoNoEstaEstacionado() {
    	app.ahoraEstasManejando();
    	verify(estado).ahoraEstasManejando(app, cel);
    }
    
    @Test
    public void testAhoraEstasCaminandoEstaEstacionado() {
    	app.setEstadoEstacionamiento(new EstaEstacionado());
    	app.ahoraEstasCaminando();
    	verifyNoInteractions(estado);
    }
    
    @Test
    public void testAhoraEstasManejandoEstaEstacionado() {
    	app.setEstadoEstacionamiento(new EstaEstacionado());
    	app.ahoraEstasManejando();
    	verifyNoInteractions(estado);
    	verify(cel).alerta(modo.alertaFinEstacionamiento(app));
    }
    
    @Test
    public void testAhoraEstasCaminandoNoEstaEnZona() {
    	app.setEstadoEstacionamiento(new NoEstaEnZona());
    	app.ahoraEstasCaminando();
    	verifyNoInteractions(estado);
    }
    
    @Test
    public void testAhoraEstasManejandoNoEstaEnZona() {
    	app.setEstadoEstacionamiento(new EstaEstacionado());
    	app.ahoraEstasManejando();
    	verifyNoInteractions(estado);
    }
    
}
