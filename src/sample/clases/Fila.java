package sample.clases;

public class Fila
{
    private Proceso process;
    private int use;
    private int free;

    public Fila(int use, int free){
        this.use = use;
        this.free = free;
    }

    public Proceso getProcess() { return process; }
    public void setProcess(Proceso process) { this.process = process; }
    public int getUse() { return use; }
    public void setUse(int use) { this.use = use; }
    public int getFree() { return free; }
    public void setFree(int free) { this.free = free; }
}