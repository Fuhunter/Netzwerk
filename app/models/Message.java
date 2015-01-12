/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Message Model
 */

package models;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Message extends Model {

    @Id
    protected Long id;

    protected Long aid;

    protected Long eid;

    protected String betreff;

    protected String nachricht;

    protected Date datum;

    protected Boolean readed;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAid() {
        return this.aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getEid() {
        return this.eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

    public String getBetreff() {
        return this.betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getNachricht() {
        return this.nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getDatum() {
        return this.datum;
    }

    public void setReaded(Boolean readed) {
        this.readed = readed;
    }

    public Boolean getReaded() {
        return this.readed;
    }

    public static Finder<Long, Message> find = new Finder<Long, Message>(
            Long.class, Message.class
    );
}
