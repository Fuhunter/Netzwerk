/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * User Database Model
 */

package models;

import controllers.Network;
import play.db.ebean.*;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post extends Model {

    @Id
    protected Long post_id;

    protected Long poster_id;

    protected String post_text;

    protected Date timestamp;

    public Long getId() {
        return this.post_id;
    }

    public Long getPoster() { return this.poster_id;}

    public String getPost() { return this.post_text;}

    public Date getTimestamp() {return this.timestamp;}

    public void setId(Long ID) {
        this.post_id = ID;
    }

    public void setPoster(Long ID) {this.poster_id = ID;}

    public void setText(String Text) {
        this.post_text = Text;
    }

    public void setTimestamp(Date Time) {this.timestamp = Time;}

    public static Finder<Long, Post> find = new Finder<Long, Post>(
            Long.class, Post.class
    );

    public static Post findById(Long post_id) {
        return find.where().eq("post_id", post_id).findUnique();
    }
}
