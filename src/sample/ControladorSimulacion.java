package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import sample.clases.Fila;
import sample.clases.Proceso;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorSimulacion implements Initializable
{
    @FXML Button btnMemoria,btnGenerar,btnSacar,btnTerminar;
    @FXML ComboBox cbPag,cbMarco,cbSacar;
    @FXML TextField txtMemoria;
    @FXML GridPane gridPaneTabla;
    @FXML TextArea txtSwapping,txtSalida;
    @FXML Label lblTamano,lblPag,lblMarco,lblProceso;

    Label tabla[][];
    Fila[] filas;
    ArrayList<Proceso> procesos;
    int paginas,marcos,tamanoMemoria,tamanoMarco;
    int procesoCont = 1;
    int tamanoProcesoGenerado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iniciarComponentes();
    }

    private void iniciarComponentes(){
        cbPag.getItems().addAll(4,6,8,10,12);
        cbPag.setValue(4);
        cbMarco.getItems().addAll(1,2,4,6);
        cbMarco.setValue(1);
        procesos = new ArrayList<>();
        btnGenerar.setDisable(true);
        btnSacar.setDisable(true);
        btnTerminar.setDisable(true);
        cbSacar.setDisable(true);
        txtSwapping.setEditable(false);
        txtSalida.setEditable(false);
        btnSacar.setOnAction(evento);
        btnMemoria.setOnAction(evento);
        btnGenerar.setOnAction(evento);
        btnTerminar.setOnAction(evento);
    }

    private void deshabilitarComponenetesHacerTabla(){
        //Componentes a deshabilitar
        cbPag.setDisable(true);
        cbMarco.setDisable(true);
        txtMemoria.setDisable(true);
        btnMemoria.setDisable(true);
        //Componentes a habilitar
        btnGenerar.setDisable(false);
        btnSacar.setDisable(false);
        cbSacar.setDisable(false);
        btnTerminar.setDisable(false);
    }
    private void deshCompUsarTabla(){
        //Componentes a deshabilitar
        cbPag.setDisable(false);
        cbMarco.setDisable(false);
        txtMemoria.setDisable(false);
        btnMemoria.setDisable(false);
        gridPaneTabla.getChildren().clear();
        procesos.clear();
        procesoCont = 1;
        txtSwapping.setText("");
        txtSalida.setText("");
        lblProceso.setText("");
        lblPag.setText("# Paginas :");
        lblTamano.setText("Tamaño Memoria :");
        lblMarco.setText("Tamano Marcos:");
        cbSacar.getItems().removeAll();
        cbSacar.getItems().clear();
        //Componentes a habilitar
        btnGenerar.setDisable(true);
        btnSacar.setDisable(true);
        cbSacar.setDisable(true);
        btnTerminar.setDisable(true);
    }
    private void hacerTabla(){
        tabla = new Label[paginas*marcos][3];
        tamanoMarco = tamanoMemoria / (marcos * paginas);
        filas = new Fila[marcos*paginas];
        lblTamano.setText("Tamaño Memoria : "+tamanoMemoria);
        lblMarco.setText("Tamano Marcos : "+(tamanoMarco));
        lblPag.setText("# Paginas : "+paginas);
        int color[] = {generarColor(),generarColor(),generarColor()};
        String estilo;
        byte fila = 0;
        for (int i=0 ; i<paginas ; i++){
            estilo = ("-fx-text-fill: black;" +
                    "-fx-background-color: rgb("+color[0]+","+color[1]+","+color[2]+");" +
                    "-fx-border-color: #000000");
            for (int j=0 ; j<marcos ; j++){
                filas[fila] = new Fila(0,tamanoMarco);
                tabla[fila][0] = new Label("LIBRE");
                tabla[fila][0].setStyle(estilo);
                tabla[fila][0].setAlignment(Pos.CENTER);
                tabla[fila][0].setMinWidth(80);
                tabla[fila][1] = new Label("0");
                tabla[fila][1].setStyle(estilo);
                tabla[fila][1].setAlignment(Pos.CENTER);
                tabla[fila][1].setMinWidth(80);
                tabla[fila][2] = new Label(""+tamanoMarco);
                tabla[fila][2].setStyle(estilo);
                tabla[fila][2].setAlignment(Pos.CENTER);
                tabla[fila][2].setMinWidth(80);
                gridPaneTabla.add(tabla[fila][0],0,fila+1);
                gridPaneTabla.add(tabla[fila][1],1,fila+1);
                gridPaneTabla.add(tabla[fila][2],2,fila+1);
                fila++;
            }
            color[0] = generarColor();
            color[1] = generarColor();
            color[2] = generarColor();
        }
        deshabilitarComponenetesHacerTabla();
    }
    private int generarColor(){ return (int) ((Math.random() * 220))+50; }
    private int generarProcesoTamano(){ return (int)( (Math.random()*800)  + 50); }
    private boolean validarDatosTabla(){
        boolean bandera;
        if (txtMemoria.getText().isEmpty()){
            bandera = false;
        }try{
            paginas = (int) cbPag.getValue();
            marcos = (int) cbMarco.getValue();
            tamanoMemoria = Integer.parseInt(txtMemoria.getText());
            bandera = true;
        }catch(NumberFormatException e){
            bandera = false;
        }
        return bandera;
    }
    private void darEstadoAProceso(){
        byte cont = 0;
        for (int i=0 ; i<filas.length ; i++){
            if (filas[i].getProcess() == null){
                cont++;
            }
        }
        if (tamanoProcesoGenerado <= cont*tamanoMarco)
            procesos.get(procesos.size()-1).setEstado((byte)0);
        else
            procesos.get(procesos.size()-1).setEstado((byte)1);
    }
    private void meterProcesoATabla(Proceso proceso){
        int temp;
        boolean bandera = true;
        byte recorrer = 0;
        while(bandera){
            if (filas[recorrer].getProcess() == null){
                if (tamanoMarco >= proceso.getSize()){
                    filas[recorrer].setProcess(proceso);
                    filas[recorrer].setUse(proceso.getSize());
                    filas[recorrer].setFree(tamanoMarco - proceso.getSize());
                    bandera = false;
                }
                if (tamanoMarco < proceso.getSize()){
                    temp = proceso.getSize() - tamanoMarco;
                    proceso.setSize(tamanoMarco);
                    filas[recorrer].setUse(tamanoMarco);
                    filas[recorrer].setFree(0);
                    filas[recorrer].setProcess(proceso);
                    proceso.setSize(temp);
                }
                tabla[recorrer][0].setText("P"+filas[recorrer].getProcess().getNumberProcess());  //PID
                tabla[recorrer][1].setText(filas[recorrer].getUse() + "");  //Uso
                tabla[recorrer][2].setText(filas[recorrer].getFree() + "");  //Libre
            }
            recorrer++;
        }
        meterProcesosDeSwapping();
        actualizarSalida();
    }
    private void meterProcesoASwapping(Proceso proceso){
        txtSwapping.appendText("P"+procesoCont+"   "+proceso.getSize()+"\n");
    }
    private void actualizarSalida(){
        cbSacar.getItems().removeAll();
        cbSacar.getItems().clear();
        for (int i=0 ; i<procesos.size() ; i++){
            if (procesos.get(i).getEstado() == 0)
                cbSacar.getItems().add(procesos.get(i).getNumberProcess());
        }
    }
    private void finalizarProceso(int proceso){
        procesos.get(proceso-1).setEstado((byte)2);
        txtSalida.appendText("P"+proceso+"\n");
        actualizarSalida();
        for (int i=0 ; i<filas.length ; i++){
            if ( !(filas[i].getProcess() == null) ){
                if (filas[i].getProcess().getNumberProcess() == proceso){
                    filas[i].setFree(tamanoMarco);
                    filas[i].setUse(0);
                    filas[i].setProcess(null);

                    tabla[i][0].setText(filas[i].getProcess()+"");
                    tabla[i][1].setText(filas[i].getUse()+"");
                    tabla[i][2].setText(filas[i].getFree()+"");
                }
            }
        }
        meterProcesosDeSwapping();
    }
    private void meterProcesosDeSwapping(){
        int proceso = 0;
        int temp = 850;
        int cont = 0;
        for(int i=0 ; i<procesos.size() ; i++){
            if (procesos.get(i).getEstado() == 1){
                if (procesos.get(i).getSize() < temp){
                    proceso = i;
                    temp = procesos.get(i).getSize();
                }
            }
        }
        if (proceso > 0){
            for (int i=0 ; i<filas.length ; i++){
                if (filas[i].getProcess() == null){
                    cont++;
                }
            }
        }
        if (procesos.get(proceso).getSize() <= (cont*tamanoMarco)){
            procesos.get(proceso).setEstado((byte)0);
            meterProcesoATabla(procesos.get(proceso));
            txtSwapping.setText("");
            for (int i=0 ; i<procesos.size() ; i++){
                if (procesos.get(i).getEstado() == 1){
                    txtSwapping.appendText("P"+procesos.get(i).getNumberProcess()+"   "+procesos.get(i).getSize()+"\n");
                }
            }
        }
    }

    EventHandler<ActionEvent> evento = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == btnSacar){
                finalizarProceso((int)cbSacar.getValue());
            }
            if (event.getSource() == btnGenerar){
                tamanoProcesoGenerado = generarProcesoTamano();
                procesos.add(new Proceso(procesoCont,tamanoProcesoGenerado));
                darEstadoAProceso();
                lblProceso.setText("Se Creo P"+procesoCont+" = "+tamanoProcesoGenerado);
                if (procesos.get(procesos.size()-1).getEstado() == 0)
                    meterProcesoATabla(procesos.get(procesos.size()-1));
                if (procesos.get(procesos.size()-1).getEstado() == 1)
                    meterProcesoASwapping(procesos.get(procesos.size()-1));
                procesoCont++;
            }
            if (event.getSource() == btnMemoria){
                if (validarDatosTabla()){
                    hacerTabla();
                }
            }
            if (event.getSource() == btnTerminar){
                deshCompUsarTabla();
            }
        }
    };
}