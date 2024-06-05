package semicolon.MeetOn_Channel.domain.global.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UniqueIdMapper {
    private final Map<String, String> storage = new HashMap<>();
    private final ShortCodeGenerator codeGenerator = new ShortCodeGenerator();

    public String generateAndStore(String originalData) {
        String shortId = codeGenerator.generate();
        storage.put(shortId, originalData);
        return shortId;
    }

    public String getOriginal(String shortId) {
        return storage.get(shortId);
    }
}
