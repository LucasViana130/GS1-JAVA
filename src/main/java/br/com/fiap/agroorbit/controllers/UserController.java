package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.UserRequest;
import br.com.fiap.agroorbit.dtos.response.UserResponse;
import br.com.fiap.agroorbit.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = service.findAll();

        users.forEach(this::addLinks);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse response = service.findById(id);

        addLinks(response);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        UserResponse response = service.update(id, request);

        addLinks(response);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    private void addLinks(UserResponse response) {
        Link selfLink = linkTo(methodOn(UserController.class).findById(response.getId())).withSelfRel();
        Link listLink = linkTo(methodOn(UserController.class).findAll()).withRel("users");

        response.add(selfLink);
        response.add(listLink);
    }
}