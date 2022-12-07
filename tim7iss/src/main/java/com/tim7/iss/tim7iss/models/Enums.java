package com.tim7.iss.tim7iss.models;

public class Enums {

    public enum UserType {
        DRIVER,
        PASSENGER
    }

    public enum ReviewType{
        VEHICLE,
        DRIVER,
    }
    public enum MessageType{
        SUPPORT,
        RIDE,
        PANIC,
    }

    public enum PaymentType{
        CARD,
        PAYPAL,
        BITCOIN,
        CASH,
    }

    public enum RideStatus{
        PENDING,
        ACCEPTED,
        REJECTED,
        ACTIVE,
        FINISHED,
        CANCELED,
    }

    public enum Day{
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        SUN,
    }
}
