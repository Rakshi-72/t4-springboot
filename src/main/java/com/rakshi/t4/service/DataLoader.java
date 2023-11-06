package com.rakshi.t4.service;

import com.rakshi.t4.models.Role;
import com.rakshi.t4.models.User;
import com.rakshi.t4.repos.RoleRepo;
import com.rakshi.t4.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = List.of(new Role(1, "ROLE_SELLER"),
                new Role(2, "ROLE_CONSUMER"));
        roleRepo.saveAll(roles);

        User sellerUser = new User(1, "rakshithjogihalli@gmail.com", "143", List.of(roleRepo.findById(1).get()));
        User consumerUser = new User(2, "rakshi@123.com", "123", List.of(roleRepo.findById(2).get()));
        userRepo.saveAll(List.of(sellerUser, consumerUser));


    }
}
