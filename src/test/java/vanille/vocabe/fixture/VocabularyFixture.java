package vanille.vocabe.fixture;

import vanille.vocabe.entity.Vocabulary;

public class VocabularyFixture {

    public static Vocabulary getVocabularyFixture(String name) {
        return Vocabulary.of(name, null, true);
    }

}
