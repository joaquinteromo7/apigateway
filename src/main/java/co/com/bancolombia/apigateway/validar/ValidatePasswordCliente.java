package co.com.bancolombia.apigateway.validar;

import java.util.ArrayList;
import java.util.List;


public class ValidatePasswordCliente {

    public boolean validatePassword(String idPassword) {

        List<String> listPassword = new ArrayList<>();

        listPassword.add("1");
        listPassword.add("2");
        listPassword.add("3");
        listPassword.add("4");
        listPassword.add("5");


        for (String item : listPassword) {
            if (item.equals(idPassword.trim())) {
                return false;
            }
        }

        return true;
    }

}
