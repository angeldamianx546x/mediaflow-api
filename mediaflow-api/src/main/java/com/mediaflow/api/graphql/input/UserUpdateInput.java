package com.mediaflow.api.graphql.input;

import java.time.LocalDate;
import java.util.List;

import com.mediaflow.api.dto.LocationRquest;
import com.mediaflow.api.dto.UserRequest;

import lombok.Data;
@Data
public class UserUpdateInput {
    private String name;
    private String email;
    private String password;
    private LocalDate dateBirth;
    private List<Integer> roles;
    private String preferredLanguage;
    private LocationInput location;

    public UserRequest toUserRequest() {
        UserRequest req = new UserRequest();
        req.setName(this.name);
        req.setEmail(this.email);
        req.setPassword(this.password);
        req.setDateBirth(this.dateBirth);
        req.setRoles(this.roles);
        req.setPreferredLanguage(this.preferredLanguage);
        
        if (this.location != null) {
            LocationRquest locReq = new LocationRquest();
            locReq.setCountry(this.location.getCountry());
            locReq.setRegion(this.location.getRegion());
            locReq.setCity(this.location.getCity());
            locReq.setLat(this.location.getLat());
            locReq.setLng(this.location.getLng());
            req.setLocation(locReq);
        }
        
        return req;
    }
}
