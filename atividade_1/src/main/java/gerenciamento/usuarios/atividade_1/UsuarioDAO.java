package gerenciamento.usuarios.atividade_1;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDAO extends CrudRepository<UsuarioBean, Integer> {

    // sempre inicia com findBy...
    // incluir nome do atributo
    UsuarioBean findByUsername(String username);

    Iterable<UsuarioBean> findByUsernameAndSenha(String username, String senha);

    UsuarioBean[] findByBloqueadoTrue();

    // // SELECT * FROM TAB_CURSO WHERE CURSO = ? AND TURMA = ?
    // Iterable<UsuarioBean> findByCursoAndTurma(String curso, String turma);

    // // SELECT * FROM TAB_ALUNO WHERE NOME LIKE ?
    // Iterable<UsuarioBean> findByNomeLike(String nome);

    // @Query("select a.id from TAB_ALUNO a")
    // Iterable<Integer> minhaConsulta();

}
