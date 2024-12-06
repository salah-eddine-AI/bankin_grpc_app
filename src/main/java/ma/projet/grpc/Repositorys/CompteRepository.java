package ma.projet.grpc.Repositorys;

import ma.projet.grpc.Entitys.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, String> {
}