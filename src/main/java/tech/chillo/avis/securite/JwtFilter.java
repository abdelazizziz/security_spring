package tech.chillo.avis.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.chillo.avis.entite.Utilisateur;
import tech.chillo.avis.service.UtilisateurService;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private UtilisateurService utilisateurService;
    private JwtService jwtService;
    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username=null;
        String token=null;
        boolean isTokenExpired = true;
        final String authorization = request.getHeader("Authorization");

        if (authorization!=null && authorization.startsWith("Bearer ")){
            token=authorization.substring(7);

        isTokenExpired= jwtService.isTokenExpirede(token);


            username=jwtService.extractUsername(token);
        }
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails  utilisateurdetails=  utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(utilisateurdetails, null, utilisateurdetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }




            filterChain.doFilter(request, response);





    }
}

