package entity;

public abstract class Person {
    private String OIB, firstName,lastName,email,phoneNumber;

    protected Person(PersonBuilder builder)
    {
        this.OIB = builder.OIB;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
    }


    public abstract static class PersonBuilder
    {
        private final String OIB;
        private final String firstName;
        private final String lastName;

        public PersonBuilder(String OIB,String firstName, String lastName)
        {
            this.OIB = OIB;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        private String email="";
        public PersonBuilder email(String email)
        {
            this.email = email;
            return this;
        }
        private String phoneNumber="";
        public PersonBuilder phoneNumber(String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public abstract Person build();


    }

     public abstract void printPersonalData();


    public String getOIB() {
        return OIB;
    }

    public void setOIB(String OIB) {
        this.OIB = OIB;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
