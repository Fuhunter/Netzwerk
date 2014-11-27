/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * User Database Model
 */

package models;

import play.db.ebean.*;

import javax.persistence.*;

@Entity
public class Groups extends Model {

    @Id
    protected Long group_id;

    protected String gruppenname;

    protected  String gruppenbeschreibung;

    protected String gruppentags;

    public Long getId() {
        return this.group_id;
    }

    public void setId(Long ID) {
        this.group_id = ID;
    }

    public String getGruppenname() {
        return this.gruppenname;
    }

    public void setGruppenname(String Gruppenname) {
        this.gruppenname = Gruppenname;
    }

    public String getGruppenbeschreibung() {
        return this.gruppenbeschreibung;
    }

    public void setGruppenbeschreibung(String Gruppenbeschreibung) {
        this.gruppenbeschreibung = Gruppenbeschreibung;
    }

    public String getGruppentags() {
        return this.gruppentags;
    }

    public void setGruppentags(String Gruppentags) {
        this.gruppentags = Gruppentags;
    }

    public static Finder<Long, Groups> find = new Finder<Long, Groups>(
            Long.class, Groups.class
    );

    public static Groups findByGruppenname(String gruppenname) {
        return find.where().eq("gruppenname", gruppenname).findUnique();
    }

    public static Groups findById(Long id) {
        return find.where().eq("group_id", id).findUnique();
    }

}
