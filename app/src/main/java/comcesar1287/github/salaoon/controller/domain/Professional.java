package comcesar1287.github.salaoon.controller.domain;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Professional {

    public String name, cpf, cep, address, opening, closing, specialty;

    public Professional() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Professional(String name, String cpf, String cep, String address, String opening, String closing, String specialty) {
        this.name = name;
        this.cpf = cpf;
        this.cep = cep;
        this.address = address;
        this.opening = opening;
        this.closing = closing;
        this.specialty = specialty;
    }
}
