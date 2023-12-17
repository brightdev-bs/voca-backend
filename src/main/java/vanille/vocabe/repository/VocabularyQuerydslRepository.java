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
    private final int HOME_PAGE_SIZE = 5;

    public List<Vocabulary> findPublicVocabulariesLimitFive(int page) {
        return queryFactory
                .selectFrom(vocabulary)
                .where(vocabulary.isPublic.eq(true))
                .orderBy(vocabulary.liked.desc())
                .offset((long) page * HOME_PAGE_SIZE)
                .limit(HOME_PAGE_SIZE)
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
