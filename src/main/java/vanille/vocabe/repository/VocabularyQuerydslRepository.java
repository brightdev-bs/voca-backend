package vanille.vocabe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vanille.vocabe.entity.Vocabulary;

import java.util.List;

import static vanille.vocabe.entity.QVocabulary.vocabulary;

@Repository
@RequiredArgsConstructor
public class VocabularyQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<Vocabulary> findPublicVocabulariesLimitFive() {
        return queryFactory
                .selectFrom(vocabulary)
                .where(vocabulary.isPublic.eq(true))
                .orderBy(vocabulary.liked.desc())
                .limit(5)
                .fetch();
    }

    public List<Vocabulary> findVocabulariesByKeyword(String keyword) {
        return queryFactory
                .selectFrom(vocabulary)
                .where(vocabulary.name.contains(keyword))
                .limit(10)
                .fetch();
    }
}
