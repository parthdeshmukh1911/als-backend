package com.als.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.als.config.JwtService;
import com.als.emailservice.EmailService;
import com.als.employee.EmployeeRepository;
import com.als.user.Role;
import com.als.user.User;
import com.als.user.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeService;
    private final EmailService emailService;
    @Autowired
    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
                               AuthenticationManager authenticationManager, EmployeeRepository employeeService, EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.emailService = emailService;
    }

    public AuthenticationResponse register(RegisterRequest request) {
//        System.out.println("Register method called");
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        System.out.println("Authenticate method called");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
//        System.out.println("Fetching the details from repo");
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
//        System.out.println("User Details : "+user);
        var jwtToken = jwtService.generateToken(user);
//        System.out.println("Jwt token :"+jwtToken);
        var employee = employeeService.findByEmailId(request.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .employeeId(employee.getId())
                .role(user.getRole())
                .build();
    }

    public void updatePassword(UpdatePasswordRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(); // Assuming the user must exist
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        String to = request.getEmail();
        String subject = " Passsword update  " ;
        String body = "Dear Employee your password has been updated ,\n\n" +
                "Your new Crendentials is .\n" +
                "UserId: " +request.getEmail() + "\n" +
                "Password: " +request.getNewPassword() + "\n\n" +
                "Regards,\n" +
                "Leave Management System";
        emailService.sendMail(to, subject, body);
    }

    public void updateRole(String email, Role newRole) {
         repository.updateRoleByEmail(email ,newRole);
    }

    public void deleteUser(String email){
       try{
    	   User user=repository.findByEmail(email).get();
    	   repository.delete(user);
       }catch(Exception e){
          System.out.println("User could not be deleted!!!!");
       }
    }
}
