package sample.clases;

public class Proceso{
    private int numberProcess;
    private int size;
    private byte estado;

    public Proceso(int numberProcess,int size){
        this.numberProcess = numberProcess;
        this.size = size;
    }

    public int getNumberProcess() { return numberProcess; }
    public int getSize() { return size; }
    public byte getEstado() { return estado; }

    public void setSize(int size) { this.size = size; }
    public void setNumberProcess(int numberProcess) { this.numberProcess = numberProcess; }
    public void setEstado(byte estado) { this.estado = estado; }
}