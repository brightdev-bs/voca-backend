package vanille.vocabe.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vanille.vocabe.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static vanille.vocabe.entity.QWord.word1;

@Repository
@RequiredArgsConstructor
public class WordQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<String> findByUserAndCreatedAtBetweenAndGroupBy(User user, LocalDate from, LocalDate to) {

        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , word1.createdAt
                , ConstantImpl.create("%m/%d/%Y")
        );
        return queryFactory
                .select(formattedDate)
                .from(word1)
                .where(
                        word1.user.eq(user)
                        .and(word1.createdAt.between(from, to))
                )
                .groupBy(formattedDate)
                .fetch();
    }
}