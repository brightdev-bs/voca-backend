package vanille.vocabe.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vanille.vocabe.entity.User;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserCacheRepository {

    private final RedisTemplate<String, String> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);
    private final ObjectMapper objectMapper;

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}:{}", key, user);
        try {
            String userData = objectMapper.writeValueAsString(user);
            userRedisTemplate.opsForValue().set(key, userData, USER_CACHE_TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> getUser(String username) {
        String key = getKey(username);
        String user = userRedisTemplate.opsForValue().get(key);
        log.info("Get data from Redis {}:{}", key, user);
        try {
            return Optional.ofNullable(objectMapper.readValue(user, User.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getKey(String userName) {
        return "USER:" + userName;
    }
}
