package br.nicoletti.spring02.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.nicoletti.spring02.DTO.UserDto;
import br.nicoletti.spring02.dao.UserDAO;
import br.nicoletti.spring02.model.User;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {
    
    @Autowired // injeção de dependencia
    private UserDAO dao;

    @GetMapping("/all")
    public List<User> listarTodos(){
        List<User> lista = (List<User>) dao.findAll();

        return lista;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> BUscaPorId(@PathVariable int id){
        User user = dao.findById(id).orElse(null);

        if (user != null) {
            user.setPassword("********");
            return ResponseEntity.ok(user); // ok = 200
        }
        return ResponseEntity.notFound().build(); // nok = 404 - not found
    }

    @PostMapping("/new")
    public ResponseEntity<User> novoUsuario(@RequestBody User user){
        User newUser = dao.save(user);

        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/email")
    public ResponseEntity<UserDto> buscaPOrEmail(@RequestBody User user){
        User userFinded = dao.findByEmail(user.getEmail());

        if(userFinded != null){
            UserDto userDto = new UserDto(userFinded);
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/loginemail")
    public ResponseEntity<UserDto> loginPorEmail(@RequestBody User user){
        User userFinded = dao.findByEmailAndPassword(user.getEmail(), user.getPassword());
        
        if(userFinded != null){
            UserDto userDto = new UserDto(userFinded);
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();        
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody User user){
        User userFinded = dao.findByEmailOrCpf(user.getEmail(), user.getCpf());

        if(userFinded != null) {
            if(user.getPassword().equals(userFinded.getPassword())) {
                UserDto userDto = new UserDto(userFinded);
                return ResponseEntity.ok(userDto);
            }
            //return ResponseEntity.status(401).build();
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/id2/{id}")
    public ResponseEntity<User> buscarPersonalizado(@PathVariable int id) {
        User userFinded = dao.buscaPorId(id);

        if(userFinded != null){
            return ResponseEntity.ok(userFinded);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/id3/{id}")
    public ResponseEntity<Object> buscarUserPersonalizado(@PathVariable int id) {
        Object userFinded = dao.buscarUsuariosPorId(id);

        if(userFinded != null){
            return ResponseEntity.ok(userFinded);
        }
        return ResponseEntity.notFound().build();
    }
}
