package com.jmdev.resavision.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.jmdev.resavision.domain.enumeration.Civilite;


/**
 * A Employe.
 */
@Entity
@Table(name = "employe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employe")
public class Employe implements Serializable {

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

    @ManyToOne
    private Boutique boutique;

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

    public Employe civilite(Civilite civilite) {
        this.civilite = civilite;
        return this;
    }

    public void setCivilite(Civilite civilite) {
        this.civilite = civilite;
    }

    public Boolean isCheckTel() {
        return checkTel;
    }

    public Employe checkTel(Boolean checkTel) {
        this.checkTel = checkTel;
        return this;
    }

    public void setCheckTel(Boolean checkTel) {
        this.checkTel = checkTel;
    }

    public Boolean isChekMail() {
        return chekMail;
    }

    public Employe chekMail(Boolean chekMail) {
        this.chekMail = chekMail;
        return this;
    }

    public void setChekMail(Boolean chekMail) {
        this.chekMail = chekMail;
    }

    public User getUser() {
        return user;
    }

    public Employe user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boutique getBoutique() {
        return boutique;
    }

    public Employe boutique(Boutique boutique) {
        this.boutique = boutique;
        return this;
    }

    public void setBoutique(Boutique boutique) {
        this.boutique = boutique;
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
        Employe employe = (Employe) o;
        if (employe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employe{" +
            "id=" + getId() +
            ", civilite='" + getCivilite() + "'" +
            ", checkTel='" + isCheckTel() + "'" +
            ", chekMail='" + isChekMail() + "'" +
            "}";
    }
}
