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
public class Friendship extends Model {

    @Id
    protected Long friendship_id;

    protected Long user_id;

    protected Long friend_id;

    protected Date timestamp;

    protected Integer status;

    public Long getId() {
        return this.friendship_id;
    }

    public void setId(Long ID) {
        this.friendship_id = ID;
    }

    public void setUserid(Long uid){
        this.user_id = uid;
    }

    public Long getUserid(){
        return this.user_id;
    }

    public void setFriendid(Long fid) {
        this.friend_id = fid;
    }

    public Long getFriendid() {
        return this.friend_id;
    }

    public void setTimestamp(Date stamp) {
        this.timestamp = stamp;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public static Finder<Long, Friendship> find = new Finder<Long, Friendship>(
            Long.class, Friendship.class
    );

    public static Friendship findById(String id) {
        return find.where().eq("freund_id", id).findUnique();
    }
}
