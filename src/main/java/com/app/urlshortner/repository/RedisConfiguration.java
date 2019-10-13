package com.app.urlshortner.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;


@Component
@ComponentScan("com.app.urlshortner")
@EnableRedisRepositories
@PropertySource("application.properties")
public class RedisConfiguration {

  @Value("${spring.redis.host}")
  private String redisHost;
  @Value("${spring.redis.port:6379}")
  private String redisPort;
  @Value("${spring.redis.password}")
  private String password;
  // pool configuration
  @Value("${redis.maxTotal:20}")
  private String maxTotal;
  @Value("${redis.maxIdle:20}")
  private String maxIdle;
  @Value("${redis.minIdle:20}")
  private String minIdle;
  @Value("${redis.testOnBorrow:true}")
  private String testOnBorrow;
  @Value("${redis.testOnReturn:true}")
  private String testOnReturn;
  @Value("${redis.testWhileIdle:true}")
  private String testWhileIdle;
  @Value("${redis.use.pool:true}")
  private String redisUsePool;

  private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);


  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    final JedisPoolConfig poolConfig = buildPoolConfig();
    logger.debug("redisConnectionFactory");
    JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
    connectionFactory.setUsePool(Boolean.parseBoolean(redisUsePool));
    connectionFactory.setPassword(password);
    connectionFactory.setHostName(redisHost);
    connectionFactory.setPort(Integer.parseInt(redisPort));
    logger.debug("redisConnectionFactory done!");
    return connectionFactory;
  }


  @Bean("redisTemplate")
  RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(redisConnectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(customObjectMapper()));
    template.afterPropertiesSet();
    template.setEnableTransactionSupport(true);
    logger.debug("returning redisTemplate");
    return template;
  }

  /* Deserialize the value according to stored class*/
  private ObjectMapper customObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setVisibility(mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY)
        .withGetterVisibility(Visibility.NONE).withSetterVisibility(Visibility.NONE)
        .withCreatorVisibility(Visibility.NONE));
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new JodaModule());
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    mapper.setDateFormat(df);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.findAndRegisterModules();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL,
        "@class"); // enable default typing
    return mapper;
  }

  private JedisPoolConfig buildPoolConfig() {
    logger.debug("buildPoolConfig");
    final JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(Integer.parseInt(maxTotal));
    poolConfig.setMaxIdle(Integer.parseInt(maxIdle));
    poolConfig.setMinIdle(Integer.parseInt(minIdle));
    poolConfig.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
    poolConfig.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
    poolConfig.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
    logger.debug("buildPoolConfig done!");
    return poolConfig;
  }

}
