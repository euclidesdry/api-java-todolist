package pro.euclides.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import pro.euclides.todolist.user.IUserRepository;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authorization = request.getHeader("Authorization");
        var authEncoded = authorization.substring("Basic".length()).trim();
        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
        String authDecodedString = new String(authDecoded);

        String[] credentials = authDecodedString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        var user = this.userRepository.findByUsername(username);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No user found");
            return;
        } else {
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (passwordVerify.verified) {
                request.setAttribute("user", user);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong password");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
