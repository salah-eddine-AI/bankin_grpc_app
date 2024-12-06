package ma.projet.grpc.Repositorys;

import ma.projet.grpc.Entitys.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompteRepository extends JpaRepository<Compte, String> {

    List<Compte> findByType(String type);
}