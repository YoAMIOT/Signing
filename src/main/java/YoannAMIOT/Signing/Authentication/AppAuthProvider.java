package YoannAMIOT.Signing.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppAuthProvider extends DaoAuthenticationProvider{

	@Autowired
	UserService userDetailsService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
    	String name = auth.getName();
        String rawPassword = auth.getCredentials().toString();
        UserDetails user = userDetailsService.loadUserByUsername(name);
        
        if (user == null || !checkPassword(rawPassword, user.getPassword())) {
        	throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal());
        }
        
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
    
    
    
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    
    
    
    private boolean checkPassword(String rawPassword, String hash) {
    	return passwordEncoder.matches(rawPassword, hash);
    }
}
