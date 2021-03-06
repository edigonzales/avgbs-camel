package ch.so.agi.avgbs.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Av2GbMessage {
    private int t_id;
    private String beschrieb;
    private String dateinameplan;
    private Date endetechnbereit;
    private String istprojektmutation;
    private String mutationsnummerNummer;
    private String mutationsnummerNBIdent;
    private Timestamp importdate;
    
    public int getT_id() {
        return t_id;
    }
    public void setT_id(int t_id) {
        this.t_id = t_id;
    }
    public String getBeschrieb() {
        return beschrieb;
    }
    public void setBeschrieb(String beschrieb) {
        this.beschrieb = beschrieb;
    }
    public String getDateinameplan() {
        return dateinameplan;
    }
    public void setDateinameplan(String dateinameplan) {
        this.dateinameplan = dateinameplan;
    }
    public Date getEndetechnbereit() {
        return endetechnbereit;
    }
    public void setEndetechnbereit(Date endetechnbereit) {
        this.endetechnbereit = endetechnbereit;
    }
    public String getIstprojektmutation() {
        return istprojektmutation;
    }
    public void setIstprojektmutation(String istprojektmutation) {
        this.istprojektmutation = istprojektmutation;
    }
    public String getMutationsnummerNummer() {
        return mutationsnummerNummer;
    }
    public void setMutationsnummerNummer(String mutationsnummerNummer) {
        this.mutationsnummerNummer = mutationsnummerNummer;
    }
    public String getMutationsnummerNBIdent() {
        return mutationsnummerNBIdent;
    }
    public void setMutationsnummerNBIdent(String mutationsnummerNBIdent) {
        this.mutationsnummerNBIdent = mutationsnummerNBIdent;
    }
    public Timestamp getImportdate() {
        return importdate;
    }
    public void setImportdate(Timestamp importdate) {
        this.importdate = importdate;
    }
}
