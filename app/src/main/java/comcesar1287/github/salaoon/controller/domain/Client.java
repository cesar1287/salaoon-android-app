package comcesar1287.github.salaoon.controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Client {

    public String name, cpf, cep, address;

    public Client() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Client(String name, String cpf, String cep, String address) {
        this.name = name;
        this.cpf = cpf;
        this.cep = cep;
        this.address = address;
    }
}
