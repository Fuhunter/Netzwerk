/**
 * @author Thomas Dennhardt, Christoph Gaudl and Niclas Günther
 *
 * User Database Model
 */

package models;

import controllers.Network;
import play.db.ebean.*;

import javax.persistence.*;

@Entity
public class Post extends Model {

    @Id
    protected Long post_id;

    protected Long poster_id;

    protected String post_text;

    public Long getId() {
        return this.post_id;
    }

    public Long getPoster() { return this.poster_id;}

    public String getPost() { return this.post_text;}

    public void setId(Long ID) {
        this.post_id = ID;
    }

    public void setPoster(Long ID) {this.poster_id = ID;}

    public void setText(String Text) {
        this.post_text = Text;
    }

    public static Finder<Long, Post> find = new Finder<Long, Post>(
            Long.class, Post.class
    );

    public static Post findById(Long post_id) {
        return find.where().eq("post_id", post_id).findUnique();
    }
}
