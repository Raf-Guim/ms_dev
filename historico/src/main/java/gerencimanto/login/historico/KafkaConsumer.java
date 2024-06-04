package gerencimanto.login.historico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumer {

  @Autowired
  private FalhaLoginDao dao;
  @Value("${topic.name.consumer}")
  private String topicName;
  private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

  @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
  public void consume(String username) {
    FalhaLoginBean falhaLoginBean = new FalhaLoginBean();
    falhaLoginBean.setUsername(username);
    falhaLoginBean.setDate(new java.sql.Time(System.currentTimeMillis()));
    dao.save(falhaLoginBean);
    log.info("Tópico: {}", topicName);
    log.info("Mensagem: {}", username);
  }
}
