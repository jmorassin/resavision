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
 * A TypeBoutique.
 */
@Entity
@Table(name = "type_boutique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typeboutique")
public class TypeBoutique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToOne
    private TypeSite typeSite;

    @ManyToMany(mappedBy = "typeBoutiques")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Boutique> listBoutiques = new HashSet<>();

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

    public TypeBoutique nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeSite getTypeSite() {
        return typeSite;
    }

    public TypeBoutique typeSite(TypeSite typeSite) {
        this.typeSite = typeSite;
        return this;
    }

    public void setTypeSite(TypeSite typeSite) {
        this.typeSite = typeSite;
    }

    public Set<Boutique> getListBoutiques() {
        return listBoutiques;
    }

    public TypeBoutique listBoutiques(Set<Boutique> boutiques) {
        this.listBoutiques = boutiques;
        return this;
    }

    public TypeBoutique addListBoutique(Boutique boutique) {
        this.listBoutiques.add(boutique);
        boutique.getTypeBoutiques().add(this);
        return this;
    }

    public TypeBoutique removeListBoutique(Boutique boutique) {
        this.listBoutiques.remove(boutique);
        boutique.getTypeBoutiques().remove(this);
        return this;
    }

    public void setListBoutiques(Set<Boutique> boutiques) {
        this.listBoutiques = boutiques;
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
        TypeBoutique typeBoutique = (TypeBoutique) o;
        if (typeBoutique.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeBoutique.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeBoutique{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
