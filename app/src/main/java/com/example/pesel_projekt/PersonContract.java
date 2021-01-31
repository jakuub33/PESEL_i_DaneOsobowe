package com.example.pesel_projekt;

public final class PersonContract
{
    private PersonContract() {}

    //W tej klasie stworzymy schemat bazy danych
    public static class PersonEntry
    {
        public static final String TABLE_NAME = "person_info";
        public static final String PERSON_ID = "person_id";
        public static final String NAME = "name";
        public static final String SURNAME = "surname";
        public static final String AGE = "age";
        public static final String EMAIL = "email";
    }
}
