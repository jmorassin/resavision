package com.jmdev.resavision.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Boutique.
 */
@Entity
@Table(name = "boutique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "boutique")
public class Boutique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Column(name = "url")
    private String url;

    @NotNull
    @Column(name = "num_telephone", nullable = false)
    private String numTelephone;

    @NotNull
    @Column(name = "mail", nullable = false)
    private String mail;

    @NotNull
    @Column(name = "adresse_1", nullable = false)
    private String adresse1;

    @Column(name = "adresse_2")
    private String adresse2;

    @OneToMany(mappedBy = "boutique")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employe> employes = new HashSet<>();

    @ManyToOne
    private Ville ville;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "boutique_type_boutique",
               joinColumns = @JoinColumn(name="boutiques_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="type_boutiques_id", referencedColumnName="id"))
    private Set<TypeBoutique> typeBoutiques = new HashSet<>();

    @OneToOne(mappedBy = "boutique")
    @JsonIgnore
    private Responsable responsable;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Boutique nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public byte[] getLogo() {
        return logo;
    }

    public Boutique logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public Boutique logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getUrl() {
        return url;
    }

    public Boutique url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public Boutique numTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
        return this;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getMail() {
        return mail;
    }

    public Boutique mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public Boutique adresse1(String adresse1) {
        this.adresse1 = adresse1;
        return this;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public Boutique adresse2(String adresse2) {
        this.adresse2 = adresse2;
        return this;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public Boutique employes(Set<Employe> employes) {
        this.employes = employes;
        return this;
    }

    public Boutique addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.setBoutique(this);
        return this;
    }

    public Boutique removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.setBoutique(null);
        return this;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
    }

    public Ville getVille() {
        return ville;
    }

    public Boutique ville(Ville ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Set<TypeBoutique> getTypeBoutiques() {
        return typeBoutiques;
    }

    public Boutique typeBoutiques(Set<TypeBoutique> typeBoutiques) {
        this.typeBoutiques = typeBoutiques;
        return this;
    }

    public Boutique addTypeBoutique(TypeBoutique typeBoutique) {
        this.typeBoutiques.add(typeBoutique);
        typeBoutique.getListBoutiques().add(this);
        return this;
    }

    public Boutique removeTypeBoutique(TypeBoutique typeBoutique) {
        this.typeBoutiques.remove(typeBoutique);
        typeBoutique.getListBoutiques().remove(this);
        return this;
    }

    public void setTypeBoutiques(Set<TypeBoutique> typeBoutiques) {
        this.typeBoutiques = typeBoutiques;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public Boutique responsable(Responsable responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
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
        Boutique boutique = (Boutique) o;
        if (boutique.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boutique.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Boutique{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", url='" + getUrl() + "'" +
            ", numTelephone='" + getNumTelephone() + "'" +
            ", mail='" + getMail() + "'" +
            ", adresse1='" + getAdresse1() + "'" +
            ", adresse2='" + getAdresse2() + "'" +
            "}";
    }
}
