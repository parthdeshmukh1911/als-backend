package com.als.auth;

import com.als.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
        service.updatePassword(updatePasswordRequest);
        return ResponseEntity.ok("Done updated password!!");
    }
    
    @PutMapping("/{email}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable("email") String email, @RequestBody Role newRole) {
        service.updateRole(email, newRole);
        return ResponseEntity.ok("User role updated successfully.");
    }
    
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
        service.deleteUser(email);
        return ResponseEntity.ok("User Deleted successfully.");
    }
    
}
