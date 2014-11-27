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

    @Id
    protected Long id;

    protected Long user_id;

    protected Long group_id;

    public Long getId() {
        return (this.id);
    }

    public void setId(Long ID) {
        this.id = ID;
    }

    public void setMember(Long users){
        this.user_id = users;
    }

    public Long getMember(){
        return(this.user_id);
    }

    public Long getGroup(){
        return(this.group_id);
    }

    public void setGroup(Long groups){
        this.group_id = groups;
    }

    public static Finder<Long, Groupmembers> find = new Finder<Long, Groupmembers>(
            Long.class, Groupmembers.class
    );

    public static Groupmembers findById(long user_id, long group_id){

        return find.where().eq("user_id", user_id).eq("group_id", group_id).findUnique();
    }


}
