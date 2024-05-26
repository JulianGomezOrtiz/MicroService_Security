package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.UserProfile;
import com.ucaldas.mssecurity.Repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users-profiles")
public class UserProfileController {
    @Autowired
    private UserProfileRepository theUserProfileRepository;

    @GetMapping("")
    public List<UserProfile> findAll() {
        return this.theUserProfileRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)

    @PostMapping
    public UserProfile create(@RequestBody UserProfile theNewUserProfile) {
        return this.theUserProfileRepository.save(theNewUserProfile);
    }

    @GetMapping("{id}")
    public UserProfile findById(@PathVariable String id) {
        UserProfile theUserProfile = this.theUserProfileRepository
                .findById(id)
                .orElse(null);
        return theUserProfile;

    }

    @PutMapping("{id}")
    public UserProfile update(@PathVariable String id, @RequestBody UserProfile theNewUserProfile) {
        UserProfile theActualUserProfile = this.theUserProfileRepository
                .findById(id)
                .orElse(null);
        if (theActualUserProfile != null) {
            theActualUserProfile.setName(theNewUserProfile.getName());
            theActualUserProfile.setLast_name(theNewUserProfile.getLast_name());
            theActualUserProfile.setCity_of_residence(theNewUserProfile.getCity_of_residence());
            theActualUserProfile.setAddress(theNewUserProfile.getAddress());
            theActualUserProfile.setphone_number(theNewUserProfile.getPhone_number());
            theActualUserProfile.setBirthday(theNewUserProfile.getBirthday());

            return this.theUserProfileRepository.save(theActualUserProfile);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        UserProfile theUser = this.theUserProfileRepository
                .findById(id)
                .orElse(null);
        if (theUser != null) {
            this.theUserProfileRepository.delete(theUser);
        }
    }

}
