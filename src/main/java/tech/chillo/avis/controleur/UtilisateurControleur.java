package tech.chillo.avis.controleur;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.chillo.avis.Dto.AuthenicationDTO;
import tech.chillo.avis.entite.Utilisateur;
import tech.chillo.avis.securite.JwtService;
import tech.chillo.avis.service.UtilisateurService;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurControleur {

    private UtilisateurService utilisateurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping(path = "inscription")
    public void inscription(@RequestBody Utilisateur utilisateur) {
        log.info("Inscription");
        this.utilisateurService.inscription(utilisateur);
    }
    @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.activation(activation);
    }

    @PostMapping(path = "connexion")
    public Map<String, String> connexion(@RequestBody AuthenicationDTO authenicationDTO) {
      final   Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenicationDTO.username(), authenicationDTO.password()));
      log.info("authentication is {}", authenticate.isAuthenticated());
      if (authenticate.isAuthenticated()) {
          return jwtService.generer(authenicationDTO.username());
      }
        return null;

    }
}
