package trade_company;

import trade_company.logic.Hasher;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class PasswordHashCreator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.print("Введите желаемый пароль: ");
        Scanner reader = new Scanner(System.in);
        String string = reader.nextLine();
        reader.close();
        System.out.println("Хеш пароля: " + Hasher.createSHAHash(string));
    }
}
