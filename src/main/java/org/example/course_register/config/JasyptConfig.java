package org.example.course_register.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {
  @Value("${jasypt.encryptor.password}")
  private String jasyptEncryptorPassword;

  @Bean(name = "jasyptEncryptorPassword")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setPassword(jasyptEncryptorPassword);
    encryptor.setAlgorithm("PBEWithMD5AndDES");

    return encryptor;
  }
}
