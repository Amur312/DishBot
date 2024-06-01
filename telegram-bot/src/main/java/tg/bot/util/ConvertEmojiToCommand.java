package tg.bot.util;

import org.springframework.stereotype.Component;

@Component
public class ConvertEmojiToCommand {
    public String convertEmojiToCommand(String text) {
        if (text.contains("\uD83D\uDCC3")) {
            return "/CATALOG";
        } else if (text.contains("\uD83D\uDED2")) {
            return "/BASKET";
        } else if (text.contains("\uD83D\uDD0D")) {
            return "/SEARCH";
        } else if (text.contains("\u260E")) {
            return "/CONTACTS";
        } else if(text.contains("\uD83D\uDCC4")){
            return "/ORDER_HISTORY";
        }
        return text;
    }
}
