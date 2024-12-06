package ma.projet.grpc.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Correct JPA import
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import ma.projet.grpc.stubs.TypeCompte;

@Entity
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private float solde;
    private String dateCreation;

    @Enumerated(EnumType.STRING) // To store enums as string values in the database
    private TypeCompte type;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public TypeCompte getType() {
        return type;
    }

    public void setType(TypeCompte type) {
        this.type = type;
    }
}
