package gerenciamento.usuarios.atividade_1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/usuario")
public class Usuario {

  @Autowired
  private UsuarioDAO dao;

  @Autowired
  private FalhaLoginKafaProducer kafkaProducer;

  // Existing code

  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody UsuarioBean usuario) throws IdDuplicadoException {

    if (dao.count() == 0) {
      System.out.println("Não há usuários");
      dao.save(usuario);
      return new ResponseEntity<String>(usuario.getUsername(), HttpStatus.CREATED);
    } else if (dao.findByUsername(usuario.getUsername()) != null) {
      System.out.println("Usuário já existe");
      return new ResponseEntity<String>("Usuário ja existente", HttpStatus.NOT_ACCEPTABLE);
    }
    System.out.println("Usuário não existe");
    usuario.setTotal_falhas(0);
    usuario.setTotal_logins(0);
    System.out.println(usuario);
    dao.save(usuario);
    return new ResponseEntity<String>(usuario.getUsername(), HttpStatus.CREATED);

  }

  @GetMapping()
  public ResponseEntity<Iterable<UsuarioBean>> todosUsuarios() {
    System.out.println(dao.count());

    if (dao.count() > 0) {
      return new ResponseEntity<Iterable<UsuarioBean>>(dao.findAll(), HttpStatus.OK);
    } else {
      System.out.println("Não há usuários");
      return new ResponseEntity<Iterable<UsuarioBean>>(HttpStatus.NO_CONTENT);
    }
  }

  @PutMapping()
  public ResponseEntity<String> updateUser(@RequestBody UsuarioBean usuario) {
    if (usuario.getUsername() == null || usuario.getSenha() == null) {
      return new ResponseEntity<String>("Usuário e senha não podem ser nulos", HttpStatus.BAD_REQUEST);
    }

    UsuarioBean userExists = dao.findByUsername(usuario.getUsername());
    if (userExists == null) {
      System.out.println("Usuário não existe");
      return new ResponseEntity<String>("Usuário não existe", HttpStatus.UNAUTHORIZED);
    }

    if (userExists.isBloqueado()) {
      System.out.println("Usuário bloqueado");
      return new ResponseEntity<String>("Usuário bloqueado", HttpStatus.UNAUTHORIZED);
    }
    try {
      if (userExists.getTotal_logins() > 10) {
        System.out.println("10 logins já foram feitos, favor alterar senha");
        return new ResponseEntity<String>("Usuário deve alterar a senha para logar novamente", HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
    }

    if (!userExists.getSenha().equals(usuario.getSenha())) {
      System.out.println("Senha incorreta");

      try {
        userExists.setTotal_falhas(userExists.getTotal_falhas() + 1);
      } catch (Exception e) {
        userExists.setTotal_falhas(1);
      }

      if (userExists.getTotal_falhas() > 5) {
        userExists.setBloqueado(true);
        System.out.println("Usuário bloqueado");
      }

      dao.save(userExists);
      kafkaProducer.sendMessage(userExists.getUsername());
      return new ResponseEntity<String>("Senha incorreta",
          HttpStatus.UNAUTHORIZED);
    }

    try {
      userExists.setTotal_logins(userExists.getTotal_logins() + 1);
    } catch (Exception e) {
      userExists.setTotal_logins(1);
    }

    dao.save(userExists);
    return new ResponseEntity<String>("Login efetuado", HttpStatus.OK);
  }

  @GetMapping("/bloqueados")
  public ResponseEntity<UsuarioBean[]> usuariosBloqueados() {
    UsuarioBean[] bloqueados = dao.findByBloqueadoTrue();

    System.out.println(bloqueados.length);

    if (bloqueados.length > 0) {
      return new ResponseEntity<UsuarioBean[]>(bloqueados, HttpStatus.OK);
    } else {
      System.out.println("Não há usuários bloqueados");
      return new ResponseEntity<UsuarioBean[]>(HttpStatus.NO_CONTENT);
    }
  }

  @PutMapping("/trocasenha")
  public ResponseEntity<String> trocaSenha(@RequestBody TrocaSenhaBean usuario) {
    UsuarioBean userExists = dao.findByUsername(usuario.getUsername());

    if (userExists == null) {
      System.out.println("Usuário não existe");
      return new ResponseEntity<String>("Usuário não existe", HttpStatus.NOT_FOUND);
    }

    if (userExists.isBloqueado()) {
      System.out.println("Usuário bloqueado");
      return new ResponseEntity<String>("Usuário bloqueado", HttpStatus.UNAUTHORIZED);
    }

    if (!userExists.getSenha().equals(usuario.getSenhaAtual())) {
      System.out.println("Senha atual incorreta");
      return new ResponseEntity<String>("Senha atual incorreta", HttpStatus.UNAUTHORIZED);
    }

    if (userExists.getSenha().equals(usuario.getSenhaNova())) {
      System.out.println("Senha atual e nova são iguais");
      return new ResponseEntity<String>("Senha atual e nova são iguais", HttpStatus.BAD_REQUEST);
    }

    userExists.setSenha(usuario.getSenhaNova());
    userExists.setTotal_logins(0);
    dao.save(userExists);
    return new ResponseEntity<String>("Senha alterada", HttpStatus.OK);
  }

  @PostMapping("/desbloquear/{username}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> unblock(@PathVariable String username) {
    System.out.println("TO AQUI PORRA!");
    if (username == null) {
      return new ResponseEntity<String>("Usuário não pode ser nulo", HttpStatus.BAD_REQUEST);
    }
    UsuarioBean user = dao.findByUsername(username);
    System.out.println(user);
    if (user == null) {
      return new ResponseEntity<String>("Usuário não existe", HttpStatus.NOT_FOUND);
    }
    if (!user.isBloqueado()) {
      return new ResponseEntity<String>("Usuário não está bloqueado", HttpStatus.BAD_REQUEST);
    }
    user.setTotal_falhas(0);
    user.setBloqueado(false);
    dao.save(user);
    return new ResponseEntity<String>("Usuário desbloqueado", HttpStatus.OK);
  }
}
