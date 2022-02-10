package repository;

import domain.UserInfo;
import lombok.val;

public class TestDataGenerator {
    public UserInfo getUserInfo(boolean invalidPassword, boolean invalidUsername) {
        val output = new UserInfo();
        output.setUserName("vasya");
        output.setPassword("qwerty123");

        if (invalidPassword) {
            output.setPassword(getInvalidPassword());
        }

        if (invalidUsername) {
            output.setUserName(getInvalidUserName());
        }
        return output;
    }

    public String getInvalidPassword() {
        return "rmgkldf490404";
    }

    public String getInvalidUserName() {
        return "gghhhjd";
    }

    public String getInvalidCode() {
        return "1233";
    }
}
