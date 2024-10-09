package net.mat0u5.do2smpmanager.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    private static HashMap<List<String>, List<String>> emotes = new HashMap<List<String>, List<String>>();
    private static final List<String> maxOne = List.of("");
    public static void setEmotes() {
        emotes.put(List.of("skull"),List.of("☠"));
        emotes.put(List.of("smile"),List.of("☺"));
        emotes.put(List.of("frown"),List.of("☹"));
        emotes.put(List.of("heart"),List.of("❤"));
        emotes.put(List.of("copyright"),List.of("©"));
        emotes.put(List.of("trademark","tm"),List.of("™"));

        emotes.put(List.of("mat","Mat0u5"), List.of("\uE0A1", "1293651244296044595"));
        emotes.put(List.of("gari","Garibaldi","Garibaldi_"), List.of("\uE0A2", "1293651157348257863"));
        emotes.put(List.of("onti","OntiMoose"), List.of("\uE0A3", "1293651158723858564"));
        emotes.put(List.of("simple","ItsSimpleAsThat"), List.of("\uE0A4", "1293651160376414280"));
        emotes.put(List.of("orb","ball","ontiball","onti_ball","oversized_onti_ball"), List.of("\uE0A5", "1293649858233634948"));
    }
    public static String replaceEmotes(String input) {
        for (String maxOneEmote : maxOne) {
            if (!input.contains(maxOneEmote)) continue;
            input = input.replaceFirst(":"+maxOneEmote+":", "_"+maxOneEmote+"_");
            input = input.replaceAll(":"+maxOneEmote+":", "");
            input = input.replaceFirst("_"+maxOneEmote+"_", ":"+maxOneEmote+":");
        }
        for (Map.Entry<List<String>, List<String>> entry : emotes.entrySet()) {
            if (entry.getValue().size()==0) continue;
            String emoteValue = entry.getValue().get(0);
            for (String emote : entry.getKey()) {
                String emoteCode = ":" + emote + ":";
                input = replaceCaseInsensitive(input, emoteCode, emoteValue);
            }
            if (!input.contains(":")) return input;
        }
        return input;
    }
    public static String replaceEmotesDiscord(String input) {
        for (Map.Entry<List<String>, List<String>> entry : emotes.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            String emoteValue = entry.getValue().get(0);

            if (entry.getValue().size() > 1) {
                String emoteID = entry.getValue().get(1);
                for (String emote : entry.getKey()) {
                    String emoteCode = "<:" + emote + ":"+emoteID+">";
                    input = replaceCaseInsensitive(input, emoteCode, emoteValue);
                }
            }
            for (String emote : entry.getKey()) {
                String emoteCode = ":" + emote + ":";
                input = replaceCaseInsensitive(input, emoteCode, emoteValue);
            }
            if (!input.contains(":")) return input;
        }
        return input;
    }
    public static String formatEmotesForDiscord(String input) {
        for (Map.Entry<List<String>, List<String>> entry : emotes.entrySet()) {
            if (entry.getValue().size() <=1) continue;
            String emoteValue = entry.getValue().get(1);
            for (String emote : entry.getKey()) {
                String emoteCode = ":" + emote + ":";
                input = replaceCaseInsensitive(input, emoteCode, "<"+emoteCode+emoteValue+">");
            }
            if (!input.contains(":")) return input;
        }
        return input;
    }
    public static String replaceCaseInsensitive(String input, String replaceWhat, String replaceWith) {
        Pattern pattern = Pattern.compile(replaceWhat, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        String result = matcher.replaceAll(replaceWith);
        return result;
    }
    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
