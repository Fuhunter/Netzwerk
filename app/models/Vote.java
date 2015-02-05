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
public class Vote extends Model {

    @Id
    protected Long post_id;

    protected Long poster_id;

    protected Long voter_id;

    protected String voter_name;

    protected Date timestamp;

    protected Long vote;

    protected Long votesum;

    public Long getId() {
        return this.post_id;
    }

    public Long getPosterId() { return this.poster_id;}

    public Long getVoter() { return this.voter_id;}

    public String getVoter_name() { return this.voter_name;}

    public Date getTimestamp() {return this.timestamp;}

    public Long getVote() { return this.vote;}

    public Long getVotesum() { return this.votesum;}

    public void setId(Long ID) {
        this.post_id = ID;
    }

    public void setPosterId(Long ID) {this.poster_id = ID;}

    public void setVoter(Long ID) {this.voter_id = ID;}

    public void setVoter_name(String Text) {
        this.voter_name = Text;
    }

    public void setTimestamp(Date Time) {this.timestamp = Time;}

    public void setVote(Long votes) { this.vote = votes;}

    public void setVotesum(Long sum) {this.votesum = sum;}

    public static Finder<Long, Vote> find = new Finder<Long,Vote>(
            Long.class, Vote.class
    );
    public static Vote findById(Long post_id) {
        return find.where().eq("post_id", post_id).findUnique();
    }
}