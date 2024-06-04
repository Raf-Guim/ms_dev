package gerenciamento.usuarios.atividade_1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "TAB_USUARIO")
public class UsuarioBean {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_USUARIO")
  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  private String senha;

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  private Integer total_logins;

  public Integer getTotal_logins() {
    return total_logins;
  }

  public void setTotal_logins(Integer total_logins) {
    this.total_logins = total_logins;
  }

  private Integer total_falhas;

  public Integer getTotal_falhas() {
    return total_falhas;
  }

  public void setTotal_falhas(Integer total_falhas) {
    this.total_falhas = total_falhas;
  }

  private boolean bloqueado;

  public boolean isBloqueado() {
    return bloqueado;
  }

  public void setBloqueado(boolean bloqueado) {
    this.bloqueado = bloqueado;
  }
}
