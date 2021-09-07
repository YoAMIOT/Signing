package YoannAMIOT.ANPEPSigning.Authentication;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import YoannAMIOT.ANPEPSigning.entities.User;
import YoannAMIOT.ANPEPSigning.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
    @Autowired
    private UserRepository userRepository;

    public UserService() {
        
    }
    
	
    
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Objects.requireNonNull(email);
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utilisateur"));
		return user;
	}
}
    
