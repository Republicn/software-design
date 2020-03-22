package ru.itmo.republicn.react.repository;

import ru.itmo.republicn.react.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

}