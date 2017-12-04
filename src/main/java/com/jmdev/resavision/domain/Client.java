package com.jmdev.resavision.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.jmdev.resavision.domain.enumeration.Civilite;


/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "civilite")
    private Civilite civilite;

    @Column(name = "check_tel")
    private Boolean checkTel;

    @Column(name = "chek_mail")
    private Boolean chekMail;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public Client civilite(Civilite civilite) {
        this.civilite = civilite;
        return this;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    public Boolean isCheckTel() {
        return checkTel;
    }

    public Client checkTel(Boolean checkTel) {
        this.checkTel = checkTel;
        return this;
    }

    public void setCheckTel(Boolean checkTel) {
        this.checkTel = checkTel;
    }

    public Boolean isChekMail() {
        return chekMail;
    }

    public Client chekMail(Boolean chekMail) {
        this.chekMail = chekMail;
        return this;
    }

    public void setChekMail(Boolean chekMail) {
        this.chekMail = chekMail;
    }

    public User getUser() {
        return user;
    }

    public Client user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", civilite='" + getCivilite() + "'" +
            ", checkTel='" + isCheckTel() + "'" +
            ", chekMail='" + isChekMail() + "'" +
            "}";
    }
}
