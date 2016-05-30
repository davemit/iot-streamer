package com.streamer.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Streams.
 */
@Entity
@Table(name = "streams")
public class Streams implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "puburl")
    private String puburl;

    @Column(name = "pubkey")
    private String pubkey;

    @Column(name = "privatekey")
    private String privatekey;

    @Column(name = "deletekey")
    private String deletekey;

    @Column(name = "format")
    private String format;

    @Column(name = "example")
    private String example;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPuburl() {
        return puburl;
    }

    public void setPuburl(String puburl) {
        this.puburl = puburl;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getDeletekey() {
        return deletekey;
    }

    public void setDeletekey(String deletekey) {
        this.deletekey = deletekey;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Streams streams = (Streams) o;
        if(streams.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, streams.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Streams{" +
            "id=" + id +
            ", puburl='" + puburl + "'" +
            ", pubkey='" + pubkey + "'" +
            ", privatekey='" + privatekey + "'" +
            ", deletekey='" + deletekey + "'" +
            ", format='" + format + "'" +
            ", example='" + example + "'" +
            '}';
    }
}
