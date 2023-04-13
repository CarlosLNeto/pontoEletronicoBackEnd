package com.api.pontoeletronico.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.pontoeletronico.dtos.LoginDto;
import com.api.pontoeletronico.dtos.LoginResponseDto;
import com.api.pontoeletronico.dtos.UserDto;
import com.api.pontoeletronico.models.UserModel;
import com.api.pontoeletronico.services.UserService;

import jakarta.validation.Valid;



@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    final UserService UserService;

    public UserController(UserService UserService) {
        this.UserService = UserService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto UserDto){
        if(UserService.existsByLogin(UserDto.getLogin())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Login já está em uso!");
        }
        if(UserService.existsByRegistration(UserDto.getRegistration())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Matricula já está em uso!");
        }
        var UserModel = new UserModel();
        BeanUtils.copyProperties(UserDto, UserModel);
        UserModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        UserModel.setPassword(passwordEncoder.encode(UserDto.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserService.save(UserModel));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid LoginDto loginDto) {
        Optional<UserModel> userModelOptional = UserService.findByLogin(loginDto.getLogin());

        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }

        UserModel userModel = userModelOptional.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), userModel.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }

        // Create the response object with name and registration
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setName(userModel.getName());
        loginResponseDto.setRegistration(userModel.getRegistration());
        loginResponseDto.setId(userModel.getId());

        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }





    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(UserService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> UserModelOptional = UserService.findById(id);
        if (!UserModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> UserModelOptional = UserService.findById(id);
        if (!UserModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        UserService.delete(UserModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid UserDto UserDto){
        Optional<UserModel> UserModelOptional = UserService.findById(id);
        if (!UserModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var UserModel = new UserModel();
        BeanUtils.copyProperties(UserDto, UserModel);
        UserModel.setId(UserModelOptional.get().getId());
        UserModel.setRegistrationDate(UserModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(UserService.save(UserModel));
    }



}