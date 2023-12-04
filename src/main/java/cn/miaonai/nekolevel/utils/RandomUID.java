package cn.miaonai.nekolevel.utils;

import java.util.Random;

public class RandomUID {
    public static String RandomID() {
        String characters = "abdghilnoqrsuvwy0123456789";
        StringBuilder id = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }
        return id.toString();
    }
}
