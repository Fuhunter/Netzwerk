/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * Friendship Model: ID, Friend#1 (befriender), Friend#2 (befriended), status, date
 */

package models;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Messages extends Model {

    @Id
    protected Long id;

    protected Long aid;

    protected Long eid;

    protected String betreff;

    protected String nachricht;

    protected Date date;

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

    public static Finder<Long, Friendship> find = new Finder<Long, Friendship>(
            Long.class, Friendship.class
    );
}
