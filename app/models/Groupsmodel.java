/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * User Database Model
 */

package models;

import play.db.ebean.*;
import play.data.validation.*;
import java.util.*;
import javax.persistence.*;

@Entity
public class Groupsmodel extends Model {

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

    public static Finder<Long,Groupsmodel> find = new Finder<Long,Groupsmodel>(
            Long.class, Groupsmodel.class
    );

    public static Groupsmodel findByGruppenname(String gruppenname) {
        return find.where().eq("gruppenname", gruppenname).findUnique();
    }
}
