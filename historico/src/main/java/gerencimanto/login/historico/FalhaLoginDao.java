package gerencimanto.login.historico;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FalhaLoginDao extends CrudRepository<FalhaLoginBean, Integer> {
}
