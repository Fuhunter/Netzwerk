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

    protected String poster_name;

    protected String post_text;

    protected Date timestamp;

    protected Long public_post;

    protected Long vote;

    public Long getId() {
        return this.post_id;
    }

    public Long getPoster() { return this.poster_id;}

    public String getPost() { return this.post_text;}

    public String getPoster_name() { return this.poster_name;}

    public Date getTimestamp() {return this.timestamp;}

    public Long getPublic_post() {return this.public_post;}

    public Long getVote() {
        return this.vote;
    }

    public void setId(Long ID) {
        this.post_id = ID;
    }

    public void setPoster(Long ID) {this.poster_id = ID;}

    public void setText(String Text) {
        this.post_text = Text;
    }

    public void setPoster_name(String Text) {
        this.poster_name = Text;
    }

    public void setTimestamp(Date Time) {this.timestamp = Time;}

    public void setPublic_post(Long public_post){this.public_post = public_post;}

    public void setVote(Long votes) {
        this.vote = votes;
    }

    public static Finder<Long, Post> find = new Finder<Long, Post>(
            Long.class, Post.class
    );

    public static Post findById(Long post_id) {
        return find.where().eq("post_id", post_id).findUnique();
    }
}
