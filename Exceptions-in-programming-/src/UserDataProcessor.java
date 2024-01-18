import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserDataProcessor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные в произвольном порядке (Фамилия Имя Отчество датарождения номертелефона пол):");
        String input = scanner.nextLine();

        try {
            UserData userData = processUserData(input);
            saveUserDataToFile(userData);
            System.out.println("Данные успешно обработаны и записаны в файл.");
        } catch (UserDataFormatException e) {
            System.err.println("Ошибка формата данных: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static UserData processUserData(String input) throws UserDataFormatException {
        String[] userDataArray = input.split(" ");

        if (userDataArray.length != 6) {
            throw new UserDataFormatException("Неверное количество данных. Ожидается 6 элементов (Фамилия Имя Отчество датарождения номертелефона пол).");
        }

        try {
            String surname = userDataArray[0];
            String name = userDataArray[1];
            String patronymic = userDataArray[2];
            String birthDateStr = userDataArray[3];
            String phoneNumberStr = userDataArray[4];
            char gender = userDataArray[5].charAt(0);

            if (!birthDateStr.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                throw new UserDataFormatException("Неверный формат даты. Дата должна быть введена в виде dd.mm.yyyy");
            }
            if (!phoneNumberStr.matches("[78]\\d{10}")) {
                throw new UserDataFormatException("Неверный формат номера телефона. Номер должен начинаться с 7 или 8 и содержать 11 цифр.");
            }
            if (gender != 'f' && gender != 'm') {
                throw new UserDataFormatException("Неверный символ пола. Используйте 'f' для женщин и 'm' для мужчин.");
            }
            long phoneNumber = Long.parseLong(phoneNumberStr);
            return new UserData(surname, name, patronymic, birthDateStr, phoneNumber, gender);
        } catch (NumberFormatException e) {
            throw new UserDataFormatException("Ошибка при обработке данных: " + e.getMessage());
        }
    }

    private static void saveUserDataToFile(UserData userData) throws IOException {
        String fileName = userData.getFileName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(userData.toString());
            writer.newLine();
        }
    }

    static class UserDataFormatException extends Exception {
        public UserDataFormatException(String message) {
            super(message);
        }
    }
}