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
    public class Users extends Model {

    @Id
    protected Long id;

    protected String vorname;

    protected String nachname;

    protected String email;

    protected String wohnort;

    protected String birth;

    protected String sex;

    protected String ip;

    protected String registration;

    protected String lastlogin;

    protected String lastsessionstart;

    protected Integer active;

    protected Integer blocked;

    protected String homepage;

    protected String profilbild;

    protected String beschreibung;

    protected String comment;

    protected String pwsafe;

    protected String sessionid;

    public Long getId() {
        return this.id;
    }

    public void setId(Long ID) {
        this.id = ID;
    }

    public String getVorname() {
        return this.vorname;
    }

    public void setVorname(String Vorname) {
        this.vorname = Vorname;
    }

    public String getNachname() {
        return this.nachname;
    }

    public void setNachname(String Nachname) {
        this.nachname = Nachname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getWohnort() {
        return this.wohnort;
    }

    public void setWohnort(String Wohnort) {
        this.wohnort = Wohnort;
    }

    public String getBirth() {
        return this.birth;
    }

    public void setBirth(String Birth) {
        this.birth = Birth;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String Sex) {
        this.sex = Sex;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String Ip) {
        this.ip = Ip;
    }

    public String getRegistration() {
        return this.registration;
    }

    public void setRegistration(String Registration) {
        this.registration = Registration;
    }

    public String getLastlogin() {
        return this.lastlogin;
    }

    public void setLastlogin(String Lastlogin) {
        this.lastlogin = Lastlogin;
    }

    public String getLastsessionstart() {
        return this.lastsessionstart;
    }

    public void setLastsessionstart(String Lastsessionstart) {
        this.lastsessionstart = Lastsessionstart;
    }

    public Integer getActive() {
        return this.active;
    }

    public void setActive(Integer Active) {
        this.active = Active;
    }

    public Integer getBlocked() {
        return this.blocked;
    }

    public void setBlocked(Integer Blocked) {
        this.blocked = Blocked;
    }

    public String getHomepage() {
        return this.homepage;
    }

    public void setHomepage(String Homepage) {
        this.homepage = Homepage;
    }

    public String getProfilbild() {
        return this.profilbild;
    }

    public void setProfilbild(String Profilbild) {
        this.profilbild = Profilbild;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public void setBeschreibung(String Beschreibung) {
        this.beschreibung = Beschreibung;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String Comment) {
        this.comment = Comment;
    }

    public String getPwsafe() {
        return this.pwsafe;
    }

    public void setPwsafe(String Pwsafe) {
        this.pwsafe = Pwsafe;
    }

    public String getSessionId() {
        return this.sessionid;
    }

    public void setSessionId(String sessionId) {
        this.sessionid = sessionId;
    }

    public static Finder<Long,Users> find = new Finder<Long,Users>(
            Long.class, Users.class
    );

    public static Users findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static Users findById(String id) {
        return find.where().eq("id", id).findUnique();
    }
}
