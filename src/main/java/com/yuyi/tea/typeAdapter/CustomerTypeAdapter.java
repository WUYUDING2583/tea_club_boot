package com.yuyi.tea.typeAdapter;


import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.yuyi.tea.bean.Customer;

public class CustomerTypeAdapter extends TypeAdapter {

    @Override
    public void write(JsonWriter out, Object o) throws IOException {
        Customer customer=(Customer)o;
        out.beginObject();
        out.name("uid").value(customer.getUid());
        out.name("name").value(customer.getName());
        out.name("email").value(customer.getEmail());
        out.name("contact").value((customer.getContact()));
        out.name("gender").value(customer.getGender());
        out.endObject();
    }

    @Override
    public Customer read(final JsonReader in) throws IOException {
        final Customer customer = new Customer();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "uid":
                    customer.setUid(in.nextInt());
                    break;
                case "name":
                    customer.setName(in.nextString());
                    break;
                case "contact":
                    customer.setContact(in.nextString());
                    break;
                case "email":
                    customer.setEmail(in.nextString());
                    break;
                case "gender":
                    customer.setGender(in.nextInt());
            }
        }
        in.endObject();

        return customer;
    }

}

