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
    protected Long id;

    protected String gruppenname;

    protected  String gruppenbeschreibung;

    protected String gruppentags;

    public static Finder<Long,Groupsmodel> find = new Finder<Long,Groupsmodel>(
            Long.class, Groupsmodel.class
    );

    public static Groupsmodel findByGruppenname(String gruppenname) {
        return find.where().eq("gruppenname", gruppenname).findUnique();
    }
}
