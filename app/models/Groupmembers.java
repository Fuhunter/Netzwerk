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

    public Long getMember(Long users){
        return(this.user_id);
    }

    public Long getGroup(Long groups){
        return(this.group_id);
    }

    public void setGroup(Long groups){
        this.group_id = groups;
    }

    public static Finder<Long, Groups> find = new Finder<Long, Groups>(
            Long.class, Groups.class
    );
}
