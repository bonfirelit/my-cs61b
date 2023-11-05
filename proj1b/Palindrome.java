public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> ret = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            ret.addLast(word.charAt(i));
        }
        return ret;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);

        if (wordDeque.size() == 0 || wordDeque.size() == 1) {
            return true;
        }

        int i = 0;
        int j = wordDeque.size() - 1;
        while (i < j) {
            if (wordDeque.get(i) != wordDeque.get(j)) {
                return false;
            }
            i += 1;
            j -= 1;
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);

        if (wordDeque.size() == 0 || wordDeque.size() == 1) {
            return true;
        }

        int i = 0;
        int j = wordDeque.size() - 1;
        while (i < j) {
            if (!cc.equalChars(wordDeque.get(i),wordDeque.get(j))) {
                return false;
            }
            i += 1;
            j -= 1;
        }
        return true;
    }
}