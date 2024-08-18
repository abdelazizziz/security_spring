package tech.chillo.avis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import tech.chillo.avis.entite.Utilisateur;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByEmail(String email);
}
