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
public class Groupmembers extends Model {

    protected Long groups;

    protected Long users;

    public Long getId() {
        return (this.groups);
    }

    public void setId(Long ID) {
        this.groups = ID;
        this.users = ID;
    }

    public void setMember(Long users){
        this.users = users;
    }


    public Long getMember(Long users){
        return(this.users);
    }

    public Long getGroup(Long groups){
        return(this.groups);
    }

    public void setGroup(Long groups){
        this.groups = groups;
    }

}
