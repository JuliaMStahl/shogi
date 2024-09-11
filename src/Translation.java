import java.util.HashMap;
import java.util.Map;

public class Translation {
    //TODO Implement UI to change Language
    static Strings strings = new Strings();
    private static LanguagesEnum selectedLanguage = LanguagesEnum.EN;
    private static Map<LanguagesEnum, Map<StringsEnum, String>> translations;

    public static LanguagesEnum getSelectedLanguage() {
        return selectedLanguage;
    }

    public static void setSelectedLanguage(LanguagesEnum selectedLanguage) {
        Translation.selectedLanguage = selectedLanguage;
    }

    public static void InitTranslation() {
        translations = new HashMap<>();
        strings.StartStrings();
        translations.put(LanguagesEnum.PT, strings.getPortuguese());
        translations.put(LanguagesEnum.EN, strings.getEnglish());
    }

    public static String translate(StringsEnum key) {
        if (translations == null) {
            InitTranslation();
        }
        Map<StringsEnum, String> languageTranslations = translations.get(selectedLanguage);
        if (languageTranslations != null) {
            return languageTranslations.get(key);
        }
        return key.toString();
    }
}
