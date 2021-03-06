package ba.etf.unsa.nwt.user_service.user_service.rest;

import ba.etf.unsa.nwt.user_service.user_service.model.UserDTO;
import ba.etf.unsa.nwt.user_service.user_service.model.UserToReturn;
import ba.etf.unsa.nwt.user_service.user_service.service.UserService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
//    @Autowired
//    private UserService userService;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserToReturn>> getAllUsers() {
        List<UserToReturn> returnList = userService.findAll().stream().map(userDTO -> new UserToReturn(userDTO)).collect(Collectors.toList());
        return ResponseEntity.ok(returnList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserToReturn> getUser(@PathVariable final UUID id) {
        return ResponseEntity.ok(new UserToReturn(userService.get(id)));
    }
    @GetMapping("/role")
    public ResponseEntity<List<UserToReturn>> getUserByRole(@RequestParam final String role) {
        List<UserToReturn> returnList = userService.getUsersByRole(role).stream().map(userDTO -> new UserToReturn(userDTO)).collect(Collectors.toList());
        return ResponseEntity.ok(returnList);
    }

    @PostMapping
    public ResponseEntity<UUID> createUser(@RequestBody @Valid final UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateUser(@PathVariable final UUID id,
            @RequestBody @Valid final UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteUser(@PathVariable final UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}

/*
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable final UUID id) {
        return ResponseEntity.ok(userService.get(id));
    }
    @GetMapping("/role")
    public ResponseEntity<List<UserDTO>> getUserByRole(@RequestParam final String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }*/
